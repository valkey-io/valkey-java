package io.jackey.params;

import static io.jackey.args.RawableFactory.from;

import io.jackey.CommandArguments;
import io.jackey.Protocol.Keyword;
import io.jackey.StreamEntryID;
import io.jackey.args.Rawable;
import io.jackey.args.RawableFactory;

import java.util.Objects;

public class XPendingParams implements IParams {

  private Long idle;
  private Rawable start;
  private Rawable end;
  private Integer count;
  private Rawable consumer;

  public XPendingParams(StreamEntryID start, StreamEntryID end, int count) {
    this(start.toString(), end.toString(), count);
  }

  public XPendingParams(String start, String end, int count) {
    this(RawableFactory.from(start), RawableFactory.from(end), count);
  }

  public XPendingParams(byte[] start, byte[] end, int count) {
    this(RawableFactory.from(start), RawableFactory.from(end), count);
  }

  private XPendingParams(Rawable start, Rawable end, Integer count) {
    this.start = start;
    this.end = end;
    this.count = count;
  }

  public XPendingParams() {
    this.start = null;
    this.end = null;
    this.count = null;
  }

  public static XPendingParams xPendingParams(StreamEntryID start, StreamEntryID end, int count) {
    return new XPendingParams(start, end, count);
  }

  public static XPendingParams xPendingParams(String start, String end, int count) {
    return new XPendingParams(start, end, count);
  }

  public static XPendingParams xPendingParams(byte[] start, byte[] end, int count) {
    return new XPendingParams(start, end, count);
  }

  public static XPendingParams xPendingParams() {
    return new XPendingParams();
  }

  public XPendingParams idle(long idle) {
    this.idle = idle;
    return this;
  }

  public XPendingParams start(StreamEntryID start) {
    this.start = RawableFactory.from(start.toString());
    return this;
  }

  public XPendingParams end(StreamEntryID end) {
    this.end = RawableFactory.from(end.toString());
    return this;
  }

  public XPendingParams count(int count) {
    this.count = count;
    return this;
  }

  public XPendingParams consumer(String consumer) {
    this.consumer = RawableFactory.from(consumer);
    return this;
  }

  public XPendingParams consumer(byte[] consumer) {
    this.consumer = RawableFactory.from(consumer);
    return this;
  }

  @Override
  public void addParams(CommandArguments args) {
    if (count == null) {
      throw new IllegalArgumentException("start, end and count must be set.");
    }
    if (start == null) start = RawableFactory.from("-");
    if (end == null) end = RawableFactory.from("+");

    if (idle != null) {
      args.add(Keyword.IDLE).add(idle);
    }

    args.add(start).add(end).add(count);

    if (consumer != null) {
      args.add(consumer);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    XPendingParams that = (XPendingParams) o;
    return Objects.equals(idle, that.idle) && Objects.equals(start, that.start) && Objects.equals(end, that.end) && Objects.equals(count, that.count) && Objects.equals(consumer, that.consumer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idle, start, end, count, consumer);
  }
}
