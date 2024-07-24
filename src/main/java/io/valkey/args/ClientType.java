package io.valkey.args;

import io.valkey.util.SafeEncoder;

public enum ClientType implements Rawable {

  NORMAL, MASTER, SLAVE, REPLICA, PUBSUB;

  private final byte[] raw;

  private ClientType() {
    raw = SafeEncoder.encode(name().toLowerCase());
  }

  @Override
  public byte[] getRaw() {
    return raw;
  }
}
