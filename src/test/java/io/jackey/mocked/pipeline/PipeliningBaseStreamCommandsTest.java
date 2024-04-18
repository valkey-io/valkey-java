package io.jackey.mocked.pipeline;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.jackey.Response;
import io.jackey.StreamEntryID;
import io.jackey.params.XAddParams;
import io.jackey.params.XAutoClaimParams;
import io.jackey.params.XClaimParams;
import io.jackey.params.XPendingParams;
import io.jackey.params.XReadGroupParams;
import io.jackey.params.XReadParams;
import io.jackey.params.XTrimParams;
import io.jackey.resps.StreamConsumerInfo;
import io.jackey.resps.StreamConsumersInfo;
import io.jackey.resps.StreamEntry;
import io.jackey.resps.StreamFullInfo;
import io.jackey.resps.StreamGroupInfo;
import io.jackey.resps.StreamInfo;
import io.jackey.resps.StreamPendingEntry;
import io.jackey.resps.StreamPendingSummary;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public class PipeliningBaseStreamCommandsTest extends PipeliningBaseMockedTestBase {

  @Test
  public void testXack() {
    StreamEntryID[] ids = { new StreamEntryID("1526999352406-0"), new StreamEntryID("1526999352406-1") };

    when(commandObjects.xack("key", "group", ids)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.xack("key", "group", ids);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXackBinary() {
    byte[] key = "stream".getBytes();
    byte[] group = "group".getBytes();
    byte[] id1 = "id1".getBytes();
    byte[] id2 = "id2".getBytes();

    when(commandObjects.xack(key, group, id1, id2)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.xack(key, group, id1, id2);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXadd() {
    StreamEntryID id = new StreamEntryID();

    Map<String, String> hash = new HashMap<>();
    hash.put("field1", "value1");

    when(commandObjects.xadd("key", id, hash)).thenReturn(streamEntryIdCommandObject);

    Response<StreamEntryID> response = pipeliningBase.xadd("key", id, hash);

    MatcherAssert.assertThat(commands, Matchers.contains(streamEntryIdCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXaddBinary() {
    byte[] key = "stream".getBytes();
    XAddParams params = new XAddParams();

    Map<byte[], byte[]> hash = new HashMap<>();
    hash.put("field1".getBytes(), "value1".getBytes());

    when(commandObjects.xadd(key, params, hash)).thenReturn(bytesCommandObject);

    Response<byte[]> response = pipeliningBase.xadd(key, params, hash);

    MatcherAssert.assertThat(commands, Matchers.contains(bytesCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXaddWithParams() {
    XAddParams params = new XAddParams();

    Map<String, String> hash = new HashMap<>();
    hash.put("field1", "value1");

    when(commandObjects.xadd("key", params, hash)).thenReturn(streamEntryIdCommandObject);

    Response<StreamEntryID> response = pipeliningBase.xadd("key", params, hash);

    MatcherAssert.assertThat(commands, Matchers.contains(streamEntryIdCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXautoclaim() {
    StreamEntryID start = new StreamEntryID("0-0");
    XAutoClaimParams params = new XAutoClaimParams();

    when(commandObjects.xautoclaim("key", "group", "consumerName", 10000L, start, params))
        .thenReturn(entryStreamEntryIdListStreamEntryCommandObject);

    Response<Map.Entry<StreamEntryID, List<StreamEntry>>> response = pipeliningBase
        .xautoclaim("key", "group", "consumerName", 10000L, start, params);

    MatcherAssert.assertThat(commands, Matchers.contains(entryStreamEntryIdListStreamEntryCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXautoclaimBinary() {
    byte[] key = "stream".getBytes();
    byte[] groupName = "group".getBytes();
    byte[] consumerName = "consumer".getBytes();
    long minIdleTime = 10000L;
    byte[] start = "startId".getBytes();
    XAutoClaimParams params = new XAutoClaimParams();

    when(commandObjects.xautoclaim(key, groupName, consumerName, minIdleTime, start, params)).thenReturn(listObjectCommandObject);

    Response<List<Object>> response = pipeliningBase.xautoclaim(key, groupName, consumerName, minIdleTime, start, params);

    MatcherAssert.assertThat(commands, Matchers.contains(listObjectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXautoclaimJustId() {
    StreamEntryID start = new StreamEntryID("0-0");
    XAutoClaimParams params = new XAutoClaimParams();

    when(commandObjects.xautoclaimJustId("key", "group", "consumerName", 10000L, start, params))
        .thenReturn(entryStreamEntryIdListStreamEntryIdCommandObject);

    Response<Map.Entry<StreamEntryID, List<StreamEntryID>>> response = pipeliningBase
        .xautoclaimJustId("key", "group", "consumerName", 10000L, start, params);

    MatcherAssert.assertThat(commands, Matchers.contains(entryStreamEntryIdListStreamEntryIdCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXautoclaimJustIdBinary() {
    byte[] key = "stream".getBytes();
    byte[] groupName = "group".getBytes();
    byte[] consumerName = "consumer".getBytes();
    long minIdleTime = 10000L;
    byte[] start = "startId".getBytes();
    XAutoClaimParams params = new XAutoClaimParams();

    when(commandObjects.xautoclaimJustId(key, groupName, consumerName, minIdleTime, start, params)).thenReturn(listObjectCommandObject);

    Response<List<Object>> response = pipeliningBase.xautoclaimJustId(key, groupName, consumerName, minIdleTime, start, params);

    MatcherAssert.assertThat(commands, Matchers.contains(listObjectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXclaim() {
    StreamEntryID[] ids = { new StreamEntryID("1526999352406-0"), new StreamEntryID("1526999352406-1") };
    XClaimParams params = new XClaimParams().idle(10000L);

    when(commandObjects.xclaim("key", "group", "consumerName", 10000L, params, ids))
        .thenReturn(listStreamEntryCommandObject);

    Response<List<StreamEntry>> response = pipeliningBase
        .xclaim("key", "group", "consumerName", 10000L, params, ids);

    MatcherAssert.assertThat(commands, Matchers.contains(listStreamEntryCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXclaimBinary() {
    byte[] key = "stream".getBytes();
    byte[] group = "group".getBytes();
    byte[] consumerName = "consumer".getBytes();
    long minIdleTime = 10000L;
    XClaimParams params = new XClaimParams();
    byte[] id1 = "id1".getBytes();
    byte[] id2 = "id2".getBytes();

    when(commandObjects.xclaim(key, group, consumerName, minIdleTime, params, id1, id2)).thenReturn(listBytesCommandObject);

    Response<List<byte[]>> response = pipeliningBase.xclaim(key, group, consumerName, minIdleTime, params, id1, id2);

    MatcherAssert.assertThat(commands, Matchers.contains(listBytesCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXclaimJustId() {
    StreamEntryID[] ids = { new StreamEntryID("1526999352406-0"), new StreamEntryID("1526999352406-1") };
    XClaimParams params = new XClaimParams().idle(10000L);

    when(commandObjects.xclaimJustId("key", "group", "consumerName", 10000L, params, ids))
        .thenReturn(listStreamEntryIdCommandObject);

    Response<List<StreamEntryID>> response = pipeliningBase
        .xclaimJustId("key", "group", "consumerName", 10000L, params, ids);

    MatcherAssert.assertThat(commands, Matchers.contains(listStreamEntryIdCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXclaimJustIdBinary() {
    byte[] key = "stream".getBytes();
    byte[] group = "group".getBytes();
    byte[] consumerName = "consumer".getBytes();
    long minIdleTime = 10000L;
    XClaimParams params = new XClaimParams();
    byte[] id1 = "id1".getBytes();
    byte[] id2 = "id2".getBytes();

    when(commandObjects.xclaimJustId(key, group, consumerName, minIdleTime, params, id1, id2)).thenReturn(listBytesCommandObject);

    Response<List<byte[]>> response = pipeliningBase.xclaimJustId(key, group, consumerName, minIdleTime, params, id1, id2);

    MatcherAssert.assertThat(commands, Matchers.contains(listBytesCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXdel() {
    StreamEntryID[] ids = { new StreamEntryID("1526999352406-0"), new StreamEntryID("1526999352406-1") };

    when(commandObjects.xdel("key", ids)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.xdel("key", ids);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXdelBinary() {
    byte[] key = "stream".getBytes();
    byte[] id1 = "id1".getBytes();
    byte[] id2 = "id2".getBytes();

    when(commandObjects.xdel(key, id1, id2)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.xdel(key, id1, id2);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXgroupCreate() {
    StreamEntryID id = new StreamEntryID("0-0");

    when(commandObjects.xgroupCreate("key", "groupName", id, true)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.xgroupCreate("key", "groupName", id, true);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXgroupCreateBinary() {
    byte[] key = "stream".getBytes();
    byte[] groupName = "group".getBytes();
    byte[] id = "id".getBytes();
    boolean makeStream = true;

    when(commandObjects.xgroupCreate(key, groupName, id, makeStream)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.xgroupCreate(key, groupName, id, makeStream);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXgroupCreateConsumer() {
    when(commandObjects.xgroupCreateConsumer("key", "groupName", "consumerName"))
        .thenReturn(booleanCommandObject);

    Response<Boolean> response = pipeliningBase.xgroupCreateConsumer("key", "groupName", "consumerName");

    MatcherAssert.assertThat(commands, Matchers.contains(booleanCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXgroupCreateConsumerBinary() {
    byte[] key = "stream".getBytes();
    byte[] groupName = "group".getBytes();
    byte[] consumerName = "consumer".getBytes();

    when(commandObjects.xgroupCreateConsumer(key, groupName, consumerName)).thenReturn(booleanCommandObject);

    Response<Boolean> response = pipeliningBase.xgroupCreateConsumer(key, groupName, consumerName);

    MatcherAssert.assertThat(commands, Matchers.contains(booleanCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXgroupDelConsumer() {
    when(commandObjects.xgroupDelConsumer("key", "groupName", "consumerName"))
        .thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.xgroupDelConsumer("key", "groupName", "consumerName");

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXgroupDelConsumerBinary() {
    byte[] key = "stream".getBytes();
    byte[] groupName = "group".getBytes();
    byte[] consumerName = "consumer".getBytes();

    when(commandObjects.xgroupDelConsumer(key, groupName, consumerName)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.xgroupDelConsumer(key, groupName, consumerName);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXgroupDestroy() {
    when(commandObjects.xgroupDestroy("key", "groupName")).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.xgroupDestroy("key", "groupName");

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXgroupDestroyBinary() {
    byte[] key = "stream".getBytes();
    byte[] groupName = "group".getBytes();

    when(commandObjects.xgroupDestroy(key, groupName)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.xgroupDestroy(key, groupName);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXgroupSetID() {
    StreamEntryID id = new StreamEntryID("0-0");

    when(commandObjects.xgroupSetID("key", "groupName", id)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.xgroupSetID("key", "groupName", id);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXgroupSetIDBinary() {
    byte[] key = "stream".getBytes();
    byte[] groupName = "group".getBytes();
    byte[] id = "id".getBytes();

    when(commandObjects.xgroupSetID(key, groupName, id)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.xgroupSetID(key, groupName, id);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXinfoConsumers() {
    when(commandObjects.xinfoConsumers("key", "group")).thenReturn(listStreamConsumersInfoCommandObject);

    Response<List<StreamConsumersInfo>> response = pipeliningBase.xinfoConsumers("key", "group");

    MatcherAssert.assertThat(commands, Matchers.contains(listStreamConsumersInfoCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXinfoConsumersBinary() {
    byte[] key = "stream".getBytes();
    byte[] group = "group".getBytes();

    when(commandObjects.xinfoConsumers(key, group)).thenReturn(listObjectCommandObject);

    Response<List<Object>> response = pipeliningBase.xinfoConsumers(key, group);

    MatcherAssert.assertThat(commands, Matchers.contains(listObjectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXinfoConsumers2() {
    when(commandObjects.xinfoConsumers2("key", "group")).thenReturn(listStreamConsumerInfoCommandObject);

    Response<List<StreamConsumerInfo>> response = pipeliningBase.xinfoConsumers2("key", "group");

    MatcherAssert.assertThat(commands, Matchers.contains(listStreamConsumerInfoCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXinfoGroups() {
    when(commandObjects.xinfoGroups("key")).thenReturn(listStreamGroupInfoCommandObject);

    Response<List<StreamGroupInfo>> response = pipeliningBase.xinfoGroups("key");

    MatcherAssert.assertThat(commands, Matchers.contains(listStreamGroupInfoCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXinfoGroupsBinary() {
    byte[] key = "stream".getBytes();

    when(commandObjects.xinfoGroups(key)).thenReturn(listObjectCommandObject);

    Response<List<Object>> response = pipeliningBase.xinfoGroups(key);

    MatcherAssert.assertThat(commands, Matchers.contains(listObjectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXinfoStream() {
    when(commandObjects.xinfoStream("key")).thenReturn(streamInfoCommandObject);

    Response<StreamInfo> response = pipeliningBase.xinfoStream("key");

    MatcherAssert.assertThat(commands, Matchers.contains(streamInfoCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXinfoStreamBinary() {
    byte[] key = "stream".getBytes();

    when(commandObjects.xinfoStream(key)).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.xinfoStream(key);

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXinfoStreamFull() {
    when(commandObjects.xinfoStreamFull("key")).thenReturn(streamFullInfoCommandObject);

    Response<StreamFullInfo> response = pipeliningBase.xinfoStreamFull("key");

    MatcherAssert.assertThat(commands, Matchers.contains(streamFullInfoCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXinfoStreamFullBinary() {
    byte[] key = "stream".getBytes();

    when(commandObjects.xinfoStreamFull(key)).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.xinfoStreamFull(key);

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXinfoStreamFullWithCount() {
    int count = 10;
    when(commandObjects.xinfoStreamFull("key", count)).thenReturn(streamFullInfoCommandObject);

    Response<StreamFullInfo> response = pipeliningBase.xinfoStreamFull("key", count);

    MatcherAssert.assertThat(commands, Matchers.contains(streamFullInfoCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXinfoStreamFullWithCountBinary() {
    byte[] key = "stream".getBytes();
    int count = 10;

    when(commandObjects.xinfoStreamFull(key, count)).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.xinfoStreamFull(key, count);

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXlen() {
    when(commandObjects.xlen("key")).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.xlen("key");

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXlenBinary() {
    byte[] key = "stream".getBytes();

    when(commandObjects.xlen(key)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.xlen(key);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXpending() {
    when(commandObjects.xpending("key", "groupName")).thenReturn(streamPendingSummaryCommandObject);

    Response<StreamPendingSummary> response = pipeliningBase.xpending("key", "groupName");

    MatcherAssert.assertThat(commands, Matchers.contains(streamPendingSummaryCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXpendingBinary() {
    byte[] key = "stream".getBytes();
    byte[] groupName = "group".getBytes();

    when(commandObjects.xpending(key, groupName)).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.xpending(key, groupName);

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXpendingWithParams() {
    XPendingParams params = new XPendingParams();

    when(commandObjects.xpending("key", "groupName", params)).thenReturn(listStreamPendingEntryCommandObject);

    Response<List<StreamPendingEntry>> response = pipeliningBase.xpending("key", "groupName", params);

    MatcherAssert.assertThat(commands, Matchers.contains(listStreamPendingEntryCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXpendingWithParamsBinary() {
    byte[] key = "stream".getBytes();
    byte[] groupName = "group".getBytes();
    XPendingParams params = new XPendingParams().count(10);

    when(commandObjects.xpending(key, groupName, params)).thenReturn(listObjectCommandObject);

    Response<List<Object>> response = pipeliningBase.xpending(key, groupName, params);

    MatcherAssert.assertThat(commands, Matchers.contains(listObjectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXrange() {
    String start = "-";
    String end = "+";

    when(commandObjects.xrange("key", start, end)).thenReturn(listStreamEntryCommandObject);

    Response<List<StreamEntry>> response = pipeliningBase.xrange("key", start, end);

    MatcherAssert.assertThat(commands, Matchers.contains(listStreamEntryCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXrangeBinary() {
    byte[] key = "stream".getBytes();
    byte[] start = "startId".getBytes();
    byte[] end = "endId".getBytes();

    when(commandObjects.xrange(key, start, end)).thenReturn(listObjectCommandObject);

    Response<List<Object>> response = pipeliningBase.xrange(key, start, end);

    MatcherAssert.assertThat(commands, Matchers.contains(listObjectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXrangeWithCount() {
    String start = "-";
    String end = "+";
    int count = 10;

    when(commandObjects.xrange("key", start, end, count)).thenReturn(listStreamEntryCommandObject);

    Response<List<StreamEntry>> response = pipeliningBase.xrange("key", start, end, count);

    MatcherAssert.assertThat(commands, Matchers.contains(listStreamEntryCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXrangeWithCountBinary() {
    byte[] key = "stream".getBytes();
    byte[] start = "startId".getBytes();
    byte[] end = "endId".getBytes();
    int count = 10;

    when(commandObjects.xrange(key, start, end, count)).thenReturn(listObjectCommandObject);

    Response<List<Object>> response = pipeliningBase.xrange(key, start, end, count);

    MatcherAssert.assertThat(commands, Matchers.contains(listObjectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXrangeIds() {
    StreamEntryID start = new StreamEntryID("0-0");
    StreamEntryID end = new StreamEntryID("9999999999999-0");

    when(commandObjects.xrange("key", start, end)).thenReturn(listStreamEntryCommandObject);

    Response<List<StreamEntry>> response = pipeliningBase.xrange("key", start, end);

    MatcherAssert.assertThat(commands, Matchers.contains(listStreamEntryCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXrangeIdsWithCount() {
    StreamEntryID start = new StreamEntryID("0-0");
    StreamEntryID end = new StreamEntryID("9999999999999-0");
    int count = 10;

    when(commandObjects.xrange("key", start, end, count)).thenReturn(listStreamEntryCommandObject);

    Response<List<StreamEntry>> response = pipeliningBase.xrange("key", start, end, count);

    MatcherAssert.assertThat(commands, Matchers.contains(listStreamEntryCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXread() {
    XReadParams xReadParams = new XReadParams();

    Map<String, StreamEntryID> streams = new HashMap<>();
    streams.put("key1", new StreamEntryID("0-0"));
    streams.put("key2", new StreamEntryID("0-0"));

    when(commandObjects.xread(xReadParams, streams)).thenReturn(listEntryStringListStreamEntryCommandObject);

    Response<List<Map.Entry<String, List<StreamEntry>>>> response = pipeliningBase.xread(xReadParams, streams);

    MatcherAssert.assertThat(commands, Matchers.contains(listEntryStringListStreamEntryCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXreadBinary() {
    XReadParams xReadParams = new XReadParams();
    Map.Entry<byte[], byte[]> stream1 = new AbstractMap.SimpleImmutableEntry<>("stream1".getBytes(), "id1".getBytes());

    when(commandObjects.xread(xReadParams, stream1)).thenReturn(listObjectCommandObject);

    Response<List<Object>> response = pipeliningBase.xread(xReadParams, stream1);

    MatcherAssert.assertThat(commands, Matchers.contains(listObjectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXreadGroup() {
    XReadGroupParams xReadGroupParams = new XReadGroupParams();

    Map<String, StreamEntryID> streams = new HashMap<>();
    streams.put("stream1", new StreamEntryID("0-0"));

    when(commandObjects.xreadGroup("groupName", "consumer", xReadGroupParams, streams))
        .thenReturn(listEntryStringListStreamEntryCommandObject);

    Response<List<Map.Entry<String, List<StreamEntry>>>> response = pipeliningBase
        .xreadGroup("groupName", "consumer", xReadGroupParams, streams);

    MatcherAssert.assertThat(commands, Matchers.contains(listEntryStringListStreamEntryCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXreadGroupBinary() {
    byte[] groupName = "group".getBytes();
    byte[] consumer = "consumer".getBytes();
    XReadGroupParams xReadGroupParams = new XReadGroupParams();
    Map.Entry<byte[], byte[]> stream1 = new AbstractMap.SimpleImmutableEntry<>("stream1".getBytes(), "id1".getBytes());

    when(commandObjects.xreadGroup(groupName, consumer, xReadGroupParams, stream1)).thenReturn(listObjectCommandObject);

    Response<List<Object>> response = pipeliningBase.xreadGroup(groupName, consumer, xReadGroupParams, stream1);

    MatcherAssert.assertThat(commands, Matchers.contains(listObjectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXrevrange() {
    String end = "+";
    String start = "-";

    when(commandObjects.xrevrange("key", end, start)).thenReturn(listStreamEntryCommandObject);

    Response<List<StreamEntry>> response = pipeliningBase.xrevrange("key", end, start);

    MatcherAssert.assertThat(commands, Matchers.contains(listStreamEntryCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXrevrangeBinary() {
    byte[] key = "stream".getBytes();
    byte[] end = "endId".getBytes();
    byte[] start = "startId".getBytes();

    when(commandObjects.xrevrange(key, end, start)).thenReturn(listObjectCommandObject);

    Response<List<Object>> response = pipeliningBase.xrevrange(key, end, start);

    MatcherAssert.assertThat(commands, Matchers.contains(listObjectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXrevrangeWithCount() {
    String end = "+";
    String start = "-";
    int count = 10;

    when(commandObjects.xrevrange("key", end, start, count)).thenReturn(listStreamEntryCommandObject);

    Response<List<StreamEntry>> response = pipeliningBase.xrevrange("key", end, start, count);

    MatcherAssert.assertThat(commands, Matchers.contains(listStreamEntryCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXrevrangeWithCountBinary() {
    byte[] key = "stream".getBytes();
    byte[] end = "endId".getBytes();
    byte[] start = "startId".getBytes();
    int count = 10;

    when(commandObjects.xrevrange(key, end, start, count)).thenReturn(listObjectCommandObject);

    Response<List<Object>> response = pipeliningBase.xrevrange(key, end, start, count);

    MatcherAssert.assertThat(commands, Matchers.contains(listObjectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXrevrangeIds() {
    StreamEntryID end = new StreamEntryID("9999999999999-0");
    StreamEntryID start = new StreamEntryID("0-0");

    when(commandObjects.xrevrange("key", end, start)).thenReturn(listStreamEntryCommandObject);

    Response<List<StreamEntry>> response = pipeliningBase.xrevrange("key", end, start);

    MatcherAssert.assertThat(commands, Matchers.contains(listStreamEntryCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXrevrangeIdsWithCount() {
    StreamEntryID end = new StreamEntryID("9999999999999-0");
    StreamEntryID start = new StreamEntryID("0-0");
    int count = 10;

    when(commandObjects.xrevrange("key", end, start, count)).thenReturn(listStreamEntryCommandObject);

    Response<List<StreamEntry>> response = pipeliningBase.xrevrange("key", end, start, count);

    MatcherAssert.assertThat(commands, Matchers.contains(listStreamEntryCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXtrim() {
    when(commandObjects.xtrim("key", 1000L, true)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.xtrim("key", 1000L, true);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXtrimBinary() {
    byte[] key = "stream".getBytes();
    long maxLen = 1000L;
    boolean approximateLength = true;

    when(commandObjects.xtrim(key, maxLen, approximateLength)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.xtrim(key, maxLen, approximateLength);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXtrimWithParams() {
    XTrimParams params = new XTrimParams().maxLen(1000L);
    when(commandObjects.xtrim("key", params)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.xtrim("key", params);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testXtrimWithParamsBinary() {
    byte[] key = "stream".getBytes();
    XTrimParams params = new XTrimParams().maxLen(1000L);

    when(commandObjects.xtrim(key, params)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.xtrim(key, params);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

}
