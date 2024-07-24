package io.valkey.util;

import io.valkey.args.Rawable;

public class LazyRawable implements Rawable {

  private byte[] raw = null;

  public void setRaw(byte[] raw) {
    this.raw = raw;
  }

  @Override
  public byte[] getRaw() {
    return raw;
  }
}
