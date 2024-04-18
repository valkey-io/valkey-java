package io.jackey.commands;

import io.jackey.Response;
import io.jackey.args.BitCountOption;
import io.jackey.args.BitOP;
import io.jackey.params.BitPosParams;

import java.util.List;

public interface BitPipelineCommands {

  Response<Boolean> setbit(String key, long offset, boolean value);

  Response<Boolean> getbit(String key, long offset);

  Response<Long> bitcount(String key);

  Response<Long> bitcount(String key, long start, long end);

  Response<Long> bitcount(String key, long start, long end, BitCountOption option);

  Response<Long> bitpos(String key, boolean value);

  Response<Long> bitpos(String key, boolean value, BitPosParams params);

  Response<List<Long>> bitfield(String key, String...arguments);

  Response<List<Long>> bitfieldReadonly(String key, String...arguments);

  Response<Long> bitop(BitOP op, String destKey, String... srcKeys);
}
