package io.valkey;

public interface JedisBroadcastAndRoundRobinConfig {

  public enum RediSearchMode {
    DEFAULT, LIGHT;
  }

  RediSearchMode getRediSearchModeInCluster();
}
