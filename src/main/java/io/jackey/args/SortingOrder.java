package io.jackey.args;

import io.jackey.util.SafeEncoder;

public enum SortingOrder implements Rawable {

  ASC, DESC;

  private final byte[] raw;

  private SortingOrder() {
    raw = SafeEncoder.encode(this.name());
  }

  @Override
  public byte[] getRaw() {
    return raw;
  }
}
