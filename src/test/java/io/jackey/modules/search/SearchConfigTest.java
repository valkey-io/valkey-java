package io.jackey.modules.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.Map;

import io.jackey.RedisProtocol;
import io.jackey.modules.RedisModuleCommandsTestBase;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class SearchConfigTest extends RedisModuleCommandsTestBase {

  @BeforeClass
  public static void prepare() {
    RedisModuleCommandsTestBase.prepare();
  }
//
//  @AfterClass
//  public static void tearDown() {
////    RedisModuleCommandsTestBase.tearDown();
//  }

  public SearchConfigTest(RedisProtocol protocol) {
    super(protocol);
  }

  @Test
  public void config() {
    Map<String, Object> map = client.ftConfigGet("TIMEOUT");
    assertEquals(1, map.size());
    String value = (String) map.get("TIMEOUT");
    try {
      assertNotNull(value);
    } finally {
      Assert.assertEquals("OK", client.ftConfigSet("timeout", value));
    }
  }

  @Test
  public void configOnTimeout() {
    // confirm default
    Assert.assertEquals(Collections.singletonMap("ON_TIMEOUT", "return"), client.ftConfigGet("ON_TIMEOUT"));

    try {
      Assert.assertEquals("OK", client.ftConfigSet("ON_TIMEOUT", "fail"));
      Assert.assertEquals(Collections.singletonMap("ON_TIMEOUT", "fail"), client.ftConfigGet("ON_TIMEOUT"));

    } finally {
      // restore to default
      Assert.assertEquals("OK", client.ftConfigSet("ON_TIMEOUT", "return"));
    }
  }

  @Test
  public void dialectConfig() {
    // confirm default
    Assert.assertEquals(Collections.singletonMap("DEFAULT_DIALECT", "1"), client.ftConfigGet("DEFAULT_DIALECT"));

    try {
      Assert.assertEquals("OK", client.ftConfigSet("DEFAULT_DIALECT", "2"));
      Assert.assertEquals(Collections.singletonMap("DEFAULT_DIALECT", "2"), client.ftConfigGet("DEFAULT_DIALECT"));

    } finally {
      // restore to default
      Assert.assertEquals("OK", client.ftConfigSet("DEFAULT_DIALECT", "1"));
    }
  }
}
