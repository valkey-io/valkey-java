package io.jackey.commands;

import io.jackey.bloom.commands.RedisBloomCommands;
import io.jackey.gears.RedisGearsCommands;
import io.jackey.graph.RedisGraphCommands;
import io.jackey.json.commands.RedisJsonCommands;
import io.jackey.search.RediSearchCommands;
import io.jackey.timeseries.RedisTimeSeriesCommands;

public interface RedisModuleCommands extends
    RediSearchCommands,
    RedisJsonCommands,
    RedisTimeSeriesCommands,
    RedisBloomCommands,
    RedisGraphCommands,
    RedisGearsCommands {

}
