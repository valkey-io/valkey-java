package io.jackey.args;

import io.jackey.util.SafeEncoder;

/**
 * Bit operations for {@code BITOP} command.
 */
public enum BitOP implements Rawable {

  AND, OR, XOR, NOT;

  private final byte[] raw;

  private BitOP() {
    raw = SafeEncoder.encode(name());
  }

  @Override
  public byte[] getRaw() {
    return raw;
  }
}
