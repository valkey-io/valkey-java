package io.valkey.bloom;

import static io.valkey.Protocol.toByteArray;

import io.valkey.CommandArguments;
import io.valkey.Protocol;
import io.valkey.bloom.RedisBloomProtocol.RedisBloomKeyword;
import io.valkey.params.IParams;

// [CAPACITY {capacity}] [NOCREATE]
public class CFInsertParams implements IParams {

  private Long capacity;
  private boolean noCreate = false;

  public static CFInsertParams insertParams() {
    return new CFInsertParams();
  }

  public CFInsertParams capacity(long capacity) {
    this.capacity = capacity;
    return this;
  }

  public CFInsertParams noCreate() {
    this.noCreate = true;
    return this;
  }

  @Override
  public void addParams(CommandArguments args) {
    if (capacity != null) {
      args.add(RedisBloomKeyword.CAPACITY).add(Protocol.toByteArray(capacity));
    }
    if (noCreate) {
      args.add(RedisBloomKeyword.NOCREATE);
    }
  }
}
