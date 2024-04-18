package io.jackey.resps;

import io.jackey.Builder;
import io.jackey.BuilderFactory;

import java.util.List;

public class LatencyHistoryInfo {

    private final long timestamp;
    private final long latency;

    public LatencyHistoryInfo(long timestamp, long latency) {
        this.timestamp = timestamp;
        this.latency = latency;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getLatency() {
        return latency;
    }

    public static final Builder<LatencyHistoryInfo> LATENCY_HISTORY_BUILDER = new Builder<LatencyHistoryInfo>() {
        @Override
        public LatencyHistoryInfo build(Object data) {
            List<Object> commandData = (List<Object>) data;

            long timestamp = BuilderFactory.LONG.build(commandData.get(0));
            long latency = BuilderFactory.LONG.build(commandData.get(1));

            return new LatencyHistoryInfo(timestamp, latency);
        }
    };
}
