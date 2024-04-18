package io.jackey.search.schemafields;

import io.jackey.CommandArguments;
import io.jackey.search.FieldName;
import io.jackey.search.SearchProtocol.SearchKeyword;

public class GeoShapeField extends SchemaField {

  public enum CoordinateSystem {

    /**
     * For cartesian (X,Y).
     */
    FLAT,

    /**
     * For geographic (lon, lat).
     */
    SPHERICAL
  }

  private final CoordinateSystem system;

  public GeoShapeField(String fieldName, CoordinateSystem system) {
    super(fieldName);
    this.system = system;
  }

  public GeoShapeField(FieldName fieldName, CoordinateSystem system) {
    super(fieldName);
    this.system = system;
  }

  public static GeoShapeField of(String fieldName, CoordinateSystem system) {
    return new GeoShapeField(fieldName, system);
  }

  @Override
  public GeoShapeField as(String attribute) {
    super.as(attribute);
    return this;
  }

  @Override
  public void addParams(CommandArguments args) {
    args.addParams(fieldName).add(SearchKeyword.GEOSHAPE).add(system);
  }
}
