package io.valkey.providers;

import java.util.Collections;
import java.util.Map;

import io.valkey.CommandArguments;
import io.valkey.Connection;

public interface ConnectionProvider extends AutoCloseable {

  Connection getConnection();

  Connection getConnection(CommandArguments args);

  default Map<?, ?> getConnectionMap() {
    final Connection c = getConnection();
    return Collections.singletonMap(c.toString(), c);
  }
}
