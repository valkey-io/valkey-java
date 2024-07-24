package io.valkey.executors;

import io.valkey.CommandObject;
import io.valkey.Connection;
import io.valkey.util.IOUtils;
import io.valkey.providers.ConnectionProvider;

public class DefaultCommandExecutor implements CommandExecutor {

  protected final ConnectionProvider provider;

  public DefaultCommandExecutor(ConnectionProvider provider) {
    this.provider = provider;
  }

  @Override
  public void close() {
    IOUtils.closeQuietly(this.provider);
  }

  @Override
  public final <T> T executeCommand(CommandObject<T> commandObject) {
    try (Connection connection = provider.getConnection(commandObject.getArguments())) {
      return connection.executeCommand(commandObject);
    }
  }
}
