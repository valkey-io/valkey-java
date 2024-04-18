package io.jackey.commands;

import java.util.List;

import io.jackey.Response;
import io.jackey.args.BitCountOption;
import io.jackey.args.BitOP;
import io.jackey.params.BitPosParams;

public interface BitPipelineBinaryCommands {

  Response<Boolean> setbit(byte[] key, long offset, boolean value);

  Response<Boolean> getbit(byte[] key, long offset);

  Response<Long> bitcount(byte[] key);

  Response<Long> bitcount(byte[] key, long start, long end);

  Response<Long> bitcount(byte[] key, long start, long end, BitCountOption option);

  Response<Long> bitpos(byte[] key, boolean value);

  Response<Long> bitpos(byte[] key, boolean value, BitPosParams params);

  Response<List<Long>> bitfield(byte[] key, byte[]... arguments);

  Response<List<Long>> bitfieldReadonly(byte[] key, byte[]... arguments);

  Response<Long> bitop(BitOP op, byte[] destKey, byte[]... srcKeys);
}
