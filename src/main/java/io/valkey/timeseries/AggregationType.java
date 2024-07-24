package io.valkey.timeseries;

import java.util.Locale;

import io.valkey.args.Rawable;
import io.valkey.util.SafeEncoder;

public enum AggregationType implements Rawable {

  AVG, SUM, MIN, MAX,
  RANGE, COUNT, FIRST, LAST,
  STD_P("STD.P"), STD_S("STD.S"),
  VAR_P("VAR.P"), VAR_S("VAR.S"),
  TWA;

  private final byte[] raw;

  private AggregationType() {
    raw = SafeEncoder.encode(name());
  }

  private AggregationType(String alt) {
    raw = SafeEncoder.encode(alt);
  }

  @Override
  public byte[] getRaw() {
    return raw;
  }

  public static AggregationType safeValueOf(String str) {
    try {
      return AggregationType.valueOf(str.replace('.', '_').toUpperCase(Locale.ENGLISH));
    } catch (IllegalArgumentException iae) {
      return null;
    }
  }
}
