package io.jackey.commands;

import io.jackey.Response;

public interface HyperLogLogPipelineCommands {

  Response<Long> pfadd(String key, String... elements);

  Response<String> pfmerge(String destkey, String... sourcekeys);

  Response<Long> pfcount(String key);

  Response<Long> pfcount(String... keys);

}
