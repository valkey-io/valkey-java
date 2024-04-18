package io.jackey.timeseries;

import java.util.Map;

import io.jackey.util.KeyValue;

public class TSMGetElement extends KeyValue<String, TSElement> {

  private final Map<String, String> labels;

  public TSMGetElement(String key, Map<String, String> labels, TSElement value) {
    super(key, value);
    this.labels = labels;
  }

  public Map<String, String> getLabels() {
    return labels;
  }

  public TSElement getElement() {
    return getValue();
  }

  @Override
  public String toString() {
    return new StringBuilder().append(getClass().getSimpleName())
        .append("{key=").append(getKey())
        .append(", labels=").append(labels)
        .append(", element=").append(getElement())
        .append('}').toString();
  }
}
