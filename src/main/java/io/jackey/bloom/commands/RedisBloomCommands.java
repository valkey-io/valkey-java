package io.jackey.bloom.commands;

public interface RedisBloomCommands extends BloomFilterCommands, CuckooFilterCommands,
    CountMinSketchCommands, TopKFilterCommands, TDigestSketchCommands {

}
