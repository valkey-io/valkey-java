package io.jackey.timeseries;

import static io.jackey.timeseries.TimeSeriesProtocol.TimeSeriesKeyword.LATEST;

import io.jackey.CommandArguments;
import io.jackey.params.IParams;

/**
 * Represents optional arguments of TS.GET command.
 */
public class TSGetParams implements IParams {

  private boolean latest;

  public static TSGetParams getParams() {
    return new TSGetParams();
  }

  public TSGetParams latest() {
    this.latest = true;
    return this;
  }

  @Override
  public void addParams(CommandArguments args) {
    if (latest) {
      args.add(LATEST);
    }
  }
}
