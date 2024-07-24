package io.valkey.bloom;

import static io.valkey.Protocol.toByteArray;

import io.valkey.CommandArguments;
import io.valkey.Protocol;
import io.valkey.bloom.RedisBloomProtocol.RedisBloomKeyword;
import io.valkey.params.IParams;

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
