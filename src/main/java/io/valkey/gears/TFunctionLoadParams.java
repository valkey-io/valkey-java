package io.valkey.gears;

import io.valkey.CommandArguments;
import io.valkey.gears.RedisGearsProtocol.GearsKeyword;
import io.valkey.params.IParams;

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
