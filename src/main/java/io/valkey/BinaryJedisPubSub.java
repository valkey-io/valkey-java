package io.valkey;

public abstract class BinaryJedisPubSub extends JedisPubSubBase<byte[]> {

  @Override
  protected final byte[] encode(byte[] raw) {
    return raw;
  }
}
