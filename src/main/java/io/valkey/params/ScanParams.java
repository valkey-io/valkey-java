package io.valkey.params;

import java.nio.ByteBuffer;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import io.valkey.CommandArguments;
import io.valkey.Protocol;
import io.valkey.Protocol.Keyword;
import io.valkey.util.SafeEncoder;

public class ScanParams implements IParams {

  private final Map<Keyword, ByteBuffer> params = new EnumMap<>(Keyword.class);

  public static final String SCAN_POINTER_START = String.valueOf(0);
  public static final byte[] SCAN_POINTER_START_BINARY = SafeEncoder.encode(SCAN_POINTER_START);

  public ScanParams match(final byte[] pattern) {
    params.put(Keyword.MATCH, ByteBuffer.wrap(pattern));
    return this;
  }

  /**
   * @see <a href="https://redis.io/commands/scan#the-match-option">MATCH option in Redis documentation</a>
   */
  public ScanParams match(final String pattern) {
    params.put(Keyword.MATCH, ByteBuffer.wrap(SafeEncoder.encode(pattern)));
    return this;
  }

  /**
   * @see <a href="https://redis.io/commands/scan#the-count-option">COUNT option in Redis documentation</a>
   */
  public ScanParams count(final Integer count) {
    params.put(Keyword.COUNT, ByteBuffer.wrap(Protocol.toByteArray(count)));
    return this;
  }

  @Override
  public void addParams(CommandArguments args) {
    for (Map.Entry<Keyword, ByteBuffer> param : params.entrySet()) {
      args.add(param.getKey());
      args.add(param.getValue().array());
    }
  }

  public byte[] binaryMatch() {
    if (params.containsKey(Keyword.MATCH)) {
      return params.get(Keyword.MATCH).array();
    } else {
      return null;
    }
  }

  public String match() {
    if (params.containsKey(Keyword.MATCH)) {
      return new String(params.get(Keyword.MATCH).array());
    } else {
      return null;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ScanParams that = (ScanParams) o;
    return Objects.equals(params, that.params);
  }

  @Override
  public int hashCode() {
    return Objects.hash(params);
  }
}
