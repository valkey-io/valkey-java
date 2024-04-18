package io.jackey.providers;

import java.util.Collections;
import java.util.Map;

import io.jackey.CommandArguments;
import io.jackey.Connection;

public interface ConnectionProvider extends AutoCloseable {

  Connection getConnection();

  Connection getConnection(CommandArguments args);

  default Map<?, ?> getConnectionMap() {
    final Connection c = getConnection();
    return Collections.singletonMap(c.toString(), c);
  }
}
