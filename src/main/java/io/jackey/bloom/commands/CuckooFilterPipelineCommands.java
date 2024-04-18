package io.jackey.bloom.commands;

import java.util.List;
import java.util.Map;

import io.jackey.Response;
import io.jackey.bloom.CFInsertParams;
import io.jackey.bloom.CFReserveParams;

public interface CuckooFilterPipelineCommands {

  Response<String> cfReserve(String key, long capacity);

  Response<String> cfReserve(String key, long capacity, CFReserveParams reserveParams);

  Response<Boolean> cfAdd(String key, String item);

  Response<Boolean> cfAddNx(String key, String item);

  Response<List<Boolean>> cfInsert(String key, String... items);

  Response<List<Boolean>> cfInsert(String key, CFInsertParams insertParams, String... items);

  Response<List<Boolean>> cfInsertNx(String key, String... items);

  Response<List<Boolean>> cfInsertNx(String key, CFInsertParams insertParams, String... items);

  Response<Boolean> cfExists(String key, String item);

  Response<List<Boolean>> cfMExists(String key, String... items);

  Response<Boolean> cfDel(String key, String item);

  Response<Long> cfCount(String key, String item);

  Response<Map.Entry<Long, byte[]>> cfScanDump(String key, long iterator);

  Response<String> cfLoadChunk(String key, long iterator, byte[] data);

  Response<Map<String, Object>> cfInfo(String key);
}
