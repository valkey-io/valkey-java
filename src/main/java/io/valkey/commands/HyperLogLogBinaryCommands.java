package io.valkey.commands;

public interface HyperLogLogBinaryCommands {

  long pfadd(byte[] key, byte[]... elements);

  String pfmerge(byte[] destkey, byte[]... sourcekeys);

  long pfcount(byte[] key);

  long pfcount(byte[]... keys);

}
