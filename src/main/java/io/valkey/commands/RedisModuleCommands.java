package io.valkey.commands;

import io.valkey.bloom.commands.RedisBloomCommands;
import io.valkey.gears.RedisGearsCommands;
import io.valkey.graph.RedisGraphCommands;
import io.valkey.json.commands.RedisJsonCommands;
import io.valkey.search.RediSearchCommands;
import io.valkey.timeseries.RedisTimeSeriesCommands;

public interface RedisModuleCommands extends
    RediSearchCommands,
    RedisJsonCommands,
    RedisTimeSeriesCommands,
    RedisBloomCommands,
    RedisGraphCommands,
    RedisGearsCommands {

}
