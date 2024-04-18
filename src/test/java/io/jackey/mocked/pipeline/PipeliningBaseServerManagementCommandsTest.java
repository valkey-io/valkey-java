package io.jackey.mocked.pipeline;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import io.jackey.Response;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public class PipeliningBaseServerManagementCommandsTest extends PipeliningBaseMockedTestBase {

  @Test
  public void testMemoryUsage() {
    when(commandObjects.memoryUsage("key")).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.memoryUsage("key");

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testMemoryUsageBinary() {
    byte[] key = "key".getBytes();

    when(commandObjects.memoryUsage(key)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.memoryUsage(key);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testMemoryUsageWithSamples() {
    when(commandObjects.memoryUsage("key", 10)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.memoryUsage("key", 10);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testMemoryUsageWithSamplesBinary() {
    byte[] key = "key".getBytes();
    int samples = 5;

    when(commandObjects.memoryUsage(key, samples)).thenReturn(longCommandObject);

    Response<Long> response = pipeliningBase.memoryUsage(key, samples);

    MatcherAssert.assertThat(commands, Matchers.contains(longCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

}
