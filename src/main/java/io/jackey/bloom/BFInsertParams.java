package io.jackey.bloom;

import static io.jackey.Protocol.toByteArray;

import io.jackey.CommandArguments;
import io.jackey.Protocol;
import io.jackey.bloom.RedisBloomProtocol.RedisBloomKeyword;
import io.jackey.params.IParams;

// [CAPACITY {cap}] [ERROR {error}] [EXPANSION {expansion}] [NOCREATE] [NONSCALING]
public class BFInsertParams implements IParams {

  private Long capacity;
  private Double errorRate;
  private Integer expansion;
  private boolean noCreate = false;
  private boolean nonScaling = false;

  public static BFInsertParams insertParams() {
    return new BFInsertParams();
  }

  public BFInsertParams capacity(long capacity) {
    this.capacity = capacity;
    return this;
  }

  public BFInsertParams error(double errorRate) {
    this.errorRate = errorRate;
    return this;
  }

  public BFInsertParams expansion(int expansion) {
    this.expansion = expansion;
    return this;
  }

  public BFInsertParams noCreate() {
    this.noCreate = true;
    return this;
  }

  public BFInsertParams nonScaling() {
    this.nonScaling = true;
    return this;
  }

  @Override
  public void addParams(CommandArguments args) {
    if (capacity != null) {
      args.add(RedisBloomKeyword.CAPACITY).add(Protocol.toByteArray(capacity));
    }
    if (errorRate != null) {
      args.add(RedisBloomKeyword.ERROR).add(Protocol.toByteArray(errorRate));
    }
    if (expansion != null) {
      args.add(RedisBloomKeyword.EXPANSION).add(Protocol.toByteArray(expansion));
    }
    if (noCreate) {
      args.add(RedisBloomKeyword.NOCREATE);
    }
    if (nonScaling) {
      args.add(RedisBloomKeyword.NONSCALING);
    }
  }
}
