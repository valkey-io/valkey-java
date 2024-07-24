package io.valkey;

import io.valkey.util.SafeEncoder;

public abstract class JedisPubSub extends JedisPubSubBase<String> {

  @Override
  protected final String encode(byte[] raw) {
    return SafeEncoder.encode(raw);
  }
}
