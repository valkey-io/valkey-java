package io.valkey;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.valkey.annots.Internal;
import io.valkey.exceptions.JedisClusterOperationException;
import io.valkey.exceptions.JedisException;
import io.valkey.util.IOUtils;
import io.valkey.util.SafeEncoder;

/**
 * JedisClusterInfoCache maintains the cluster topology (slot-to-node mapping) and provides
 * two complementary mechanisms to keep it up-to-date:
 *
 * <ol>
 *   <li><b>Periodic topology refresh</b> (existing): a background thread calls
 *       {@code CLUSTER SLOTS} at a configurable interval.</li>
 *   <li><b>Failover broadcast listener</b> (new, feature/cluster-failover-pubsub): a
 *       per-node subscriber thread listens on the {@code +switch-master} Pub/Sub channel.
 *       When the Valkey server publishes a failover notification (introduced by the
 *       companion server-side patch in {@code cluster_legacy.c}), the listener immediately
 *       triggers a full slot-cache refresh so that clients react in real time instead of
 *       waiting for the next periodic cycle or an error-driven refresh.</li>
 * </ol>
 *
 * The failover listener is started automatically when the cache is constructed with a
 * non-null {@code startNodes} set.  It can be disabled by calling
 * {@link #setFailoverListenerEnabled(boolean)} before the first use.
 */
@Internal
public class JedisClusterInfoCache {

  private static final Logger logger = LoggerFactory.getLogger(JedisClusterInfoCache.class);

  /** Pub/Sub channel published by the server on every cluster master failover. */
  static final String CLUSTER_FAILOVER_CHANNEL = "+switch-master";

  private final Map<String, ConnectionPool> nodes = new HashMap<>();
  private final ConnectionPool[] slots = new ConnectionPool[Protocol.CLUSTER_HASHSLOTS];
  private final HostAndPort[] slotNodes = new HostAndPort[Protocol.CLUSTER_HASHSLOTS];

  private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
  private final Lock r = rwl.readLock();
  private final Lock w = rwl.writeLock();
  private final Lock rediscoverLock = new ReentrantLock();

  private final GenericObjectPoolConfig<Connection> poolConfig;
  private final JedisClientConfig clientConfig;
  private final Set<HostAndPort> startNodes;

  private static final int MASTER_NODE_INDEX = 2;

  /** Whether the failover broadcast listener is enabled (default: true). */
  private volatile boolean failoverListenerEnabled = true;

  /**
   * The single thread executor for the periodic topology refresh task.
   */
  private ScheduledExecutorService topologyRefreshExecutor = null;

  /**
   * Background threads that subscribe to {@value #CLUSTER_FAILOVER_CHANNEL} on every
   * known cluster node.  Each thread triggers an immediate slot-cache refresh when a
   * failover notification is received.
   */
  private final List<FailoverListener> failoverListeners = new ArrayList<>();

  // -------------------------------------------------------------------------
  // Inner classes
  // -------------------------------------------------------------------------

  class TopologyRefreshTask implements Runnable {
    @Override
    public void run() {
      logger.debug("Cluster topology refresh run, old nodes: {}", nodes.keySet());
      renewClusterSlots(null);
      logger.debug("Cluster topology refresh run, new nodes: {}", nodes.keySet());
    }
  }

  /**
   * Background thread that subscribes to the {@value #CLUSTER_FAILOVER_CHANNEL} channel on
   * a single cluster node.  When a failover notification is received the thread immediately
   * calls {@link #renewClusterSlots(Connection)} so that the slot cache is updated without
   * waiting for the next periodic refresh cycle.
   *
   * <p>The thread reconnects automatically after connection failures, using an exponential
   * back-off capped at {@value #MAX_RETRY_WAIT_MILLIS} ms.
   */
  class FailoverListener extends Thread {

    private static final long INITIAL_RETRY_WAIT_MILLIS = 1_000L;
    private static final long MAX_RETRY_WAIT_MILLIS = 30_000L;

    private final HostAndPort node;
    private volatile Jedis jedis;
    private volatile boolean running = false;

    FailoverListener(HostAndPort node) {
      super("ClusterFailoverListener-[" + node + "]");
      this.node = node;
      setDaemon(true);
    }

    @Override
    public void run() {
      running = true;
      long retryWaitMillis = INITIAL_RETRY_WAIT_MILLIS;

      while (running) {
        try {
          if (!running) break;

          jedis = new Jedis(node, clientConfig);

          // Perform an immediate refresh when (re-)connecting so that any failover
          // that happened while the listener was disconnected is not missed.
          logger.debug("FailoverListener connected to {}, performing initial slot refresh.", node);
          renewClusterSlots(jedis.getClient());

          // Reset back-off on successful connection.
          retryWaitMillis = INITIAL_RETRY_WAIT_MILLIS;

          jedis.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
              logger.info(
                  "Cluster failover notification received from {} on channel '{}': {}",
                  node, channel, message);
              // Trigger an immediate topology refresh.  We pass null so that the
              // implementation falls back to startNodes / shuffled pool for the
              // CLUSTER SLOTS query, which is safer than reusing the subscriber
              // connection that is currently blocked in the subscribe loop.
              renewClusterSlots(null);
            }
          }, CLUSTER_FAILOVER_CHANNEL);

        } catch (JedisException e) {
          if (running) {
            logger.warn(
                "FailoverListener lost connection to {}. Retrying in {}ms.", node, retryWaitMillis, e);
            try {
              Thread.sleep(retryWaitMillis);
            } catch (InterruptedException ie) {
              Thread.currentThread().interrupt();
              logger.debug("FailoverListener interrupted while sleeping.", ie);
            }
            // Exponential back-off.
            retryWaitMillis = Math.min(retryWaitMillis * 2, MAX_RETRY_WAIT_MILLIS);
          } else {
            logger.debug("FailoverListener shutting down for {}.", node);
          }
        } finally {
          IOUtils.closeQuietly(jedis);
        }
      }
    }

    void shutdown() {
      running = false;
      IOUtils.closeQuietly(jedis);
    }
  }

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public JedisClusterInfoCache(final JedisClientConfig clientConfig, final Set<HostAndPort> startNodes) {
    this(clientConfig, null, startNodes);
  }

  public JedisClusterInfoCache(final JedisClientConfig clientConfig,
      final GenericObjectPoolConfig<Connection> poolConfig, final Set<HostAndPort> startNodes) {
    this(clientConfig, poolConfig, startNodes, null);
  }

  public JedisClusterInfoCache(final JedisClientConfig clientConfig,
      final GenericObjectPoolConfig<Connection> poolConfig, final Set<HostAndPort> startNodes,
      final Duration topologyRefreshPeriod) {
    this.poolConfig = poolConfig;
    this.clientConfig = clientConfig;
    this.startNodes = startNodes;
    if (topologyRefreshPeriod != null) {
      logger.info("Cluster topology refresh start, period: {}, startNodes: {}", topologyRefreshPeriod, startNodes);
      topologyRefreshExecutor = Executors.newSingleThreadScheduledExecutor();
      topologyRefreshExecutor.scheduleWithFixedDelay(new TopologyRefreshTask(), topologyRefreshPeriod.toMillis(),
          topologyRefreshPeriod.toMillis(), TimeUnit.MILLISECONDS);
    }
  }

  // -------------------------------------------------------------------------
  // Failover listener management
  // -------------------------------------------------------------------------

  /**
   * Enable or disable the failover broadcast listener.  Must be called before the cache is
   * used (i.e. before {@link #discoverClusterNodesAndSlots(Connection)} is invoked).
   *
   * @param enabled {@code true} to enable (default), {@code false} to disable
   */
  public void setFailoverListenerEnabled(boolean enabled) {
    this.failoverListenerEnabled = enabled;
  }

  /**
   * Start a {@link FailoverListener} for every node currently known to the cache.
   * Called automatically after the initial slot discovery.
   */
  private void startFailoverListeners() {
    if (!failoverListenerEnabled || startNodes == null || startNodes.isEmpty()) {
      return;
    }
    // Stop any existing listeners first (e.g. after a full reset).
    stopFailoverListeners();
    for (HostAndPort node : startNodes) {
      FailoverListener listener = new FailoverListener(node);
      failoverListeners.add(listener);
      listener.start();
      logger.info("Started FailoverListener for cluster node {}.", node);
    }
  }

  private void stopFailoverListeners() {
    for (FailoverListener listener : failoverListeners) {
      listener.shutdown();
    }
    failoverListeners.clear();
  }

  // -------------------------------------------------------------------------
  // Slot discovery
  // -------------------------------------------------------------------------

  /**
   * Check whether the number and order of slots in the cluster topology are equal to CLUSTER_HASHSLOTS
   * @param slotsInfo the cluster topology
   * @return if slots is ok, return true, else return false.
   */
  private boolean checkClusterSlotSequence(List<Object> slotsInfo) {
    List<Integer> slots = new ArrayList<>();
    for (Object slotInfoObj : slotsInfo) {
      List<Object> slotInfo = (List<Object>)slotInfoObj;
      slots.addAll(getAssignedSlotArray(slotInfo));
    }
    Collections.sort(slots);
    if (slots.size() != Protocol.CLUSTER_HASHSLOTS) {
      return false;
    }
    for (int i = 0; i < Protocol.CLUSTER_HASHSLOTS; ++i) {
      if (i != slots.get(i)) {
        return false;
      }
    }
    return true;
  }

  public void discoverClusterNodesAndSlots(Connection jedis) {
    List<Object> slotsInfo = executeClusterSlots(jedis);
    if (System.getProperty(JedisCluster.INIT_NO_ERROR_PROPERTY) == null) {
      if (slotsInfo.isEmpty()) {
        throw new JedisClusterOperationException("Cluster slots list is empty.");
      }
      if (!checkClusterSlotSequence(slotsInfo)) {
        throw new JedisClusterOperationException("Cluster slots have holes.");
      }
    }
    w.lock();
    try {
      reset();
      for (Object slotInfoObj : slotsInfo) {
        List<Object> slotInfo = (List<Object>) slotInfoObj;

        if (slotInfo.size() <= MASTER_NODE_INDEX) {
          continue;
        }

        List<Integer> slotNums = getAssignedSlotArray(slotInfo);

        // hostInfos
        int size = slotInfo.size();
        for (int i = MASTER_NODE_INDEX; i < size; i++) {
          List<Object> hostInfos = (List<Object>) slotInfo.get(i);
          if (hostInfos.isEmpty()) {
            continue;
          }

          HostAndPort targetNode = generateHostAndPort(hostInfos);
          setupNodeIfNotExist(targetNode);
          if (i == MASTER_NODE_INDEX) {
            assignSlotsToNode(slotNums, targetNode);
          }
        }
      }
    } finally {
      w.unlock();
    }
    // Start failover listeners after the initial topology is known.
    startFailoverListeners();
  }

  public void renewClusterSlots(Connection jedis) {
    // If rediscovering is already in process - no need to start one more same rediscovering, just return
    if (rediscoverLock.tryLock()) {
      try {
        // First, if jedis is available, use jedis renew.
        if (jedis != null) {
          try {
            discoverClusterSlots(jedis);
            return;
          } catch (JedisException e) {
            // try nodes from all pools
          }
        }

        // Then, we use startNodes to try, as long as startNodes is available,
        // whether it is vip, domain, or physical ip, it will succeed.
        if (startNodes != null) {
          for (HostAndPort hostAndPort : startNodes) {
            try (Connection j = new Connection(hostAndPort, clientConfig)) {
              discoverClusterSlots(j);
              return;
            } catch (JedisException e) {
              // try next nodes
            }
          }
        }

        // Finally, we go back to the ShuffledNodesPool and try the remaining physical nodes.
        for (ConnectionPool jp : getShuffledNodesPool()) {
          try (Connection j = jp.getResource()) {
            // If already tried in startNodes, skip this node.
            if (startNodes != null && startNodes.contains(j.getHostAndPort())) {
              continue;
            }
            discoverClusterSlots(j);
            return;
          } catch (JedisException e) {
            // try next nodes
          }
        }

      } finally {
        rediscoverLock.unlock();
      }
    }
  }

  private void discoverClusterSlots(Connection jedis) {
    List<Object> slotsInfo = executeClusterSlots(jedis);
    if (System.getProperty(JedisCluster.INIT_NO_ERROR_PROPERTY) == null) {
      if (slotsInfo.isEmpty()) {
        throw new JedisClusterOperationException("Cluster slots list is empty.");
      }
      if (!checkClusterSlotSequence(slotsInfo)) {
        throw new JedisClusterOperationException("Cluster slots have holes.");
      }
    }
    w.lock();
    try {
      Arrays.fill(slots, null);
      Arrays.fill(slotNodes, null);
      Set<String> hostAndPortKeys = new HashSet<>();

      for (Object slotInfoObj : slotsInfo) {
        List<Object> slotInfo = (List<Object>) slotInfoObj;

        if (slotInfo.size() <= MASTER_NODE_INDEX) {
          continue;
        }

        List<Integer> slotNums = getAssignedSlotArray(slotInfo);

        int size = slotInfo.size();
        for (int i = MASTER_NODE_INDEX; i < size; i++) {
          List<Object> hostInfos = (List<Object>) slotInfo.get(i);
          if (hostInfos.isEmpty()) {
            continue;
          }

          HostAndPort targetNode = generateHostAndPort(hostInfos);
          hostAndPortKeys.add(getNodeKey(targetNode));
          setupNodeIfNotExist(targetNode);
          if (i == MASTER_NODE_INDEX) {
            assignSlotsToNode(slotNums, targetNode);
          }
        }
      }

      // Remove dead nodes according to the latest query
      Iterator<Entry<String, ConnectionPool>> entryIt = nodes.entrySet().iterator();
      while (entryIt.hasNext()) {
        Entry<String, ConnectionPool> entry = entryIt.next();
        if (!hostAndPortKeys.contains(entry.getKey())) {
          ConnectionPool pool = entry.getValue();
          try {
            if (pool != null) {
              pool.destroy();
            }
          } catch (Exception e) {
            // pass, may be this node dead
          }
          entryIt.remove();
        }
      }
    } finally {
      w.unlock();
    }
  }

  private HostAndPort generateHostAndPort(List<Object> hostInfos) {
    String host = SafeEncoder.encode((byte[]) hostInfos.get(0));
    int port = ((Long) hostInfos.get(1)).intValue();
    return new HostAndPort(host, port);
  }

  public ConnectionPool setupNodeIfNotExist(final HostAndPort node) {
    w.lock();
    try {
      String nodeKey = getNodeKey(node);
      ConnectionPool existingPool = nodes.get(nodeKey);
      if (existingPool != null) return existingPool;

      ConnectionPool nodePool = poolConfig == null ? new ConnectionPool(node, clientConfig)
          : new ConnectionPool(node, clientConfig, poolConfig);
      nodes.put(nodeKey, nodePool);
      return nodePool;
    } finally {
      w.unlock();
    }
  }

  public void assignSlotToNode(int slot, HostAndPort targetNode) {
    w.lock();
    try {
      ConnectionPool targetPool = setupNodeIfNotExist(targetNode);
      slots[slot] = targetPool;
      slotNodes[slot] = targetNode;
    } finally {
      w.unlock();
    }
  }

  public void assignSlotsToNode(List<Integer> targetSlots, HostAndPort targetNode) {
    w.lock();
    try {
      ConnectionPool targetPool = setupNodeIfNotExist(targetNode);
      for (Integer slot : targetSlots) {
        slots[slot] = targetPool;
        slotNodes[slot] = targetNode;
      }
    } finally {
      w.unlock();
    }
  }

  public ConnectionPool getNode(String nodeKey) {
    r.lock();
    try {
      return nodes.get(nodeKey);
    } finally {
      r.unlock();
    }
  }

  public ConnectionPool getNode(HostAndPort node) {
    return getNode(getNodeKey(node));
  }

  public ConnectionPool getSlotPool(int slot) {
    r.lock();
    try {
      return slots[slot];
    } finally {
      r.unlock();
    }
  }

  public HostAndPort getSlotNode(int slot) {
    r.lock();
    try {
      return slotNodes[slot];
    } finally {
      r.unlock();
    }
  }

  public Map<String, ConnectionPool> getNodes() {
    r.lock();
    try {
      return new HashMap<>(nodes);
    } finally {
      r.unlock();
    }
  }

  public List<ConnectionPool> getShuffledNodesPool() {
    r.lock();
    try {
      List<ConnectionPool> pools = new ArrayList<>(nodes.values());
      Collections.shuffle(pools);
      return pools;
    } finally {
      r.unlock();
    }
  }

  /**
   * Clear discovered nodes collections and gently release allocated resources
   */
  public void reset() {
    w.lock();
    try {
      for (ConnectionPool pool : nodes.values()) {
        try {
          if (pool != null) {
            pool.destroy();
          }
        } catch (RuntimeException e) {
          // pass
        }
      }
      nodes.clear();
      Arrays.fill(slots, null);
      Arrays.fill(slotNodes, null);
    } finally {
      w.unlock();
    }
  }

  public void close() {
    reset();
    stopFailoverListeners();
    if (topologyRefreshExecutor != null) {
      logger.info("Cluster topology refresh shutdown, startNodes: {}", startNodes);
      topologyRefreshExecutor.shutdownNow();
    }
  }

  public static String getNodeKey(HostAndPort hnp) {
    //return hnp.getHost() + ":" + hnp.getPort();
    return hnp.toString();
  }

  private List<Object> executeClusterSlots(Connection jedis) {
    jedis.sendCommand(Protocol.Command.CLUSTER, "SLOTS");
    return jedis.getObjectMultiBulkReply();
  }

  private List<Integer> getAssignedSlotArray(List<Object> slotInfo) {
    List<Integer> slotNums = new ArrayList<>();
    for (int slot = ((Long) slotInfo.get(0)).intValue(); slot <= ((Long) slotInfo.get(1))
        .intValue(); slot++) {
      slotNums.add(slot);
    }
    return slotNums;
  }
}
