package io.jackey.search.schemafields;

import io.jackey.CommandArguments;
import io.jackey.search.FieldName;
import io.jackey.search.SearchProtocol.SearchKeyword;

public class NumericField extends SchemaField {

  private boolean sortable;
  private boolean noIndex;

  public NumericField(String fieldName) {
    super(fieldName);
  }

  public NumericField(FieldName fieldName) {
    super(fieldName);
  }

  public static NumericField of(String fieldName) {
    return new NumericField(fieldName);
  }

  public static NumericField of(FieldName fieldName) {
    return new NumericField(fieldName);
  }

  @Override
  public NumericField as(String attribute) {
    super.as(attribute);
    return this;
  }

  /**
   * Sorts the results by the value of this field.
   */
  public NumericField sortable() {
    this.sortable = true;
    return this;
  }

  /**
   * Avoid indexing.
   */
  public NumericField noIndex() {
    this.noIndex = true;
    return this;
  }

  @Override
  public void addParams(CommandArguments args) {
    args.addParams(fieldName);
    args.add(SearchKeyword.NUMERIC);

    if (sortable) {
      args.add(SearchKeyword.SORTABLE);
    }

    if (noIndex) {
      args.add(SearchKeyword.NOINDEX);
    }
  }
}
