package io.jackey;

import java.util.ArrayList;
import java.util.List;

public final class HostAndPorts {

  private static List<HostAndPort> redisHostAndPortList = new ArrayList<>();
  private static List<HostAndPort> sentinelHostAndPortList = new ArrayList<>();
  private static List<HostAndPort> clusterHostAndPortList = new ArrayList<>();
  private static List<HostAndPort> stableClusterHostAndPortList = new ArrayList<>();

  static {
    redisHostAndPortList.add(new HostAndPort("localhost", 6379));
    redisHostAndPortList.add(new HostAndPort("localhost", 6380));
    redisHostAndPortList.add(new HostAndPort("localhost", 6381));
    redisHostAndPortList.add(new HostAndPort("localhost", 6382));
    redisHostAndPortList.add(new HostAndPort("localhost", 6383));
    redisHostAndPortList.add(new HostAndPort("localhost", 6384));
    redisHostAndPortList.add(new HostAndPort("localhost", 6385));
    redisHostAndPortList.add(new HostAndPort("localhost", 6386));
    redisHostAndPortList.add(new HostAndPort("localhost", 6387));
    redisHostAndPortList.add(new HostAndPort("localhost", 6388));
    redisHostAndPortList.add(new HostAndPort("localhost", 6389));
    redisHostAndPortList.add(new HostAndPort("localhost", 6391));
    redisHostAndPortList.add(new HostAndPort("localhost", 6392));

    sentinelHostAndPortList.add(new HostAndPort("localhost", 26379));
    sentinelHostAndPortList.add(new HostAndPort("localhost", 26380));
    sentinelHostAndPortList.add(new HostAndPort("localhost", 26381));
    sentinelHostAndPortList.add(new HostAndPort("localhost", 26382));
    sentinelHostAndPortList.add(new HostAndPort("localhost", 26383));

    clusterHostAndPortList.add(new HostAndPort("localhost", 7379));
    clusterHostAndPortList.add(new HostAndPort("localhost", 7380));
    clusterHostAndPortList.add(new HostAndPort("localhost", 7381));
    clusterHostAndPortList.add(new HostAndPort("localhost", 7382));
    clusterHostAndPortList.add(new HostAndPort("localhost", 7383));
    clusterHostAndPortList.add(new HostAndPort("localhost", 7384));

    stableClusterHostAndPortList.add(new HostAndPort("localhost", 7479));
    stableClusterHostAndPortList.add(new HostAndPort("localhost", 7480));
    stableClusterHostAndPortList.add(new HostAndPort("localhost", 7481));
  }

  public static List<HostAndPort> parseHosts(String envHosts,
      List<HostAndPort> existingHostsAndPorts) {

    if (null != envHosts && 0 < envHosts.length()) {

      String[] hostDefs = envHosts.split(",");

      if (null != hostDefs && 2 <= hostDefs.length) {

        List<HostAndPort> envHostsAndPorts = new ArrayList<>(hostDefs.length);

        for (String hostDef : hostDefs) {

          envHostsAndPorts.add(HostAndPort.from(hostDef));
        }

        return envHostsAndPorts;
      }
    }

    return existingHostsAndPorts;
  }

  public static List<HostAndPort> getRedisServers() {
    return redisHostAndPortList;
  }

  public static List<HostAndPort> getSentinelServers() {
    return sentinelHostAndPortList;
  }

  public static List<HostAndPort> getClusterServers() {
    return clusterHostAndPortList;
  }

  public static List<HostAndPort> getStableClusterServers() {
    return stableClusterHostAndPortList;
  }

  private HostAndPorts() {
    throw new InstantiationError("Must not instantiate this class");
  }
}
