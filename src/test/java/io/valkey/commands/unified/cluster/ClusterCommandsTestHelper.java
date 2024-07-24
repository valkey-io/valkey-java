package io.valkey.commands.unified.cluster;

import java.util.Collections;

import io.valkey.DefaultJedisClientConfig;
import io.valkey.HostAndPorts;
import io.valkey.Jedis;
import io.valkey.JedisCluster;
import io.valkey.RedisProtocol;

public class ClusterCommandsTestHelper {

  static JedisCluster getCleanCluster(RedisProtocol protocol) {
    clearClusterData();
    return new JedisCluster(
        Collections.singleton(HostAndPorts.getStableClusterServers().get(0)),
        DefaultJedisClientConfig.builder().password("cluster").protocol(protocol).build());
  }

  static void clearClusterData() {
    for (int i = 0; i < 3; i++) {
      try (Jedis jedis = new Jedis(HostAndPorts.getStableClusterServers().get(i))) {
        jedis.auth("cluster");
        jedis.flushAll();
      }
    }
  }
}
