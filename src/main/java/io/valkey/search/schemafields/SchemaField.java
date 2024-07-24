package io.valkey.search.schemafields;

import io.valkey.params.IParams;
import io.valkey.search.FieldName;

public abstract class SchemaField implements IParams {

  protected final FieldName fieldName;

  public SchemaField(String fieldName) {
    this.fieldName = new FieldName(fieldName);
  }

  public SchemaField(FieldName fieldName) {
    this.fieldName = fieldName;
  }

  public SchemaField as(String attribute) {
    fieldName.as(attribute);
    return this;
  }

  public final FieldName getFieldName() {
    return fieldName;
  }

  public final String getName() {
    return fieldName.getName();
  }
}
