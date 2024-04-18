package io.jackey.timeseries;

import static io.jackey.Protocol.toByteArray;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import io.jackey.CommandArguments;
import io.jackey.Protocol;
import io.jackey.params.IParams;
import io.jackey.timeseries.TimeSeriesProtocol.TimeSeriesKeyword;

/**
 * Represents optional arguments of TS.ALTER command.
 */
public class TSAlterParams implements IParams {

  private Long retentionPeriod;
  private Long chunkSize;
  private DuplicatePolicy duplicatePolicy;
  private Map<String, String> labels;

  public TSAlterParams() {
  }

  public static TSAlterParams alterParams() {
    return new TSAlterParams();
  }

  public TSAlterParams retention(long retentionPeriod) {
    this.retentionPeriod = retentionPeriod;
    return this;
  }

  public TSAlterParams chunkSize(long chunkSize) {
    this.chunkSize = chunkSize;
    return this;
  }

  public TSAlterParams duplicatePolicy(DuplicatePolicy duplicatePolicy) {
    this.duplicatePolicy = duplicatePolicy;
    return this;
  }

  public TSAlterParams labels(Map<String, String> labels) {
    this.labels = labels;
    return this;
  }

  public TSAlterParams label(String label, String value) {
    if (this.labels == null) {
      this.labels = new LinkedHashMap<>();
    }
    this.labels.put(label, value);
    return this;
  }

  public TSAlterParams labelsReset() {
    return this.labels(Collections.emptyMap());
  }

  @Override
  public void addParams(CommandArguments args) {

    if (retentionPeriod != null) {
      args.add(TimeSeriesKeyword.RETENTION).add(Protocol.toByteArray(retentionPeriod));
    }

    if (chunkSize != null) {
      args.add(TimeSeriesKeyword.CHUNK_SIZE).add(Protocol.toByteArray(chunkSize));
    }

    if (duplicatePolicy != null) {
      args.add(TimeSeriesKeyword.DUPLICATE_POLICY).add(duplicatePolicy);
    }

    if (labels != null) {
      args.add(TimeSeriesKeyword.LABELS);
      labels.entrySet().forEach((entry) -> args.add(entry.getKey()).add(entry.getValue()));
    }
  }
}
