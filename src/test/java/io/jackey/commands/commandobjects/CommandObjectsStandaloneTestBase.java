package io.jackey.commands.commandobjects;

import io.jackey.HostAndPorts;
import io.jackey.RedisProtocol;

/**
 * Base class for tests that use the standalone client.
 */
public abstract class CommandObjectsStandaloneTestBase extends CommandObjectsTestBase {

  public CommandObjectsStandaloneTestBase(RedisProtocol protocol) {
    super(protocol, HostAndPorts.getRedisServers().get(0), "foobared");
  }

}
