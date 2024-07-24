package io.valkey.commands;

import io.valkey.Response;

public interface HyperLogLogPipelineBinaryCommands {

  Response<Long> pfadd(byte[] key, byte[]... elements);

  Response<String> pfmerge(byte[] destkey, byte[]... sourcekeys);

  Response<Long> pfcount(byte[] key);

  Response<Long> pfcount(byte[]... keys);

}
