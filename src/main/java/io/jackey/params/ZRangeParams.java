package io.jackey.params;

import static io.jackey.args.RawableFactory.from;

import io.jackey.CommandArguments;
import io.jackey.Protocol.Keyword;
import io.jackey.args.Rawable;
import io.jackey.args.RawableFactory;

import java.util.Objects;

public class ZRangeParams implements IParams {

  private final Keyword by;
  private final Rawable min;
  private final Rawable max;
  private boolean rev = false;

  private boolean limit = false;
  private int offset;
  private int count;

  private ZRangeParams() {
    throw new InstantiationError("Empty constructor must not be called.");
  }

  public ZRangeParams(int min, int max) {
    this.by = null;
    this.min = RawableFactory.from(min);
    this.max = RawableFactory.from(max);
  }

  public static ZRangeParams zrangeParams(int min, int max) {
    return new ZRangeParams(min, max);
  }

  public ZRangeParams(double min, double max) {
    this.by = Keyword.BYSCORE;
    this.min = RawableFactory.from(min);
    this.max = RawableFactory.from(max);
  }

  public static ZRangeParams zrangeByScoreParams(double min, double max) {
    return new ZRangeParams(min, max);
  }

  private ZRangeParams(Keyword by, Rawable min, Rawable max) {
    if (by == null || by == Keyword.BYSCORE || by == Keyword.BYLEX) {
      // ok
    } else {
      throw new IllegalArgumentException(by.name() + " is not a valid ZRANGE type argument.");
    }
    this.by = by;
    this.min = min;
    this.max = max;
  }

  public ZRangeParams(Keyword by, String min, String max) {
    this(by, RawableFactory.from(min), RawableFactory.from(max));
  }

  public ZRangeParams(Keyword by, byte[] min, byte[] max) {
    this(by, RawableFactory.from(min), RawableFactory.from(max));
  }

  public static ZRangeParams zrangeByLexParams(String min, String max) {
    return new ZRangeParams(Keyword.BYLEX, min, max);
  }

  public static ZRangeParams zrangeByLexParams(byte[] min, byte[] max) {
    return new ZRangeParams(Keyword.BYLEX, min, max);
  }

  public ZRangeParams rev() {
    this.rev = true;
    return this;
  }

  public ZRangeParams limit(int offset, int count) {
    this.limit = true;
    this.offset = offset;
    this.count = count;
    return this;
  }

  @Override
  public void addParams(CommandArguments args) {

    args.add(min).add(max);
    if (by != null) {
      args.add(by);
    }

    if (rev) {
      args.add(Keyword.REV);
    }

    if (limit) {
      args.add(Keyword.LIMIT).add(offset).add(count);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ZRangeParams that = (ZRangeParams) o;
    return rev == that.rev && limit == that.limit && offset == that.offset && count == that.count && by == that.by && Objects.equals(min, that.min) && Objects.equals(max, that.max);
  }

  @Override
  public int hashCode() {
    return Objects.hash(by, min, max, rev, limit, offset, count);
  }
}
