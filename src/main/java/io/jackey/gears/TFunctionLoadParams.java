package io.jackey.gears;

import io.jackey.CommandArguments;
import io.jackey.gears.RedisGearsProtocol.GearsKeyword;
import io.jackey.params.IParams;

public class TFunctionLoadParams implements IParams {
  private boolean replace = false;
  private String config;

  public static TFunctionLoadParams loadParams() {
    return new TFunctionLoadParams();
  }

  @Override
  public void addParams(CommandArguments args) {
    if (replace) {
      args.add(GearsKeyword.REPLACE);
    }

    if (config != null && !config.isEmpty()) {
      args.add(GearsKeyword.CONFIG).add(config);
    }
  }

  public TFunctionLoadParams replace() {
    this.replace = true;
    return this;
  }

  public TFunctionLoadParams config(String config) {
    this.config = config;
    return this;
  }
}
