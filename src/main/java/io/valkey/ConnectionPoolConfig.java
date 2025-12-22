package io.valkey;

import java.time.Duration;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class ConnectionPoolConfig extends GenericObjectPoolConfig<Connection> {

  private static final int DEFAULT_MIN_IDLE = 16;

  public ConnectionPoolConfig() {
    this(DEFAULT_MIN_IDLE);
  }

  public ConnectionPoolConfig(int minIdle) {
    if (minIdle < 1) {
      throw new IllegalArgumentException(
          "minIdle must be at least 1, got: " + minIdle);
    }

    // defaults to make your life with connection pool easier :)
    setTestWhileIdle(true);
    setMinEvictableIdleTime(Duration.ofMillis(60000));
    setTimeBetweenEvictionRuns(Duration.ofMillis(30000));
    setNumTestsPerEvictionRun(-1);

    setMinIdle(minIdle);
    setMaxIdle(2 * minIdle);
    setMaxTotal(2 * minIdle);
  }
}