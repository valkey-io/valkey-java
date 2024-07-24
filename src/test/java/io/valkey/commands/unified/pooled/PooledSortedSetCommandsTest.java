package io.valkey.commands.unified.pooled;

import io.valkey.RedisProtocol;
import io.valkey.commands.unified.SortedSetCommandsTestBase;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class PooledSortedSetCommandsTest extends SortedSetCommandsTestBase {

  public PooledSortedSetCommandsTest(RedisProtocol protocol) {
    super(protocol);
  }

  @Before
  public void setUp() {
    jedis = PooledCommandsTestHelper.getPooled(protocol);
    PooledCommandsTestHelper.clearData();
  }

  @After
  public void cleanUp() {
    jedis.close();
  }
}
