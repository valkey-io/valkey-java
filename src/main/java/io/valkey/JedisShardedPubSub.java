package io.valkey;

import io.valkey.util.SafeEncoder;

public abstract class JedisShardedPubSub extends JedisShardedPubSubBase<String> {

  @Override
  protected final String encode(byte[] raw) {
    return SafeEncoder.encode(raw);
  }
}
