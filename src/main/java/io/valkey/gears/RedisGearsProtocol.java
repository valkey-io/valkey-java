package io.valkey.gears;

import io.valkey.args.Rawable;
import io.valkey.util.SafeEncoder;
import io.valkey.commands.ProtocolCommand;

public class RedisGearsProtocol {

  public enum GearsCommand implements ProtocolCommand {

    TFUNCTION,
    TFCALL,
    TFCALLASYNC;

    private final byte[] raw;

    private GearsCommand() {
      this.raw = SafeEncoder.encode(name());
    }

    @Override
    public byte[] getRaw() {
      return raw;
    }
  }

  public enum GearsKeyword implements Rawable {

    CONFIG,
    REPLACE,
    LOAD,
    DELETE,
    LIST,
    WITHCODE,
    LIBRARY,
    VERBOSE;

    private final byte[] raw;

    private GearsKeyword() {
      this.raw = SafeEncoder.encode(name());
    }

    @Override
    public byte[] getRaw() {
      return raw;
    }
  }
}
