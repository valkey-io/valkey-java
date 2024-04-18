package io.jackey.modules.search;

import static org.junit.Assert.assertEquals;

import io.jackey.search.RediSearchUtil;
import io.jackey.search.schemafields.NumericField;
import io.jackey.search.schemafields.SchemaField;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

  @Test
  public void floatArrayToByteArray() {
    float[] floats = new float[]{0.2f};
    byte[] bytes = RediSearchUtil.toByteArray(floats);
    byte[] expected = new byte[]{-51, -52, 76, 62};
    Assert.assertArrayEquals(expected, bytes);
  }

  @Test
  public void getSchemaFieldName() {
    SchemaField field = NumericField.of("$.num").as("num");

    Assert.assertEquals("$.num", field.getFieldName().getName());
    Assert.assertEquals("num", field.getFieldName().getAttribute());

    assertEquals("$.num", field.getName());
  }
}
