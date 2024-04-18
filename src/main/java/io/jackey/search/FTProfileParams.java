package io.jackey.search;

import static io.jackey.search.SearchProtocol.SearchKeyword.LIMITED;

import io.jackey.CommandArguments;
import io.jackey.params.IParams;

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
