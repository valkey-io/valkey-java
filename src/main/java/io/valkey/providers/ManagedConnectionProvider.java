package io.valkey.providers;

import io.valkey.CommandArguments;
import io.valkey.Connection;

public class ManagedConnectionProvider implements ConnectionProvider {

  private Connection connection;

  public final void setConnection(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void close() {
  }

  @Override
  public final Connection getConnection() {
    return connection;
  }

  @Override
  public final Connection getConnection(CommandArguments args) {
    return connection;
  }
}
