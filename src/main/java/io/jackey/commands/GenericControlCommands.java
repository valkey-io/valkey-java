package io.jackey.commands;

import java.util.List;

import io.jackey.Module;
import io.jackey.params.FailoverParams;

public interface GenericControlCommands extends ConfigCommands, ScriptingControlCommands, SlowlogCommands {

  String failover();

  String failover(FailoverParams failoverParams);

  String failoverAbort();

  List<Module> moduleList();
}
