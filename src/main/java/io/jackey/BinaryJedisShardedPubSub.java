package io.jackey;

public abstract class BinaryJedisShardedPubSub extends JedisShardedPubSubBase<byte[]> {

  @Override
  protected final byte[] encode(byte[] raw) {
    return raw;
  }
}
