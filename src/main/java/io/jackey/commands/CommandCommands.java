package io.jackey.commands;

import io.jackey.params.CommandListFilterByParams;
import io.jackey.resps.CommandDocument;
import io.jackey.resps.CommandInfo;
import io.jackey.util.KeyValue;

import java.util.List;
import java.util.Map;

public interface CommandCommands {

  /**
   * The number of total commands in this Redis server
   * @return The number of total commands
   */
  long commandCount();

  /**
   * Return documentary information about commands.
   * If not specifying commands, the reply includes all the server's commands.
   * @param commands specify the names of one or more commands
   * @return list of {@link CommandDocument}
   */

  Map<String, CommandDocument> commandDocs(String... commands);

  /**
   * Return list of keys from a full Redis command
   * @param command
   * @return list of keys
   */
  List<String> commandGetKeys(String... command);

  /**
   * Return list of keys from a full Redis command and their usage flags
   * @param command
   * @return list of {@link KeyValue}
   */
  List<KeyValue<String, List<String>>> commandGetKeysAndFlags(String... command);

  /**
   * Return details about multiple Redis commands
   * @param commands
   * @return list of {@link CommandInfo}
   */
  Map<String, CommandInfo> commandInfo(String... commands);

  /**
   * Return a list of the server's command names
   * @return commands list
   */
  List<String> commandList();

  /**
   * Return a list of the server's command names filtered by module's name, ACL category or pattern
   * @param filterByParams {@link CommandListFilterByParams}
   * @return commands list
   */
  List<String> commandListFilterBy(CommandListFilterByParams filterByParams);
}
