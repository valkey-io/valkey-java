package io.jackey.commands.unified.cluster;

import io.jackey.RedisProtocol;
import io.jackey.commands.unified.BinaryValuesCommandsTestBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ClusterBinaryValuesCommandsTest extends BinaryValuesCommandsTestBase {

  public ClusterBinaryValuesCommandsTest(RedisProtocol protocol) {
    super(protocol);
  }

  @Before
  public void setUp() {
    jedis = ClusterCommandsTestHelper.getCleanCluster(protocol);
  }

  @After
  public void tearDown() {
    jedis.close();
    ClusterCommandsTestHelper.clearClusterData();
  }

  @Ignore
  @Override
  public void mget() {
  }

  @Ignore
  @Override
  public void mset() {
  }

  @Ignore
  @Override
  public void msetnx() {
  }
}
