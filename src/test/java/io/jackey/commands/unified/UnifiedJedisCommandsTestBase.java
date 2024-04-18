package io.jackey.commands.unified;

import java.util.Collection;

import io.jackey.RedisProtocol;
import io.jackey.UnifiedJedis;
import io.jackey.commands.CommandsTestsParameters;
import org.junit.runners.Parameterized;

public abstract class UnifiedJedisCommandsTestBase {

  /**
   * Input data for parameterized tests. In principle all subclasses of this
   * class should be parameterized tests, to run with several versions of RESP.
   *
   * @see CommandsTestsParameters#respVersions()
   */
  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    return CommandsTestsParameters.respVersions();
  }

  protected final RedisProtocol protocol;

  protected UnifiedJedis jedis;

  /**
   * The RESP protocol is to be injected by the subclasses, usually via JUnit
   * parameterized tests, because most of the subclassed tests are meant to be
   * executed against multiple RESP versions. For the special cases where a single
   * RESP version is relevant, we still force the subclass to be explicit and
   * call this constructor.
   *
   * @param protocol The RESP protocol to use during the tests.
   */
  public UnifiedJedisCommandsTestBase(RedisProtocol protocol) {
    this.protocol = protocol;
  }
}
