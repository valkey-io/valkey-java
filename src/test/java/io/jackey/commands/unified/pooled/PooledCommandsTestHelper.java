package io.jackey.commands.unified.pooled;

import io.jackey.DefaultJedisClientConfig;
import io.jackey.HostAndPort;
import io.jackey.HostAndPorts;
import io.jackey.Jedis;
import io.jackey.JedisPooled;
import io.jackey.RedisProtocol;

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
