package io.jackey;

public interface JedisBroadcastAndRoundRobinConfig {

  public enum RediSearchMode {
    DEFAULT, LIGHT;
  }

  RediSearchMode getRediSearchModeInCluster();
}
