package io.valkey.search;

import static io.valkey.search.SearchProtocol.SearchKeyword.LIMITED;

import io.valkey.CommandArguments;
import io.valkey.params.IParams;

public class FTProfileParams implements IParams {

  private boolean limited;

  public FTProfileParams() {
  }

  public static FTProfileParams profileParams() {
    return new FTProfileParams();
  }

  /**
   * Removes details of {@code reader} iterator.
   */
  public FTProfileParams limited() {
    this.limited = true;
    return this;
  }

  @Override
  public void addParams(CommandArguments args) {

    if (limited) {
      args.add(LIMITED);
    }
  }
}
