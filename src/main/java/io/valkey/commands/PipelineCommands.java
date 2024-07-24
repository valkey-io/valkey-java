package io.valkey.commands;

public interface PipelineCommands extends KeyPipelineCommands, StringPipelineCommands,
    ListPipelineCommands, HashPipelineCommands, SetPipelineCommands, SortedSetPipelineCommands,
    GeoPipelineCommands, HyperLogLogPipelineCommands, StreamPipelineCommands,
    ScriptingKeyPipelineCommands, SampleKeyedPipelineCommands, FunctionPipelineCommands {
}
