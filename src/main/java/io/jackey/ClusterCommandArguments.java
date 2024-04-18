package io.jackey;

import io.jackey.commands.ProtocolCommand;
import io.jackey.exceptions.JedisClusterOperationException;
import io.jackey.util.JedisClusterCRC16;

public class ClusterCommandArguments extends CommandArguments {

  private int commandHashSlot = -1;

  public ClusterCommandArguments(ProtocolCommand command) {
    super(command);
  }

  public int getCommandHashSlot() {
    return commandHashSlot;
  }

  @Override
  protected CommandArguments processKey(byte[] key) {
    final int hashSlot = JedisClusterCRC16.getSlot(key);
    if (commandHashSlot < 0) {
      commandHashSlot = hashSlot;
    } else if (commandHashSlot != hashSlot) {
      throw new JedisClusterOperationException("Keys must belong to same hashslot.");
    }
    return this;
  }

  @Override
  protected CommandArguments processKey(String key) {
    final int hashSlot = JedisClusterCRC16.getSlot(key);
    if (commandHashSlot < 0) {
      commandHashSlot = hashSlot;
    } else if (commandHashSlot != hashSlot) {
      throw new JedisClusterOperationException("Keys must belong to same hashslot.");
    }
    return this;
  }
}
