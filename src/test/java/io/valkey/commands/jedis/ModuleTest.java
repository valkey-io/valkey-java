package io.valkey.commands.jedis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import io.valkey.Module;
import io.valkey.RedisProtocol;
import io.valkey.commands.ProtocolCommand;
import io.valkey.util.SafeEncoder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ModuleTest extends JedisCommandsTestBase {

  static enum ModuleCommand implements ProtocolCommand {

    SIMPLE("testmodule.simple");

    private final byte[] raw;

    private ModuleCommand(String alt) {
      raw = SafeEncoder.encode(alt);
    }

    @Override
    public byte[] getRaw() {
      return raw;
    }
  }

  public ModuleTest(RedisProtocol protocol) {
    super(protocol);
  }

  @Test
  public void testModules() {
    try {
      assertEquals("OK", jedis.moduleLoad("/tmp/testmodule.so"));

      List<Module> modules = jedis.moduleList();

      assertEquals("testmodule", modules.get(0).getName());

      Object output = jedis.sendCommand(ModuleCommand.SIMPLE);
      assertTrue((Long) output > 0);

    } finally {

      assertEquals("OK", jedis.moduleUnload("testmodule"));
      assertEquals(Collections.emptyList(), jedis.moduleList());
    }
  }
}
