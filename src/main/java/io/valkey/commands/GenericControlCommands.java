package io.valkey.commands;

import java.util.List;

import io.valkey.Module;
import io.valkey.params.FailoverParams;

public interface GenericControlCommands extends ConfigCommands, ScriptingControlCommands, SlowlogCommands {

  String failover();

  String failover(FailoverParams failoverParams);

  String failoverAbort();

  List<Module> moduleList();
}
