package io.valkey.executors;

import io.valkey.CommandObject;

public interface CommandExecutor extends AutoCloseable {

  <T> T executeCommand(CommandObject<T> commandObject);

  default <T> T broadcastCommand(CommandObject<T> commandObject) {
    return executeCommand(commandObject);
  }
}
