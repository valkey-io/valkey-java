package io.jackey;

import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import io.jackey.providers.ShardedConnectionProvider;
import io.jackey.util.Hashing;
import io.jackey.util.IOUtils;

/**
 * WARNING: RESP3 is not properly implemented for ShardedPipeline.
 *
 * @deprecated Sharding/Sharded feature will be removed in next major release.
 */
@Deprecated
public class ShardedPipeline extends MultiNodePipelineBase {

  private final ShardedConnectionProvider provider;
  private AutoCloseable closeable = null;

  public ShardedPipeline(List<HostAndPort> shards, JedisClientConfig clientConfig) {
    this(new ShardedConnectionProvider(shards, clientConfig));
    this.closeable = this.provider;
  }

  public ShardedPipeline(ShardedConnectionProvider provider) {
    super(new ShardedCommandObjects(provider.getHashingAlgo()));
    this.provider = provider;
  }

  public ShardedPipeline(List<HostAndPort> shards, JedisClientConfig clientConfig,
      GenericObjectPoolConfig<Connection> poolConfig, Hashing algo, Pattern tagPattern) {
    this(new ShardedConnectionProvider(shards, clientConfig, poolConfig, algo), tagPattern);
    this.closeable = this.provider;
  }

  public ShardedPipeline(ShardedConnectionProvider provider, Pattern tagPattern) {
    super(new ShardedCommandObjects(provider.getHashingAlgo(), tagPattern));
    this.provider = provider;
  }

  @Override
  public void close() {
    try {
      super.close();
    } finally {
      IOUtils.closeQuietly(closeable);
    }
  }

  @Override
  protected HostAndPort getNodeKey(CommandArguments args) {
    return provider.getNode(((ShardedCommandArguments) args).getKeyHash());
  }

  @Override
  protected Connection getConnection(HostAndPort nodeKey) {
    return provider.getConnection(nodeKey);
  }

  /**
   * This method must be called after constructor, if graph commands are going to be used.
   */
  public void prepareGraphCommands() {
    super.prepareGraphCommands(provider);
  }
}
