package io.jackey.bloom;

import static io.jackey.Protocol.toByteArray;

import io.jackey.CommandArguments;
import io.jackey.Protocol;
import io.jackey.bloom.RedisBloomProtocol.RedisBloomKeyword;
import io.jackey.params.IParams;

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
