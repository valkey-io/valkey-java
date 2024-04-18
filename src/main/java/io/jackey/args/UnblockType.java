package io.jackey.args;

import io.jackey.util.SafeEncoder;

/**
 * Unblock type for {@code CLIENT UNBLOCK} command.
 */
public enum UnblockType implements Rawable {

  TIMEOUT, ERROR;

  private final byte[] raw;

  private UnblockType() {
    raw = SafeEncoder.encode(this.name());
  }

  @Override
  public byte[] getRaw() {
    return raw;
  }
}
