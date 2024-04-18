package io.jackey;

import io.jackey.util.SafeEncoder;

public abstract class JedisPubSub extends JedisPubSubBase<String> {

  @Override
  protected final String encode(byte[] raw) {
    return SafeEncoder.encode(raw);
  }
}
