package io.valkey.commands;

import java.util.List;

import io.valkey.resps.Slowlog;

public interface SlowlogCommands {

  String slowlogReset();

  long slowlogLen();

  List<Slowlog> slowlogGet();

  List<Object> slowlogGetBinary();

  List<Slowlog> slowlogGet(long entries);

  List<Object> slowlogGetBinary(long entries);

}
