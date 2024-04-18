package io.jackey.timeseries;

import io.jackey.CommandArguments;
import io.jackey.params.IParams;
import io.jackey.timeseries.TimeSeriesProtocol.TimeSeriesKeyword;

/**
 * Represents optional arguments of TS.MGET command.
 */
public class TSMGetParams implements IParams {

  private boolean latest;

  private boolean withLabels;
  private String[] selectedLabels;

  public static TSMGetParams multiGetParams() {
    return new TSMGetParams();
  }

  public TSMGetParams latest() {
    this.latest = true;
    return this;
  }

  public TSMGetParams withLabels(boolean withLabels) {
    this.withLabels = withLabels;
    return this;
  }

  public TSMGetParams withLabels() {
    return withLabels(true);
  }

  public TSMGetParams selectedLabels(String... labels) {
    this.selectedLabels = labels;
    return this;
  }

  @Override
  public void addParams(CommandArguments args) {
    if (latest) {
      args.add(TimeSeriesKeyword.LATEST);
    }

    if (withLabels) {
      args.add(TimeSeriesKeyword.WITHLABELS);
    } else if (selectedLabels != null) {
      args.add(TimeSeriesKeyword.SELECTED_LABELS);
      for (String label : selectedLabels) {
        args.add(label);
      }
    }
  }
}
