package io.jackey.search.schemafields;

import io.jackey.CommandArguments;
import io.jackey.search.FieldName;
import io.jackey.search.SearchProtocol.SearchKeyword;

public class GeoField extends SchemaField {

  public GeoField(String fieldName) {
    super(fieldName);
  }

  public GeoField(FieldName fieldName) {
    super(fieldName);
  }

  public static GeoField of(String fieldName) {
    return new GeoField(fieldName);
  }

  public static GeoField of(FieldName fieldName) {
    return new GeoField(fieldName);
  }

  @Override
  public GeoField as(String attribute) {
    super.as(attribute);
    return this;
  }

  @Override
  public void addParams(CommandArguments args) {
    args.addParams(fieldName);
    args.add(SearchKeyword.GEO);
  }
}
