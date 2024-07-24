package io.valkey.commands.jedis;

import java.util.Collection;

import io.valkey.DefaultJedisClientConfig;
import io.valkey.HostAndPort;
import io.valkey.HostAndPorts;
import io.valkey.Jedis;
import io.valkey.RedisProtocol;
import io.valkey.commands.CommandsTestsParameters;
import org.junit.After;
import org.junit.Before;
import org.junit.runners.Parameterized.Parameters;

public abstract class JedisCommandsTestBase {

  /**
   * Input data for parameterized tests. In principle all subclasses of this
   * class should be parameterized tests, to run with several versions of RESP.
   *
   * @see CommandsTestsParameters#respVersions()
   */
  @Parameters
  public static Collection<Object[]> data() {
    return CommandsTestsParameters.respVersions();
  }

  protected static final HostAndPort hnp = HostAndPorts.getRedisServers().get(0);

  protected final RedisProtocol protocol;

  protected Jedis jedis;

  /**
   * The RESP protocol is to be injected by the subclasses, usually via JUnit
   * parameterized tests, because most of the subclassed tests are meant to be
   * executed against multiple RESP versions. For the special cases where a single
   * RESP version is relevant, we still force the subclass to be explicit and
   * call this constructor.
   *
   * @param protocol The RESP protocol to use during the tests.
   */
  public JedisCommandsTestBase(RedisProtocol protocol) {
    this.protocol = protocol;
  }

  @Before
  public void setUp() throws Exception {
//    jedis = new Jedis(hnp, DefaultJedisClientConfig.builder().timeoutMillis(500).password("foobared").build());
    jedis = new Jedis(hnp, DefaultJedisClientConfig.builder()
        .protocol(protocol).timeoutMillis(500).password("foobared").build());
    jedis.flushAll();
  }

  @After
  public void tearDown() throws Exception {
    jedis.close();
  }

  protected Jedis createJedis() {
//    return new Jedis(hnp, DefaultJedisClientConfig.builder().password("foobared").build());
    return new Jedis(hnp, DefaultJedisClientConfig.builder()
        .protocol(protocol).password("foobared").build());
  }
}
