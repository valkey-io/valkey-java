package io.jackey.modules.bloom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.jackey.RedisProtocol;
import io.jackey.bloom.BFInsertParams;
import io.jackey.bloom.BFReserveParams;
import io.jackey.exceptions.JedisDataException;
import io.jackey.modules.RedisModuleCommandsTestBase;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class BloomTest extends RedisModuleCommandsTestBase {

  @BeforeClass
  public static void prepare() {
    RedisModuleCommandsTestBase.prepare();
  }
//
//  @AfterClass
//  public static void tearDown() {
////    RedisModuleCommandsTestBase.tearDown();
//  }

  public BloomTest(RedisProtocol protocol) {
    super(protocol);
  }

  @Test
  public void reserveBasic() {
    client.bfReserve("myBloom", 0.001, 100L);
    assertTrue(client.bfAdd("myBloom", "val1"));
    assertTrue(client.bfExists("myBloom", "val1"));
    assertFalse(client.bfExists("myBloom", "val2"));
  }

  @Test(expected = JedisDataException.class)
  public void reserveValidateZeroCapacity() {
    client.bfReserve("myBloom", 0.001, 0L);
  }

  @Test(expected = JedisDataException.class)
  public void reserveValidateZeroError() {
    client.bfReserve("myBloom", 0d, 100L);
  }

  @Test(expected = JedisDataException.class)
  public void reserveAlreadyExists() {
    client.bfReserve("myBloom", 0.1, 100);
    client.bfReserve("myBloom", 0.1, 100);
  }

  @Test
  public void reserveV2() {
    client.bfReserve("reserve-basic", 0.001, 2);
    Assert.assertEquals(Arrays.asList(true), client.bfInsert("reserve-basic", "a"));
    Assert.assertEquals(Arrays.asList(true), client.bfInsert("reserve-basic", "b"));
    Assert.assertEquals(Arrays.asList(true), client.bfInsert("reserve-basic", "c"));
  }

  @Test
  public void reserveEmptyParams() {
    client.bfReserve("empty-param", 0.001, 2, BFReserveParams.reserveParams());
    Assert.assertEquals(Arrays.asList(true), client.bfInsert("empty-param", "a"));
    Assert.assertEquals(Arrays.asList(true), client.bfInsert("empty-param", "b"));
    Assert.assertEquals(Arrays.asList(true), client.bfInsert("empty-param", "c"));
  }

  @Test
  public void reserveNonScaling() {
    client.bfReserve("nonscaling", 0.001, 2, BFReserveParams.reserveParams().nonScaling());
    Assert.assertEquals(Arrays.asList(true), client.bfInsert("nonscaling", "a"));
    Assert.assertEquals(Arrays.asList(true), client.bfInsert("nonscaling", "b"));
    Assert.assertEquals(Arrays.asList((Boolean) null), client.bfInsert("nonscaling", "c"));
  }

  @Test
  public void reserveExpansion() {
    // bf.reserve bfexpansion 0.001 1000 expansion 4
    client.bfReserve("bfexpansion", 0.001, 1000, BFReserveParams.reserveParams().expansion(4));
    Assert.assertEquals(Arrays.asList(true), client.bfInsert("bfexpansion", "a"));
    Assert.assertEquals(Arrays.asList(true), client.bfInsert("bfexpansion",
        BFInsertParams.insertParams().noCreate(), "b"));
  }

  @Test
  public void addExistsString() {
    assertTrue(client.bfAdd("newFilter", "foo"));
    assertTrue(client.bfExists("newFilter", "foo"));
    assertFalse(client.bfExists("newFilter", "bar"));
    assertFalse(client.bfAdd("newFilter", "foo"));
  }

  @Test
  public void testExistsNonExist() {
    assertFalse(client.bfExists("nonExist", "foo"));
  }

  @Test
  public void addExistsMulti() {
    List<Boolean> rv = client.bfMAdd("newFilter", "foo", "bar", "baz");
    assertEquals(Arrays.asList(true, true, true), rv);

    rv = client.bfMAdd("newFilter", "newElem", "bar", "baz");
    assertEquals(Arrays.asList(true, false, false), rv);
  }

  @Test
  public void testExample() {
    // Simple bloom filter using default module settings
    client.bfAdd("simpleBloom", "Mark");
    // Does "Mark" now exist?
    client.bfExists("simpleBloom", "Mark"); // true
    client.bfExists("simpleBloom", "Farnsworth"); // False

    // If you have a long list of items to check/add, you can use the
    // "multi" methods
    client.bfMAdd("simpleBloom", "foo", "bar", "baz", "bat", "bag");

    // Check if they exist:
    List<Boolean> rv = client.bfMExists("simpleBloom", "foo", "bar", "baz", "bat", "Mark", "nonexist");
    // All items except the last one will be 'true'
    assertEquals(Arrays.asList(true, true, true, true, true, false), rv);

    // Reserve a "customized" bloom filter
    client.bfReserve("specialBloom", 0.0001, 10000);
    client.bfAdd("specialBloom", "foo");
  }

  @Test
  public void testInsert() {
    client.bfInsert("b1", new BFInsertParams().capacity(1L), "1");
    assertTrue(client.bfExists("b1", "1"));

    // returning an error if the filter does not already exist
    JedisDataException jde = assertThrows("Should error if the filter does not already exist.",
        JedisDataException.class, () -> client.bfInsert("b2", new BFInsertParams().noCreate(), "1"));
    assertEquals("ERR not found", jde.getMessage());

    client.bfInsert("b3", new BFInsertParams().capacity(1L).error(0.0001), "2");
    assertTrue(client.bfExists("b3", "2"));
  }

  @Test
  public void issue49() {
    BFInsertParams insertOptions = new BFInsertParams();
    List<Boolean> insert = client.bfInsert("mykey", insertOptions, "a", "b", "c");
    assertEquals(3, insert.size());
  }

  @Test
  public void card() {
    client.bfInsert("test_card", new BFInsertParams().capacity(1L), "1");
    Assert.assertEquals(1L, client.bfCard("test_card"));

    // returning '0' if the filter does not already exist
    Assert.assertEquals(0L, client.bfCard("not_exist"));

    // returning an error if the filter is not a bloom filter
    client.set("foo", "bar");
    assertThrows("Should error if the filter is not a bloom filter",
        JedisDataException.class, () -> client.bfCard("foo"));
  }

  @Test
  public void info() {
    client.bfInsert("test_info", new BFInsertParams().capacity(1L), "1");
    Map<String, Object> info = client.bfInfo("test_info");
    assertEquals(Long.valueOf(1), info.get("Number of items inserted"));

    // returning an error if the filter does not already exist
    JedisDataException jde = assertThrows("Should error if the filter does not already exist.",
        JedisDataException.class, () -> client.bfInfo("not_exist"));
    assertEquals("ERR not found", jde.getMessage());
  }

  @Test
  public void insertNonScaling() {
    List<Boolean> insert = client.bfInsert("nonscaling_err",
        BFInsertParams.insertParams().capacity(4).nonScaling(), "a", "b", "c");
    assertEquals(Arrays.asList(true, true, true), insert);

    insert = client.bfInsert("nonscaling_err", "d", "e");
    assertEquals(Arrays.asList(true, null), insert);
  }

  @Test
  public void insertExpansion() {
    // BF.INSERT bfexpansion CAPACITY 3 expansion 3 ITEMS a b c d e f g h j k l o i u y t r e w q
    List<Boolean> insert = client.bfInsert("bfexpansion",
        BFInsertParams.insertParams().capacity(3).expansion(3),
        "a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "l",
        "o", "i", "u", "y", "t", "r", "e", "w", "q");
    assertEquals(20, insert.size());
  }

  @Test(timeout = 2000L)
  public void testScanDumpAndLoadChunk() {
    client.bfAdd("bloom-dump", "a");

    long iterator = 0;
    while (true) {
      Map.Entry<Long, byte[]> chunkData = client.bfScanDump("bloom-dump", iterator);
      iterator = chunkData.getKey();
      if (iterator == 0L) break;
      Assert.assertEquals("OK", client.bfLoadChunk("bloom-load", iterator, chunkData.getValue()));
    }

    // check for properties
    Assert.assertEquals(client.bfInfo("bloom-dump"), client.bfInfo("bloom-load"));
    // check for existing items
    assertTrue(client.bfExists("bloom-load", "a"));
  }
}
