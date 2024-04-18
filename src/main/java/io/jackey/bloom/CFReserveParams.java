package io.jackey.bloom;

import static io.jackey.Protocol.toByteArray;

import io.jackey.CommandArguments;
import io.jackey.Protocol;
import io.jackey.bloom.RedisBloomProtocol.RedisBloomKeyword;
import io.jackey.params.IParams;

// [BUCKETSIZE {bucketsize}] [MAXITERATIONS {maxiterations}] [EXPANSION {expansion}]
public class CFReserveParams implements IParams {

  private Long bucketSize;
  private Integer maxIterations;
  private Integer expansion;

  public static CFReserveParams reserveParams() {
    return new CFReserveParams();
  }

  public CFReserveParams bucketSize(long bucketSize) {
    this.bucketSize = bucketSize;
    return this;
  }

  public CFReserveParams maxIterations(int maxIterations) {
    this.maxIterations = maxIterations;
    return this;
  }

  public CFReserveParams expansion(int expansion) {
    this.expansion = expansion;
    return this;
  }

  @Override
  public void addParams(CommandArguments args) {
    if (bucketSize != null) {
      args.add(RedisBloomKeyword.BUCKETSIZE).add(Protocol.toByteArray(bucketSize));
    }
    if (maxIterations != null) {
      args.add(RedisBloomKeyword.MAXITERATIONS).add(Protocol.toByteArray(maxIterations));
    }
    if (expansion != null) {
      args.add(RedisBloomKeyword.EXPANSION).add(Protocol.toByteArray(expansion));
    }
  }
}
