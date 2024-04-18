package io.jackey;

import io.jackey.util.SafeEncoder;

public abstract class JedisShardedPubSub extends JedisShardedPubSubBase<String> {

  @Override
  protected final String encode(byte[] raw) {
    return SafeEncoder.encode(raw);
  }
}
