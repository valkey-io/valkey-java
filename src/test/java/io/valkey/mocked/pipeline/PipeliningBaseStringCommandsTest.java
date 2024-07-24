package io.valkey.mocked.pipeline;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.util.List;

import io.valkey.Response;
import io.valkey.params.GetExParams;
import io.valkey.params.LCSParams;
import io.valkey.params.SetParams;
import io.valkey.resps.LCSMatchResult;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public class PipeliningBaseStringCommandsTest extends PipeliningBaseMockedTestBase {

  @Test
  public void testAppend() {
    when(commandObjects.append("key", "value")).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.append("key", "value");

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testAppendBinary() {
    byte[] key = "key".getBytes();
    byte[] value = "value".getBytes();

    when(commandObjects.append(key, value)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.append(key, value);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testDecr() {
    when(commandObjects.decr("key")).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.decr("key");

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testDecrBinary() {
    byte[] key = "key".getBytes();

    when(commandObjects.decr(key)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.decr(key);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testDecrBy() {
    when(commandObjects.decrBy("key", 10L)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.decrBy("key", 10L);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testDecrByBinary() {
    byte[] key = "key".getBytes();
    long decrement = 2L;

    when(commandObjects.decrBy(key, decrement)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.decrBy(key, decrement);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testGet() {
    when(commandObjects.get("key")).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.get("key");

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testGetBinary() {
    byte[] key = "key".getBytes();

    when(commandObjects.get(key)).thenReturn(bytesCommandObject);

    Response<byte[]> response = pipeliningBase.get(key);

    MatcherAssert.assertThat(commands, Matchers.contains(bytesCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testGetDel() {
    when(commandObjects.getDel("key")).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.getDel("key");

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testGetDelBinary() {
    byte[] key = "key".getBytes();

    when(commandObjects.getDel(key)).thenReturn(bytesCommandObject);

    Response<byte[]> response = pipeliningBase.getDel(key);

    MatcherAssert.assertThat(commands, Matchers.contains(bytesCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testGetEx() {
    GetExParams params = new GetExParams();

    when(commandObjects.getEx("key", params)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.getEx("key", params);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testGetExBinary() {
    byte[] key = "key".getBytes();
    GetExParams params = new GetExParams().ex(10);

    when(commandObjects.getEx(key, params)).thenReturn(bytesCommandObject);

    Response<byte[]> response = pipeliningBase.getEx(key, params);

    MatcherAssert.assertThat(commands, Matchers.contains(bytesCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testGetrange() {
    when(commandObjects.getrange("key", 0, 100)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.getrange("key", 0, 100);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testGetrangeBinary() {
    byte[] key = "key".getBytes();
    long startOffset = 0L;
    long endOffset = 10L;

    when(commandObjects.getrange(key, startOffset, endOffset)).thenReturn(bytesCommandObject);

    Response<byte[]> response = pipeliningBase.getrange(key, startOffset, endOffset);

    MatcherAssert.assertThat(commands, Matchers.contains(bytesCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testGetSet() {
    when(commandObjects.getSet("key", "value")).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.getSet("key", "value");

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testGetSetBinary() {
    byte[] key = "key".getBytes();
    byte[] value = "value".getBytes();

    when(commandObjects.getSet(key, value)).thenReturn(bytesCommandObject);

    Response<byte[]> response = pipeliningBase.getSet(key, value);

    MatcherAssert.assertThat(commands, Matchers.contains(bytesCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testIncr() {
    when(commandObjects.incr("key")).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.incr("key");

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testIncrBinary() {
    byte[] key = "key".getBytes();

    when(commandObjects.incr(key)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.incr(key);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testIncrBy() {
    when(commandObjects.incrBy("key", 10L)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.incrBy("key", 10L);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testIncrByBinary() {
    byte[] key = "key".getBytes();
    long increment = 2L;

    when(commandObjects.incrBy(key, increment)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.incrBy(key, increment);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testIncrByFloat() {
    when(commandObjects.incrByFloat("key", 1.5)).thenReturn(doubleCommandObject);

    Response<Double> response = pipeliningBase.incrByFloat("key", 1.5);

    MatcherAssert.assertThat(commands, Matchers.contains(doubleCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testIncrByFloatBinary() {
    byte[] key = "key".getBytes();
    double increment = 2.5;

    when(commandObjects.incrByFloat(key, increment)).thenReturn(doubleCommandObject);

    Response<Double> response = pipeliningBase.incrByFloat(key, increment);

    MatcherAssert.assertThat(commands, Matchers.contains(doubleCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testLcs() {
    LCSParams params = new LCSParams();

    when(commandObjects.lcs("keyA", "keyB", params)).thenReturn(lcsMatchResultCommandObject);

    Response<LCSMatchResult> response = pipeliningBase.lcs("keyA", "keyB", params);

    MatcherAssert.assertThat(commands, Matchers.contains(lcsMatchResultCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testLcsBinary() {
    byte[] keyA = "keyA".getBytes();
    byte[] keyB = "keyB".getBytes();
    LCSParams params = new LCSParams().withMatchLen();

    when(commandObjects.lcs(keyA, keyB, params)).thenReturn(lcsMatchResultCommandObject);

    Response<LCSMatchResult> response = pipeliningBase.lcs(keyA, keyB, params);

    MatcherAssert.assertThat(commands, Matchers.contains(lcsMatchResultCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testMget() {
    String[] keys = { "key1", "key2", "key3" };

    when(commandObjects.mget(keys)).thenReturn(listStringCommandObject);

    Response<List<String>> response = pipeliningBase.mget(keys);

    MatcherAssert.assertThat(commands, Matchers.contains(listStringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testMgetBinary() {
    byte[] key1 = "key1".getBytes();
    byte[] key2 = "key2".getBytes();

    when(commandObjects.mget(key1, key2)).thenReturn(listBytesCommandObject);

    Response<List<byte[]>> response = pipeliningBase.mget(key1, key2);

    MatcherAssert.assertThat(commands, Matchers.contains(listBytesCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testMset() {
    String[] keysvalues = { "key1", "value1", "key2", "value2" };

    when(commandObjects.mset(keysvalues)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.mset(keysvalues);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testMsetBinary() {
    byte[] key1 = "key1".getBytes();
    byte[] value1 = "value1".getBytes();
    byte[] key2 = "key2".getBytes();
    byte[] value2 = "value2".getBytes();

    when(commandObjects.mset(key1, value1, key2, value2)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.mset(key1, value1, key2, value2);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testMsetnx() {
    String[] keysvalues = { "key1", "value1", "key2", "value2" };

    when(commandObjects.msetnx(keysvalues)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.msetnx(keysvalues);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testMsetnxBinary() {
    byte[] key1 = "key1".getBytes();
    byte[] value1 = "value1".getBytes();
    byte[] key2 = "key2".getBytes();
    byte[] value2 = "value2".getBytes();

    when(commandObjects.msetnx(key1, value1, key2, value2)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.msetnx(key1, value1, key2, value2);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testPsetex() {
    when(commandObjects.psetex("key", 100000, "value")).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.psetex("key", 100000, "value");

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testPsetexBinary() {
    byte[] key = "key".getBytes();
    long milliseconds = 5000L;
    byte[] value = "value".getBytes();

    when(commandObjects.psetex(key, milliseconds, value)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.psetex(key, milliseconds, value);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testSet() {
    when(commandObjects.set("key", "value")).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.set("key", "value");

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testSetBinary() {
    byte[] key = "key".getBytes();
    byte[] value = "value".getBytes();

    when(commandObjects.set(key, value)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.set(key, value);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testSetWithParams() {
    SetParams params = new SetParams();

    when(commandObjects.set("key", "value", params)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.set("key", "value", params);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testSetWithParamsBinary() {
    byte[] key = "key".getBytes();
    byte[] value = "value".getBytes();
    SetParams params = new SetParams().nx().ex(10);

    when(commandObjects.set(key, value, params)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.set(key, value, params);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testSetGet() {
    when(commandObjects.setGet("key", "value")).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.setGet("key", "value");

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testSetGetBinary() {
    byte[] key = "key".getBytes();
    byte[] value = "value".getBytes();

    when(commandObjects.setGet(key, value)).thenReturn(bytesCommandObject);

    Response<byte[]> response = pipeliningBase.setGet(key, value);

    MatcherAssert.assertThat(commands, Matchers.contains(bytesCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testSetGetWithParams() {
    SetParams setParams = new SetParams();

    when(commandObjects.setGet("key", "value", setParams)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.setGet("key", "value", setParams);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testSetGetWithParamsBinary() {
    byte[] key = "key".getBytes();
    byte[] value = "value".getBytes();
    SetParams params = new SetParams().nx().ex(10);

    when(commandObjects.setGet(key, value, params)).thenReturn(bytesCommandObject);

    Response<byte[]> response = pipeliningBase.setGet(key, value, params);

    MatcherAssert.assertThat(commands, Matchers.contains(bytesCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testSetex() {
    when(commandObjects.setex("key", 60, "value")).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.setex("key", 60, "value");

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testSetexBinary() {
    byte[] key = "key".getBytes();
    long seconds = 60L;
    byte[] value = "value".getBytes();

    when(commandObjects.setex(key, seconds, value)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.setex(key, seconds, value);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testSetnx() {
    when(commandObjects.setnx("key", "value")).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.setnx("key", "value");

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testSetnxBinary() {
    byte[] key = "key".getBytes();
    byte[] value = "value".getBytes();

    when(commandObjects.setnx(key, value)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.setnx(key, value);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testSetrange() {
    when(commandObjects.setrange("key", 100, "value")).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.setrange("key", 100, "value");

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testSetrangeBinary() {
    byte[] key = "key".getBytes();
    long offset = 10L;
    byte[] value = "value".getBytes();

    when(commandObjects.setrange(key, offset, value)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.setrange(key, offset, value);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testStrlen() {
    when(commandObjects.strlen("key")).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.strlen("key");

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testStrlenBinary() {
    byte[] key = "key".getBytes();

    when(commandObjects.strlen(key)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.strlen(key);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testSubstr() {
    when(commandObjects.substr("key", 0, 10)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.substr("key", 0, 10);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testSubstrBinary() {
    byte[] key = "key".getBytes();
    int start = 0;
    int end = 5;

    when(commandObjects.substr(key, start, end)).thenReturn(bytesCommandObject);

    Response<byte[]> response = pipeliningBase.substr(key, start, end);

    MatcherAssert.assertThat(commands, Matchers.contains(bytesCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

}
