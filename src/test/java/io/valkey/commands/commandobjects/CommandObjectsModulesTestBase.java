package io.valkey.commands.commandobjects;

import io.valkey.HostAndPort;
import io.valkey.Protocol;
import io.valkey.RedisProtocol;

/**
 * Base class for tests that need a Redis Stack server.
 */
public abstract class CommandObjectsModulesTestBase extends CommandObjectsTestBase {

  private static final String address =
      System.getProperty("modulesDocker", Protocol.DEFAULT_HOST + ':' + 6479);

  public CommandObjectsModulesTestBase(RedisProtocol protocol) {
    super(protocol, HostAndPort.from(address), null);
  }

}
