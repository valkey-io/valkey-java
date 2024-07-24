package io.valkey.mocked.pipeline;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.util.List;

import io.valkey.Response;
import io.valkey.args.BitCountOption;
import io.valkey.args.BitOP;
import io.valkey.params.BitPosParams;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public class PipeliningBaseBitmapCommandsTest extends PipeliningBaseMockedTestBase {

  @Test
  public void testBitcount() {
    when(commandObjects.bitcount("key")).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.bitcount("key");

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testBitcountBinary() {
    byte[] key = "key".getBytes();

    when(commandObjects.bitcount(key)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.bitcount(key);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testBitcountRange() {
    when(commandObjects.bitcount("key", 0, 10)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.bitcount("key", 0, 10);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testBitcountRangeBinary() {
    byte[] key = "key".getBytes();
    long start = 0L;
    long end = 10L;

    when(commandObjects.bitcount(key, start, end)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.bitcount(key, start, end);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testBitcountRangeOption() {
    BitCountOption option = BitCountOption.BYTE;

    when(commandObjects.bitcount("key", 0, 10, option)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.bitcount("key", 0, 10, option);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testBitcountRangeOptionBinary() {
    byte[] key = "key".getBytes();
    long start = 0L;
    long end = 10L;
    BitCountOption option = BitCountOption.BYTE;

    when(commandObjects.bitcount(key, start, end, option)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.bitcount(key, start, end, option);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testBitfield() {
    String[] arguments = { "INCRBY", "mykey", "2", "1" };

    when(commandObjects.bitfield("key", arguments)).thenReturn(listLongCommandObject);

    Response<List<Long>> response = pipeliningBase.bitfield("key", arguments);

    MatcherAssert.assertThat(commands, Matchers.contains(listLongCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testBitfieldBinary() {
    byte[] key = "key".getBytes();
    byte[][] arguments = { "INCRBY".getBytes(), "mykey".getBytes(), "2".getBytes(), "1".getBytes() };

    when(commandObjects.bitfield(key, arguments)).thenReturn(listLongCommandObject);

    Response<List<Long>> response = pipeliningBase.bitfield(key, arguments);

    MatcherAssert.assertThat(commands, Matchers.contains(listLongCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testBitfieldReadonly() {
    String[] arguments = { "GET", "u4", "0" };

    when(commandObjects.bitfieldReadonly("key", arguments)).thenReturn(listLongCommandObject);

    Response<List<Long>> response = pipeliningBase.bitfieldReadonly("key", arguments);

    MatcherAssert.assertThat(commands, Matchers.contains(listLongCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testBitfieldReadonlyBinary() {
    byte[] key = "key".getBytes();
    byte[][] arguments = { "GET".getBytes(), "u4".getBytes(), "0".getBytes() };

    when(commandObjects.bitfieldReadonly(key, arguments)).thenReturn(listLongCommandObject);

    Response<List<Long>> response = pipeliningBase.bitfieldReadonly(key, arguments);

    MatcherAssert.assertThat(commands, Matchers.contains(listLongCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testBitop() {
    BitOP op = BitOP.AND;

    when(commandObjects.bitop(op, "destKey", "srckey1", "srckey2")).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.bitop(op, "destKey", "srckey1", "srckey2");

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testBitopBinary() {
    BitOP op = BitOP.AND;
    byte[] destKey = "destKey".getBytes();
    byte[] srcKey1 = "srcKey1".getBytes();
    byte[] srcKey2 = "srcKey2".getBytes();

    when(commandObjects.bitop(op, destKey, srcKey1, srcKey2)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.bitop(op, destKey, srcKey1, srcKey2);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testBitpos() {
    when(commandObjects.bitpos("key", true)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.bitpos("key", true);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testBitposBinary() {
    byte[] key = "key".getBytes();
    boolean value = true;

    when(commandObjects.bitpos(key, value)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.bitpos(key, value);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testBitposParams() {
    BitPosParams params = new BitPosParams(0, -1);

    when(commandObjects.bitpos("key", true, params)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.bitpos("key", true, params);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testBitposParamsBinary() {
    byte[] key = "key".getBytes();
    boolean value = true;
    BitPosParams params = new BitPosParams(0);

    when(commandObjects.bitpos(key, value, params)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.bitpos(key, value, params);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testGetbit() {
    when(commandObjects.getbit("key", 100)).thenReturn(booleanCommandObject);

    Response<Boolean> response = pipeliningBase.getbit("key", 100);

    MatcherAssert.assertThat(commands, Matchers.contains(booleanCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testGetbitBinary() {
    byte[] key = "key".getBytes();
    long offset = 10L;

    when(commandObjects.getbit(key, offset)).thenReturn(booleanCommandObject);

    Response<Boolean> response = pipeliningBase.getbit(key, offset);

    MatcherAssert.assertThat(commands, Matchers.contains(booleanCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testSetbit() {
    when(commandObjects.setbit("key", 100, true)).thenReturn(booleanCommandObject);

    Response<Boolean> response = pipeliningBase.setbit("key", 100, true);

    MatcherAssert.assertThat(commands, Matchers.contains(booleanCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testSetbitBinary() {
    byte[] key = "key".getBytes();
    long offset = 10L;
    boolean value = true;

    when(commandObjects.setbit(key, offset, value)).thenReturn(booleanCommandObject);

    Response<Boolean> response = pipeliningBase.setbit(key, offset, value);

    MatcherAssert.assertThat(commands, Matchers.contains(booleanCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

}
