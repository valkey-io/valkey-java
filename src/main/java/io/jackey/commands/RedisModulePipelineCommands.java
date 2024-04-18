package io.jackey.commands;

import io.jackey.bloom.commands.RedisBloomPipelineCommands;
import io.jackey.graph.RedisGraphPipelineCommands;
import io.jackey.json.commands.RedisJsonPipelineCommands;
import io.jackey.search.RediSearchPipelineCommands;
import io.jackey.timeseries.RedisTimeSeriesPipelineCommands;

public interface RedisModulePipelineCommands extends
    RediSearchPipelineCommands,
    RedisJsonPipelineCommands,
    RedisTimeSeriesPipelineCommands,
    RedisBloomPipelineCommands,
    RedisGraphPipelineCommands {

}
