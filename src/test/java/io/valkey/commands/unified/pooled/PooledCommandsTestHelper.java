package io.valkey.commands.unified.pooled;

import io.valkey.DefaultJedisClientConfig;
import io.valkey.HostAndPort;
import io.valkey.HostAndPorts;
import io.valkey.Jedis;
import io.valkey.JedisPooled;
import io.valkey.RedisProtocol;

public class PooledCommandsTestHelper {

  private static final HostAndPort nodeInfo = HostAndPorts.getRedisServers().get(0);

  public static JedisPooled getPooled(RedisProtocol redisProtocol) {
    return new JedisPooled(nodeInfo, DefaultJedisClientConfig.builder()
        .protocol(redisProtocol).password("foobared").build());
  }

  public static void clearData() {
    try (Jedis node = new Jedis(nodeInfo)) {
      node.auth("foobared");
      node.flushAll();
    }
  }
}
