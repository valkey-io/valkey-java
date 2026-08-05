package io.valkey.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.valkey.CommandArguments;
import io.valkey.Connection;
import io.valkey.ConnectionPool;
import io.valkey.HostAndPort;
import io.valkey.Jedis;
import io.valkey.JedisClientConfig;
import io.valkey.JedisPubSub;
import io.valkey.util.IOUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.valkey.exceptions.JedisConnectionException;
import io.valkey.exceptions.JedisException;

/**
 * Sentinel-mode connection provider that combines two complementary failover-detection
 * strategies:
 *
 * <ol>
 *   <li><b>Pub/Sub broadcast listener</b> (existing): each {@link SentinelListener} thread
 *       subscribes to the {@code +switch-master} channel on its sentinel node and calls
 *       {@link #initMaster} immediately when a failover event is published.</li>
 *   <li><b>Periodic active probe</b> (new, feature/failover-enhancement): a separate
 *       single-threaded scheduler calls {@code SENTINEL GETMASTERADDRBYNAME} on every
 *       sentinel at a configurable interval.  This ensures that failovers are detected even
 *       when the Pub/Sub connection is temporarily disrupted or when the sentinel does not
 *       publish a notification (e.g. due to a network partition).</li>
 * </ol>
 *
 * <p>The probe interval defaults to {@value #DEFAULT_PROBE_PERIOD_MILLIS} ms and can be
 * customised via the constructor that accepts {@code probePeriodMillis}.  Set the interval
 * to {@code 0} or a negative value to disable the active probe entirely.</p>
 */
public class SentineledConnectionProvider implements ConnectionProvider {

  private static final Logger LOG = LoggerFactory.getLogger(SentineledConnectionProvider.class);

  protected static final long DEFAULT_SUBSCRIBE_RETRY_WAIT_TIME_MILLIS = 5000;

  /**
   * Default interval (ms) for the periodic active-probe task.
   * 10 seconds is a reasonable default: fast enough to catch missed events,
   * slow enough not to flood sentinels with queries.
   */
  public static final long DEFAULT_PROBE_PERIOD_MILLIS = 10_000L;

  private volatile HostAndPort currentMaster;

  private volatile ConnectionPool pool;

  private final String masterName;

  private final JedisClientConfig masterClientConfig;

  private final GenericObjectPoolConfig<Connection> masterPoolConfig;

  protected final Collection<SentinelListener> sentinelListeners = new ArrayList<>();

  private final JedisClientConfig sentinelClientConfig;

  private final long subscribeRetryWaitTimeMillis;

  /**
   * Interval (ms) for the active-probe scheduler.  {@code <= 0} disables the probe.
   */
  private final long probePeriodMillis;

  /**
   * The sentinel nodes used for active probing (same set as the listeners).
   */
  private volatile Set<HostAndPort> sentinelNodes;

  /**
   * Single-threaded scheduler that periodically queries sentinels for the current master.
   */
  private ScheduledExecutorService probeExecutor = null;

  private final Object initPoolLock = new Object();

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public SentineledConnectionProvider(String masterName, final JedisClientConfig masterClientConfig,
      Set<HostAndPort> sentinels, final JedisClientConfig sentinelClientConfig) {
    this(masterName, masterClientConfig, /*poolConfig*/ null, sentinels, sentinelClientConfig);
  }

  public SentineledConnectionProvider(String masterName, final JedisClientConfig masterClientConfig,
      final GenericObjectPoolConfig<Connection> poolConfig,
      Set<HostAndPort> sentinels, final JedisClientConfig sentinelClientConfig) {
    this(masterName, masterClientConfig, poolConfig, sentinels, sentinelClientConfig,
        DEFAULT_SUBSCRIBE_RETRY_WAIT_TIME_MILLIS);
  }

  public SentineledConnectionProvider(String masterName, final JedisClientConfig masterClientConfig,
      final GenericObjectPoolConfig<Connection> poolConfig,
      Set<HostAndPort> sentinels, final JedisClientConfig sentinelClientConfig,
      final long subscribeRetryWaitTimeMillis) {
    this(masterName, masterClientConfig, poolConfig, sentinels, sentinelClientConfig,
        subscribeRetryWaitTimeMillis, DEFAULT_PROBE_PERIOD_MILLIS);
  }

  /**
   * Full constructor that exposes the active-probe interval.
   *
   * @param probePeriodMillis interval between active-probe cycles in milliseconds;
   *                          {@code <= 0} disables the active probe
   */
  public SentineledConnectionProvider(String masterName, final JedisClientConfig masterClientConfig,
      final GenericObjectPoolConfig<Connection> poolConfig,
      Set<HostAndPort> sentinels, final JedisClientConfig sentinelClientConfig,
      final long subscribeRetryWaitTimeMillis, final long probePeriodMillis) {

    this.masterName = masterName;
    this.masterClientConfig = masterClientConfig;
    this.masterPoolConfig = poolConfig;

    this.sentinelClientConfig = sentinelClientConfig;
    this.subscribeRetryWaitTimeMillis = subscribeRetryWaitTimeMillis;
    this.probePeriodMillis = probePeriodMillis;

    HostAndPort master = initSentinels(sentinels);
    initMaster(master);

    if (probePeriodMillis > 0) {
      startActiveProbe(sentinels);
    }
  }

  // -------------------------------------------------------------------------
  // ConnectionProvider interface
  // -------------------------------------------------------------------------

  @Override
  public Connection getConnection() {
    return pool.getResource();
  }

  @Override
  public Connection getConnection(CommandArguments args) {
    return pool.getResource();
  }

  @Override
  public void close() {
    sentinelListeners.forEach(SentinelListener::shutdown);
    stopActiveProbe();
    pool.close();
  }

  public HostAndPort getCurrentMaster() {
    return currentMaster;
  }

  // -------------------------------------------------------------------------
  // Master pool management
  // -------------------------------------------------------------------------

  private void initMaster(HostAndPort master) {
    synchronized (initPoolLock) {
      if (!master.equals(currentMaster)) {
        currentMaster = master;

        ConnectionPool newPool = masterPoolConfig != null
            ? new ConnectionPool(currentMaster, masterClientConfig, masterPoolConfig)
            : new ConnectionPool(currentMaster, masterClientConfig);

        ConnectionPool existingPool = pool;
        pool = newPool;
        LOG.info("Created connection pool to master at {}.", master);

        if (existingPool != null) {
          // although we clear the pool, we still have to check the returned object in getResource,
          // this call only clears idle instances, not borrowed instances
          // existingPool.clear(); // necessary??
          existingPool.close();
        }
      }
    }
  }

  // -------------------------------------------------------------------------
  // Sentinel initialisation
  // -------------------------------------------------------------------------

  private HostAndPort initSentinels(Set<HostAndPort> sentinels) {

    HostAndPort master = null;
    boolean sentinelAvailable = false;

    LOG.debug("Trying to find master from available sentinels...");

    for (HostAndPort sentinel : sentinels) {

      LOG.debug("Connecting to Sentinel {}...", sentinel);

      try (Jedis jedis = new Jedis(sentinel, sentinelClientConfig)) {

        List<String> masterAddr = jedis.sentinelGetMasterAddrByName(masterName);

        // connected to sentinel...
        sentinelAvailable = true;

        if (masterAddr == null || masterAddr.size() != 2) {
          LOG.warn("Sentinel {} is not monitoring master {}.", sentinel, masterName);
          continue;
        }

        master = toHostAndPort(masterAddr);
        LOG.debug("Redis master reported at {}.", master);
        break;
      } catch (JedisException e) {
        // resolves #1036, it should handle JedisException there's another chance
        // of raising JedisDataException
        LOG.warn("Could not get master address from {}.", sentinel, e);
      }
    }

    if (master == null) {
      if (sentinelAvailable) {
        // can connect to sentinel, but master name seems to not monitored
        throw new JedisException(
            "Can connect to sentinel, but " + masterName + " seems to be not monitored.");
      } else {
        throw new JedisConnectionException(
            "All sentinels down, cannot determine where " + masterName + " is running.");
      }
    }

    LOG.info("Redis master running at {}. Starting sentinel listeners...", master);

    for (HostAndPort sentinel : sentinels) {

      SentinelListener listener = new SentinelListener(sentinel);
      // whether SentinelListener threads are alive or not, process can be stopped
      listener.setDaemon(true);
      sentinelListeners.add(listener);
      listener.start();
    }

    return master;
  }

  // -------------------------------------------------------------------------
  // Active probe scheduler
  // -------------------------------------------------------------------------

  /**
   * Start the periodic active-probe scheduler.
   *
   * <p>The probe task iterates over all sentinel nodes and calls
   * {@code SENTINEL GETMASTERADDRBYNAME} on the first reachable one.  If the returned
   * master address differs from {@link #currentMaster} the pool is updated immediately,
   * providing a safety net for cases where the Pub/Sub notification was missed.</p>
   *
   * @param sentinels the set of sentinel nodes to probe
   */
  private void startActiveProbe(Set<HostAndPort> sentinels) {
    this.sentinelNodes = sentinels;
    probeExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
      Thread t = new Thread(r, "SentinelActiveProbe-" + masterName);
      t.setDaemon(true);
      return t;
    });
    probeExecutor.scheduleWithFixedDelay(
        new ActiveProbeTask(),
        probePeriodMillis,
        probePeriodMillis,
        TimeUnit.MILLISECONDS);
    LOG.info("Sentinel active probe started for master '{}', interval={}ms.", masterName, probePeriodMillis);
  }

  private void stopActiveProbe() {
    if (probeExecutor != null) {
      probeExecutor.shutdownNow();
      probeExecutor = null;
      LOG.info("Sentinel active probe stopped for master '{}'.", masterName);
    }
  }

  /**
   * Runnable executed by the active-probe scheduler.
   * Queries each sentinel in turn until one responds, then updates the master pool if needed.
   */
  private class ActiveProbeTask implements Runnable {

    @Override
    public void run() {
      if (sentinelNodes == null) return;

      for (HostAndPort sentinel : sentinelNodes) {
        try (Jedis jedis = new Jedis(sentinel, sentinelClientConfig)) {
          List<String> masterAddr = jedis.sentinelGetMasterAddrByName(masterName);
          if (masterAddr != null && masterAddr.size() == 2) {
            HostAndPort probedMaster = toHostAndPort(masterAddr);
            if (!probedMaster.equals(currentMaster)) {
              LOG.info(
                  "Active probe detected master change for '{}': {} -> {}. Updating pool.",
                  masterName, currentMaster, probedMaster);
              initMaster(probedMaster);
            } else {
              LOG.debug("Active probe: master '{}' still at {}.", masterName, currentMaster);
            }
            // Successfully queried one sentinel – no need to try the rest.
            return;
          } else {
            LOG.warn("Active probe: sentinel {} returned no address for master '{}'.", sentinel, masterName);
          }
        } catch (JedisException e) {
          LOG.warn("Active probe: could not reach sentinel {}. Trying next.", sentinel, e);
        }
      }

      LOG.warn("Active probe: all sentinels unreachable for master '{}'.", masterName);
    }
  }

  // -------------------------------------------------------------------------
  // Helpers
  // -------------------------------------------------------------------------

  /**
   * Must be of size 2.
   */
  private static HostAndPort toHostAndPort(List<String> masterAddr) {
    return toHostAndPort(masterAddr.get(0), masterAddr.get(1));
  }

  private static HostAndPort toHostAndPort(String hostStr, String portStr) {
    return new HostAndPort(hostStr, Integer.parseInt(portStr));
  }

  // -------------------------------------------------------------------------
  // SentinelListener inner class
  // -------------------------------------------------------------------------

  /**
   * Background thread that subscribes to the {@code +switch-master} Pub/Sub channel on a
   * single sentinel node.  When a failover event is published the thread immediately calls
   * {@link #initMaster} to switch the connection pool to the new master.
   *
   * <p>The thread reconnects automatically after connection failures, sleeping
   * {@link #subscribeRetryWaitTimeMillis} ms between attempts.</p>
   */
  protected class SentinelListener extends Thread {

    protected final HostAndPort node;
    protected volatile Jedis sentinelJedis;
    protected AtomicBoolean running = new AtomicBoolean(false);

    public SentinelListener(HostAndPort node) {
      super(String.format("%s-SentinelListener-[%s]", masterName, node.toString()));
      this.node = node;
    }

    @Override
    public void run() {

      running.set(true);

      while (running.get()) {

        try {
          // double check that it is not being shutdown
          if (!running.get()) {
            break;
          }

          sentinelJedis = new Jedis(node, sentinelClientConfig);

          // Perform an active refresh immediately on (re-)connect so that any failover
          // that happened while the listener was disconnected is not missed.
          List<String> masterAddr = sentinelJedis.sentinelGetMasterAddrByName(masterName);
          if (masterAddr == null || masterAddr.size() != 2) {
            LOG.warn("Cannot get master {} address. Sentinel: {}.", masterName, node);
          } else {
            initMaster(toHostAndPort(masterAddr));
          }

          sentinelJedis.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
              LOG.debug("Sentinel {} published: {}.", node, message);

              String[] switchMasterMsg = message.split(" ");

              if (switchMasterMsg.length > 3) {

                if (masterName.equals(switchMasterMsg[0])) {
                  initMaster(toHostAndPort(switchMasterMsg[3], switchMasterMsg[4]));
                } else {
                  LOG.debug(
                    "Ignoring message on +switch-master for master {}. Our master is {}.",
                    switchMasterMsg[0], masterName);
                }

              } else {
                LOG.error("Invalid message received on sentinel {} on channel +switch-master: {}.",
                    node, message);
              }
            }
          }, "+switch-master");

        } catch (JedisException e) {

          if (running.get()) {
            LOG.error("Lost connection to sentinel {}. Sleeping {}ms and retrying.", node,
                subscribeRetryWaitTimeMillis, e);
            try {
              Thread.sleep(subscribeRetryWaitTimeMillis);
            } catch (InterruptedException se) {
              LOG.error("Sleep interrupted.", se);
            }
          } else {
            LOG.debug("Unsubscribing from sentinel {}.", node);
          }
        } finally {
          IOUtils.closeQuietly(sentinelJedis);
        }
      }
    }

    // must not throw exception
    public void shutdown() {
      try {
        LOG.debug("Shutting down listener on {}.", node);
        running.set(false);
        // This isn't good, the Jedis object is not thread safe
        if (sentinelJedis != null) {
          sentinelJedis.close();
        }
      } catch (RuntimeException e) {
        LOG.error("Error while shutting down.", e);
      }
    }
  }
}
