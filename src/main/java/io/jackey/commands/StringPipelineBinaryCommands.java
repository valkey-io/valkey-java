package io.jackey.commands;

import java.util.List;

import io.jackey.Response;
import io.jackey.params.GetExParams;
import io.jackey.params.LCSParams;
import io.jackey.params.SetParams;
import io.jackey.resps.LCSMatchResult;

public interface StringPipelineBinaryCommands extends BitPipelineBinaryCommands {

  Response<String> set(byte[] key, byte[] value);

  Response<String> set(byte[] key, byte[] value, SetParams params);

  Response<byte[]> get(byte[] key);

  Response<byte[]> setGet(byte[] key, byte[] value);

  Response<byte[]> setGet(byte[] key, byte[] value, SetParams params);

  Response<byte[]> getDel(byte[] key);

  Response<byte[]> getEx(byte[] key, GetExParams params);

  Response<Long> setrange(byte[] key, long offset, byte[] value);

  Response<byte[]> getrange(byte[] key, long startOffset, long endOffset);

  /**
   * @deprecated {@link StringPipelineBinaryCommands#setGet(byte[], byte[], SetParams)}.
   */
  @Deprecated
  Response<byte[]> getSet(byte[] key, byte[] value);

  Response<Long> setnx(byte[] key, byte[] value);

  Response<String> setex(byte[] key, long seconds, byte[] value);

  Response<String> psetex(byte[] key, long milliseconds, byte[] value);

  Response<List<byte[]>> mget(byte[]... keys);

  Response<String> mset(byte[]... keysvalues);

  Response<Long> msetnx(byte[]... keysvalues);

  Response<Long> incr(byte[] key);

  Response<Long> incrBy(byte[] key, long increment);

  Response<Double> incrByFloat(byte[] key, double increment);

  Response<Long> decr(byte[] key);

  Response<Long> decrBy(byte[] key, long decrement);

  Response<Long> append(byte[] key, byte[] value);

  Response<byte[]> substr(byte[] key, int start, int end);

  Response<Long> strlen(byte[] key);

  Response<LCSMatchResult> lcs(byte[] keyA, byte[] keyB, LCSParams params);
}
