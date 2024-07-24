package io.valkey.commands;

import io.valkey.bloom.commands.RedisBloomPipelineCommands;
import io.valkey.graph.RedisGraphPipelineCommands;
import io.valkey.json.commands.RedisJsonPipelineCommands;
import io.valkey.search.RediSearchPipelineCommands;
import io.valkey.timeseries.RedisTimeSeriesPipelineCommands;

public interface RedisModulePipelineCommands extends
    RediSearchPipelineCommands,
    RedisJsonPipelineCommands,
    RedisTimeSeriesPipelineCommands,
    RedisBloomPipelineCommands,
    RedisGraphPipelineCommands {

}
