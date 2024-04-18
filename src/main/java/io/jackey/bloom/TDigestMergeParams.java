package io.jackey.bloom;

import static io.jackey.Protocol.toByteArray;
import static io.jackey.bloom.RedisBloomProtocol.RedisBloomKeyword.COMPRESSION;
import static io.jackey.bloom.RedisBloomProtocol.RedisBloomKeyword.OVERRIDE;

import io.jackey.CommandArguments;
import io.jackey.Protocol;
import io.jackey.params.IParams;

public class TDigestMergeParams implements IParams {

  private Integer compression;
  private boolean override = false;

  public static TDigestMergeParams mergeParams() {
    return new TDigestMergeParams();
  }

  public TDigestMergeParams compression(int compression) {
    this.compression = compression;
    return this;
  }

  public TDigestMergeParams override() {
    this.override = true;
    return this;
  }

  @Override
  public void addParams(CommandArguments args) {
    if (compression != null) {
      args.add(COMPRESSION).add(Protocol.toByteArray(compression));
    }
    if (override) {
      args.add(OVERRIDE);
    }
  }
}
