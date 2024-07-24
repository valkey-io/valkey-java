package io.valkey.misc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;

import io.valkey.ClientSetInfoConfig;
import io.valkey.exceptions.JedisValidationException;
import org.junit.Assert;
import org.junit.Test;

public class ClientSetInfoConfigTest {

  @Test
  public void replaceSpacesWithHyphens() {
    Assert.assertEquals("Redis-Java-client",
        ClientSetInfoConfig.withLibNameSuffix("Redis Java client").getLibNameSuffix());
  }

  @Test
  public void errorForBraces() {
    Arrays.asList('(', ')', '[', ']', '{', '}')
        .forEach(brace -> assertThrows(JedisValidationException.class,
            () -> ClientSetInfoConfig.withLibNameSuffix("" + brace)));
  }
}
