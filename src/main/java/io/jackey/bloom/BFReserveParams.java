package io.jackey.bloom;

import static io.jackey.Protocol.toByteArray;

import io.jackey.CommandArguments;
import io.jackey.Protocol;
import io.jackey.bloom.RedisBloomProtocol.RedisBloomKeyword;
import io.jackey.params.IParams;

public class BFReserveParams implements IParams {

  private Integer expansion;
  private boolean nonScaling = false;

  public static BFReserveParams reserveParams() {
    return new BFReserveParams();
  }

  public BFReserveParams expansion(int expansion) {
    this.expansion = expansion;
    return this;
  }

  public BFReserveParams nonScaling() {
    this.nonScaling = true;
    return this;
  }

  @Override
  public void addParams(CommandArguments args) {
    if (expansion != null) {
      args.add(RedisBloomKeyword.EXPANSION).add(Protocol.toByteArray(expansion));
    }
    if (nonScaling) {
      args.add(RedisBloomKeyword.NONSCALING);
    }
  }
}
