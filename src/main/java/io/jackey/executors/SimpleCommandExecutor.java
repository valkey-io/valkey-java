package io.jackey.executors;

import io.jackey.CommandObject;
import io.jackey.Connection;
import io.jackey.util.IOUtils;

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
