package io.valkey.executors;

import io.valkey.CommandObject;
import io.valkey.Connection;
import io.valkey.util.IOUtils;

public class SimpleCommandExecutor implements CommandExecutor {

  protected final Connection connection;

  public SimpleCommandExecutor(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void close() {
    IOUtils.closeQuietly(connection);
  }

  @Override
  public final <T> T executeCommand(CommandObject<T> commandObject) {
    return connection.executeCommand(commandObject);
  }
}
