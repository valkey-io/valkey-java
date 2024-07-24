package io.valkey.timeseries;

import static io.valkey.Protocol.toByteArray;
import static io.valkey.timeseries.TimeSeriesProtocol.MINUS;
import static io.valkey.timeseries.TimeSeriesProtocol.PLUS;
import static io.valkey.timeseries.TimeSeriesProtocol.TimeSeriesKeyword.*;
import static io.valkey.util.SafeEncoder.encode;

import io.valkey.CommandArguments;
import io.valkey.Protocol;
import io.valkey.params.IParams;
import io.valkey.util.SafeEncoder;

/**
 * Represents optional arguments of TS.MRANGE and TS.MREVRANGE commands.
 */
public class TSMRangeParams implements IParams {

  private Long fromTimestamp;
  private Long toTimestamp;

  private boolean latest;

  private long[] filterByTimestamps;
  private double[] filterByValues;

  private boolean withLabels;
  private String[] selectedLabels;

  private Integer count;

  private byte[] align;

  private AggregationType aggregationType;
  private long bucketDuration;
  private byte[] bucketTimestamp;

  private boolean empty;

  private String[] filters;

  private String groupByLabel;
  private String groupByReduce;

  public TSMRangeParams(long fromTimestamp, long toTimestamp) {
    this.fromTimestamp = fromTimestamp;
    this.toTimestamp = toTimestamp;
  }

  public static TSMRangeParams multiRangeParams(long fromTimestamp, long toTimestamp) {
    return new TSMRangeParams(fromTimestamp, toTimestamp);
  }

  public TSMRangeParams() {
  }

  public static TSMRangeParams multiRangeParams() {
    return new TSMRangeParams();
  }

  public TSMRangeParams fromTimestamp(long fromTimestamp) {
    this.fromTimestamp = fromTimestamp;
    return this;
  }

  public TSMRangeParams toTimestamp(long toTimestamp) {
    this.toTimestamp = toTimestamp;
    return this;
  }

  public TSMRangeParams latest() {
    this.latest = true;
    return this;
  }

  public TSMRangeParams filterByTS(long... timestamps) {
    this.filterByTimestamps = timestamps;
    return this;
  }

  public TSMRangeParams filterByValues(double min, double max) {
    this.filterByValues = new double[] {min, max};
    return this;
  }

  public TSMRangeParams withLabels(boolean withLabels) {
    this.withLabels = withLabels;
    return this;
  }

  public TSMRangeParams withLabels() {
    return withLabels(true);
  }

  public TSMRangeParams selectedLabels(String... labels) {
    this.selectedLabels = labels;
    return this;
  }

  public TSMRangeParams count(int count) {
    this.count = count;
    return this;
  }

  private TSMRangeParams align(byte[] raw) {
    this.align = raw;
    return this;
  }

  /**
   * This requires AGGREGATION.
   */
  public TSMRangeParams align(long timestamp) {
    return align(Protocol.toByteArray(timestamp));
  }

  /**
   * This requires AGGREGATION.
   */
  public TSMRangeParams alignStart() {
    return align(MINUS);
  }

  /**
   * This requires AGGREGATION.
   */
  public TSMRangeParams alignEnd() {
    return align(PLUS);
  }

  public TSMRangeParams aggregation(AggregationType aggregationType, long bucketDuration) {
    this.aggregationType = aggregationType;
    this.bucketDuration = bucketDuration;
    return this;
  }

  /**
   * This requires AGGREGATION.
   */
  public TSMRangeParams bucketTimestamp(String bucketTimestamp) {
    this.bucketTimestamp = SafeEncoder.encode(bucketTimestamp);
    return this;
  }

  /**
   * This requires AGGREGATION.
   */
  public TSMRangeParams bucketTimestampLow() {
    this.bucketTimestamp = MINUS;
    return this;
  }

  /**
   * This requires AGGREGATION.
   */
  public TSMRangeParams bucketTimestampHigh() {
    this.bucketTimestamp = PLUS;
    return this;
  }

  /**
   * This requires AGGREGATION.
   */
  public TSMRangeParams bucketTimestampMid() {
    this.bucketTimestamp = Protocol.BYTES_TILDE;
    return this;
  }

  /**
   * This requires AGGREGATION.
   */
  public TSMRangeParams empty() {
    this.empty = true;
    return this;
  }

  public TSMRangeParams filter(String... filters) {
    this.filters = filters;
    return this;
  }

  public TSMRangeParams groupBy(String label, String reduce) {
    this.groupByLabel = label;
    this.groupByReduce = reduce;
    return this;
  }

  @Override
  public void addParams(CommandArguments args) {

    if (filters == null) {
      throw new IllegalArgumentException("FILTER arguments must be set.");
    }

    if (fromTimestamp == null) {
      args.add(MINUS);
    } else {
      args.add(Protocol.toByteArray(fromTimestamp));
    }

    if (toTimestamp == null) {
      args.add(PLUS);
    } else {
      args.add(Protocol.toByteArray(toTimestamp));
    }

    if (latest) {
      args.add(LATEST);
    }

    if (filterByTimestamps != null) {
      args.add(FILTER_BY_TS);
      for (long ts : filterByTimestamps) {
        args.add(Protocol.toByteArray(ts));
      }
    }

    if (filterByValues != null) {
      args.add(FILTER_BY_VALUE);
      for (double value : filterByValues) {
        args.add(Protocol.toByteArray(value));
      }
    }

    if (withLabels) {
      args.add(WITHLABELS);
    } else if (selectedLabels != null) {
      args.add(SELECTED_LABELS);
      for (String label : selectedLabels) {
        args.add(label);
      }
    }

    if (count != null) {
      args.add(COUNT).add(Protocol.toByteArray(count));
    }

    if (aggregationType != null) {

      if (align != null) {
        args.add(ALIGN).add(align);
      }

      args.add(AGGREGATION).add(aggregationType).add(Protocol.toByteArray(bucketDuration));

      if (bucketTimestamp != null) {
        args.add(BUCKETTIMESTAMP).add(bucketTimestamp);
      }

      if (empty) {
        args.add(EMPTY);
      }
    }

    args.add(FILTER);
    for (String filter : filters) {
      args.add(filter);
    }

    if (groupByLabel != null && groupByReduce != null) {
      args.add(GROUPBY).add(groupByLabel).add(REDUCE).add(groupByReduce);
    }
  }
}
