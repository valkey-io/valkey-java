package io.jackey.args;

import io.jackey.commands.ClusterCommands;
import io.jackey.util.SafeEncoder;

/**
 * Enumeration of cluster failover options.
 * <p>
 * Used by {@link ClusterCommands#clusterFailover(ClusterFailoverOption)}.
 */
public enum ClusterFailoverOption implements Rawable {

  FORCE, TAKEOVER;

  private final byte[] raw;

  private ClusterFailoverOption() {
    this.raw = SafeEncoder.encode(name());
  }

  @Override
  public byte[] getRaw() {
    return raw;
  }
}
