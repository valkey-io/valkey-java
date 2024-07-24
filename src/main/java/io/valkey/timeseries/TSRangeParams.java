package io.valkey.timeseries;

import static io.valkey.Protocol.toByteArray;
import static io.valkey.util.SafeEncoder.encode;

import io.valkey.CommandArguments;
import io.valkey.Protocol;
import io.valkey.params.IParams;
import io.valkey.timeseries.TimeSeriesProtocol.TimeSeriesKeyword;
import io.valkey.util.SafeEncoder;

/**
 * Represents optional arguments of TS.RANGE and TS.REVRANGE commands.
 */
public class TSRangeParams implements IParams {

  private Long fromTimestamp;
  private Long toTimestamp;

  private boolean latest;

  private long[] filterByTimestamps;
  private double[] filterByValues;

  private Integer count;

  private byte[] align;

  private AggregationType aggregationType;
  private long bucketDuration;
  private byte[] bucketTimestamp;

  private boolean empty;

  public TSRangeParams(long fromTimestamp, long toTimestamp) {
    this.fromTimestamp = fromTimestamp;
    this.toTimestamp = toTimestamp;
  }

  public static TSRangeParams rangeParams(long fromTimestamp, long toTimestamp) {
    return new TSRangeParams(fromTimestamp, toTimestamp);
  }

  public TSRangeParams() {
  }

  public static TSRangeParams rangeParams() {
    return new TSRangeParams();
  }

  public TSRangeParams fromTimestamp(long fromTimestamp) {
    this.fromTimestamp = fromTimestamp;
    return this;
  }

  public TSRangeParams toTimestamp(long toTimestamp) {
    this.toTimestamp = toTimestamp;
    return this;
  }

  public TSRangeParams latest() {
    this.latest = true;
    return this;
  }

  public TSRangeParams filterByTS(long... timestamps) {
    this.filterByTimestamps = timestamps;
    return this;
  }

  public TSRangeParams filterByValues(double min, double max) {
    this.filterByValues = new double[]{min, max};
    return this;
  }

  public TSRangeParams count(int count) {
    this.count = count;
    return this;
  }

  private TSRangeParams align(byte[] raw) {
    this.align = raw;
    return this;
  }

  /**
   * This requires AGGREGATION.
   */
  public TSRangeParams align(long timestamp) {
    return align(Protocol.toByteArray(timestamp));
  }

  /**
   * This requires AGGREGATION.
   */
  public TSRangeParams alignStart() {
    return align(TimeSeriesProtocol.MINUS);
  }

  /**
   * This requires AGGREGATION.
   */
  public TSRangeParams alignEnd() {
    return align(TimeSeriesProtocol.PLUS);
  }

  public TSRangeParams aggregation(AggregationType aggregationType, long bucketDuration) {
    this.aggregationType = aggregationType;
    this.bucketDuration = bucketDuration;
    return this;
  }

  /**
   * This requires AGGREGATION.
   */
  public TSRangeParams bucketTimestamp(String bucketTimestamp) {
    this.bucketTimestamp = SafeEncoder.encode(bucketTimestamp);
    return this;
  }

  /**
   * This requires AGGREGATION.
   */
  public TSRangeParams bucketTimestampLow() {
    this.bucketTimestamp = TimeSeriesProtocol.MINUS;
    return this;
  }

  /**
   * This requires AGGREGATION.
   */
  public TSRangeParams bucketTimestampHigh() {
    this.bucketTimestamp = TimeSeriesProtocol.PLUS;
    return this;
  }

  /**
   * This requires AGGREGATION.
   */
  public TSRangeParams bucketTimestampMid() {
    this.bucketTimestamp = Protocol.BYTES_TILDE;
    return this;
  }

  /**
   * This requires AGGREGATION.
   */
  public TSRangeParams empty() {
    this.empty = true;
    return this;
  }

  @Override
  public void addParams(CommandArguments args) {

    if (fromTimestamp == null) {
      args.add(TimeSeriesProtocol.MINUS);
    } else {
      args.add(Protocol.toByteArray(fromTimestamp));
    }

    if (toTimestamp == null) {
      args.add(TimeSeriesProtocol.PLUS);
    } else {
      args.add(Protocol.toByteArray(toTimestamp));
    }

    if (latest) {
      args.add(TimeSeriesKeyword.LATEST);
    }

    if (filterByTimestamps != null) {
      args.add(TimeSeriesKeyword.FILTER_BY_TS);
      for (long ts : filterByTimestamps) {
        args.add(Protocol.toByteArray(ts));
      }
    }

    if (filterByValues != null) {
      args.add(TimeSeriesKeyword.FILTER_BY_VALUE);
      for (double value : filterByValues) {
        args.add(Protocol.toByteArray(value));
      }
    }

    if (count != null) {
      args.add(TimeSeriesKeyword.COUNT).add(Protocol.toByteArray(count));
    }

    if (aggregationType != null) {

      if (align != null) {
        args.add(TimeSeriesKeyword.ALIGN).add(align);
      }

      args.add(TimeSeriesKeyword.AGGREGATION).add(aggregationType).add(Protocol.toByteArray(bucketDuration));

      if (bucketTimestamp != null) {
        args.add(TimeSeriesKeyword.BUCKETTIMESTAMP).add(bucketTimestamp);
      }

      if (empty) {
        args.add(TimeSeriesKeyword.EMPTY);
      }
    }
  }
}
