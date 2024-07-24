package io.valkey.commands.unified;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import io.valkey.RedisProtocol;
import io.valkey.exceptions.JedisDataException;
import io.valkey.params.GetExParams;
import io.valkey.params.LCSParams;
import io.valkey.resps.LCSMatchResult;
import org.junit.Assert;
import org.junit.Test;

public abstract class StringValuesCommandsTestBase extends UnifiedJedisCommandsTestBase {

  public StringValuesCommandsTestBase(RedisProtocol protocol) {
    super(protocol);
  }

  @Test
  public void setAndGet() {
    String status = jedis.set("foo", "bar");
    assertEquals("OK", status);

    String value = jedis.get("foo");
    assertEquals("bar", value);

    assertNull(jedis.get("bar"));
  }

  @Test
  public void getSet() {
    String value = jedis.getSet("foo", "bar");
    assertNull(value);
    value = jedis.get("foo");
    assertEquals("bar", value);
  }

  @Test
  public void getDel() {
    String status = jedis.set("foo", "bar");
    assertEquals("OK", status);

    String value = jedis.getDel("foo");
    assertEquals("bar", value);

    assertNull(jedis.get("foo"));
  }

  @Test
  public void getEx() {
    assertNull(jedis.getEx("foo", GetExParams.getExParams().ex(1)));
    jedis.set("foo", "bar");

    Assert.assertEquals("bar", jedis.getEx("foo", GetExParams.getExParams().ex(10)));
    long ttl = jedis.ttl("foo");
    assertTrue(ttl > 0 && ttl <= 10);

    Assert.assertEquals("bar", jedis.getEx("foo", GetExParams.getExParams().px(20000l)));
    ttl = jedis.ttl("foo");
    assertTrue(ttl > 10 && ttl <= 20);

    Assert.assertEquals("bar", jedis.getEx("foo", GetExParams.getExParams().exAt(System.currentTimeMillis() / 1000 + 30)));
    ttl = jedis.ttl("foo");
    assertTrue(ttl > 20 && ttl <= 30);

    Assert.assertEquals("bar", jedis.getEx("foo", GetExParams.getExParams().pxAt(System.currentTimeMillis() + 40000l)));
    ttl = jedis.ttl("foo");
    assertTrue(ttl > 30 && ttl <= 40);

    Assert.assertEquals("bar", jedis.getEx("foo", GetExParams.getExParams().persist()));
    Assert.assertEquals(-1, jedis.ttl("foo"));
  }

  @Test
  public void mget() {
    List<String> values = jedis.mget("foo", "bar");
    List<String> expected = new ArrayList<String>();
    expected.add(null);
    expected.add(null);

    assertEquals(expected, values);

    jedis.set("foo", "bar");

    expected = new ArrayList<String>();
    expected.add("bar");
    expected.add(null);
    values = jedis.mget("foo", "bar");

    assertEquals(expected, values);

    jedis.set("bar", "foo");

    expected = new ArrayList<String>();
    expected.add("bar");
    expected.add("foo");
    values = jedis.mget("foo", "bar");

    assertEquals(expected, values);
  }

  @Test
  public void setnx() {
    Assert.assertEquals(1, jedis.setnx("foo", "bar"));
    Assert.assertEquals("bar", jedis.get("foo"));

    Assert.assertEquals(0, jedis.setnx("foo", "bar2"));
    Assert.assertEquals("bar", jedis.get("foo"));
  }

  @Test
  public void setex() {
    String status = jedis.setex("foo", 20, "bar");
    assertEquals("OK", status);
    long ttl = jedis.ttl("foo");
    assertTrue(ttl > 0 && ttl <= 20);
  }

  @Test
  public void mset() {
    String status = jedis.mset("foo", "bar", "bar", "foo");
    assertEquals("OK", status);
    Assert.assertEquals("bar", jedis.get("foo"));
    Assert.assertEquals("foo", jedis.get("bar"));
  }

  @Test
  public void msetnx() {
    Assert.assertEquals(1, jedis.msetnx("foo", "bar", "bar", "foo"));
    Assert.assertEquals("bar", jedis.get("foo"));
    Assert.assertEquals("foo", jedis.get("bar"));

    Assert.assertEquals(0, jedis.msetnx("foo", "bar1", "bar2", "foo2"));
    Assert.assertEquals("bar", jedis.get("foo"));
    Assert.assertEquals("foo", jedis.get("bar"));
  }

  @Test
  public void incr() {
    Assert.assertEquals(1, jedis.incr("foo"));
    Assert.assertEquals(2, jedis.incr("foo"));
  }

  @Test(expected = JedisDataException.class)
  public void incrWrongValue() {
    jedis.set("foo", "bar");
    jedis.incr("foo");
  }

  @Test
  public void incrBy() {
    Assert.assertEquals(2, jedis.incrBy("foo", 2));
    Assert.assertEquals(5, jedis.incrBy("foo", 3));
  }

  @Test(expected = JedisDataException.class)
  public void incrByWrongValue() {
    jedis.set("foo", "bar");
    jedis.incrBy("foo", 2);
  }

  @Test
  public void incrByFloat() {
    Assert.assertEquals(10.5, jedis.incrByFloat("foo", 10.5), 0.0);
    Assert.assertEquals(10.6, jedis.incrByFloat("foo", 0.1), 0.0);
  }

  @Test(expected = JedisDataException.class)
  public void incrByFloatWrongValue() {
    jedis.set("foo", "bar");
    jedis.incrByFloat("foo", 2d);
  }

  @Test(expected = JedisDataException.class)
  public void decrWrongValue() {
    jedis.set("foo", "bar");
    jedis.decr("foo");
  }

  @Test
  public void decr() {
    Assert.assertEquals(-1, jedis.decr("foo"));
    Assert.assertEquals(-2, jedis.decr("foo"));
  }

  @Test
  public void decrBy() {
    Assert.assertEquals(-2, jedis.decrBy("foo", 2));
    Assert.assertEquals(-4, jedis.decrBy("foo", 2));
  }

  @Test(expected = JedisDataException.class)
  public void decrByWrongValue() {
    jedis.set("foo", "bar");
    jedis.decrBy("foo", 2);
  }

  @Test
  public void append() {
    Assert.assertEquals(3, jedis.append("foo", "bar"));
    Assert.assertEquals("bar", jedis.get("foo"));
    Assert.assertEquals(6, jedis.append("foo", "bar"));
    Assert.assertEquals("barbar", jedis.get("foo"));
  }

  @Test
  public void substr() {
    jedis.set("s", "This is a string");
    Assert.assertEquals("This", jedis.substr("s", 0, 3));
    Assert.assertEquals("ing", jedis.substr("s", -3, -1));
    Assert.assertEquals("This is a string", jedis.substr("s", 0, -1));
    Assert.assertEquals(" string", jedis.substr("s", 9, 100000));
  }

  @Test
  public void strlen() {
    String str = "This is a string";
    jedis.set("s", str);
    Assert.assertEquals(str.length(), jedis.strlen("s"));
  }

  @Test
  public void incrLargeNumbers() {
    Assert.assertEquals(1, jedis.incr("foo"));
    Assert.assertEquals(1L + Integer.MAX_VALUE, jedis.incrBy("foo", Integer.MAX_VALUE));
  }

  @Test(expected = JedisDataException.class)
  public void incrReallyLargeNumbers() {
    jedis.set("foo", Long.toString(Long.MAX_VALUE));
    jedis.incr("foo"); // Should throw an exception 
  }

  @Test
  public void psetex() {
    String status = jedis.psetex("foo", 20000, "bar");
    assertEquals("OK", status);
    long ttl = jedis.ttl("foo");
    assertTrue(ttl > 0 && ttl <= 20000);
  }

  @Test
  public void lcs() {
    jedis.mset("key1", "ohmytext", "key2", "mynewtext");

    LCSMatchResult stringMatchResult = jedis.lcs("key1", "key2",
            LCSParams.LCSParams());
    assertEquals("mytext", stringMatchResult.getMatchString());

    stringMatchResult = jedis.lcs( "key1", "key2",
            LCSParams.LCSParams().idx().withMatchLen());
    assertEquals(stringMatchResult.getLen(), 6);
    assertEquals(2, stringMatchResult.getMatches().size());

    stringMatchResult = jedis.lcs( "key1", "key2",
            LCSParams.LCSParams().idx().minMatchLen(10));
    assertEquals(0, stringMatchResult.getMatches().size());
  }

}
