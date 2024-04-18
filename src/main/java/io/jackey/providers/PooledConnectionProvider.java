package io.jackey.providers;

import java.util.Collections;
import java.util.Map;

import io.jackey.CommandArguments;
import io.jackey.Connection;
import io.jackey.ConnectionFactory;
import io.jackey.ConnectionPool;
import io.jackey.HostAndPort;
import io.jackey.JedisClientConfig;
import io.jackey.util.Pool;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class PooledConnectionProvider implements ConnectionProvider {

  private final Pool<Connection> pool;
  private Object connectionMapKey = "";

  public PooledConnectionProvider(HostAndPort hostAndPort) {
    this(new ConnectionFactory(hostAndPort));
    this.connectionMapKey = hostAndPort;
  }

  public PooledConnectionProvider(HostAndPort hostAndPort, JedisClientConfig clientConfig) {
    this(new ConnectionPool(hostAndPort, clientConfig));
    this.connectionMapKey = hostAndPort;
  }

  public PooledConnectionProvider(HostAndPort hostAndPort, JedisClientConfig clientConfig,
      GenericObjectPoolConfig<Connection> poolConfig) {
    this(new ConnectionFactory(hostAndPort, clientConfig), poolConfig);
    this.connectionMapKey = hostAndPort;
  }

  public PooledConnectionProvider(PooledObjectFactory<Connection> factory) {
    this(new ConnectionPool(factory));
    this.connectionMapKey = factory;
  }

  public PooledConnectionProvider(PooledObjectFactory<Connection> factory,
      GenericObjectPoolConfig<Connection> poolConfig) {
    this(new ConnectionPool(factory, poolConfig));
    this.connectionMapKey = factory;
  }

  private PooledConnectionProvider(Pool<Connection> pool) {
    this.pool = pool;
  }

  @Override
  public void close() {
    pool.close();
  }

  public final Pool<Connection> getPool() {
    return pool;
  }

  @Override
  public Connection getConnection() {
    return pool.getResource();
  }

  @Override
  public Connection getConnection(CommandArguments args) {
    return pool.getResource();
  }

  @Override
  public Map<?, Pool<Connection>> getConnectionMap() {
    return Collections.singletonMap(connectionMapKey, pool);
  }
}
