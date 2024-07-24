package io.valkey.commands.commandobjects;

import io.valkey.HostAndPorts;
import io.valkey.RedisProtocol;

/**
 * Base class for tests that use the standalone client.
 */
public abstract class CommandObjectsStandaloneTestBase extends CommandObjectsTestBase {

  public CommandObjectsStandaloneTestBase(RedisProtocol protocol) {
    super(protocol, HostAndPorts.getRedisServers().get(0), "foobared");
  }

}
