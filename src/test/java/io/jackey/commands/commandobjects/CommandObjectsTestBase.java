package io.jackey.commands.commandobjects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Collection;

import io.jackey.CommandObject;
import io.jackey.CommandObjects;
import io.jackey.DefaultJedisClientConfig;
import io.jackey.HostAndPort;
import io.jackey.PipeliningBase;
import io.jackey.RedisProtocol;
import io.jackey.UnifiedJedis;
import io.jackey.args.FlushMode;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.jackey.commands.CommandsTestsParameters;
import io.jackey.executors.CommandExecutor;
import io.jackey.executors.DefaultCommandExecutor;
import io.jackey.providers.ConnectionProvider;
import io.jackey.providers.PooledConnectionProvider;

/**
 * Base class for CommandObjects tests. The tests are parameterized to run with
 * several versions of RESP. The idea is to test commands at this low level, using
 * a simple executor. Higher level concepts like {@link UnifiedJedis},
 * or {@link PipeliningBase} can be tested separately with mocks.
 * <p>
 * This class provides the basic setup, except the {@link HostAndPort} for connecting
 * to a running Redis server. That one is provided by abstract subclasses, depending
 * on if a Redis Stack server is needed, or a standalone suffices.
 */
@RunWith(Parameterized.class)
public abstract class CommandObjectsTestBase {

  /**
   * Input data for parameterized tests. In principle all subclasses of this
   * class should be parameterized tests, to run with several versions of RESP.
   *
   * @see CommandsTestsParameters#respVersions()
   */
  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    return CommandsTestsParameters.respVersions();
  }

  /**
   * RESP protocol used in the tests. Injected from subclasses.
   */
  protected final RedisProtocol protocol;

  /**
   * Host and port of the Redis server to connect to. Injected from subclasses.
   */
  protected final HostAndPort nodeInfo;

  /**
   * Password to use when connecting to the Redis server, if needed. Injected from subclasses.
   */
  private final String authPassword;

  /**
   * The {@link CommandObjects} to use for the tests. This is the subject-under-test.
   */
  protected final CommandObjects commandObjects;

  /**
   * A {@link CommandExecutor} that can execute commands against the running Redis server.
   * Not exposed to subclasses, which should use a convenience method instead.
   */
  private CommandExecutor commandExecutor;

  public CommandObjectsTestBase(RedisProtocol protocol, HostAndPort nodeInfo, String authPassword) {
    this.protocol = protocol;
    this.nodeInfo = nodeInfo;
    this.authPassword = authPassword;
    commandObjects = new CommandObjects();
    commandObjects.setProtocol(protocol);
  }

  @Before
  public void setUp() {
    // Configure a default command executor.
    DefaultJedisClientConfig clientConfig = DefaultJedisClientConfig.builder()
        .protocol(protocol).password(authPassword).build();

    ConnectionProvider connectionProvider = new PooledConnectionProvider(nodeInfo, clientConfig);

    commandExecutor = new DefaultCommandExecutor(connectionProvider);

    // Cleanup before each test.
    assertThat(
        commandExecutor.executeCommand(commandObjects.flushAll()),
        equalTo("OK"));

    assertThat(
        commandExecutor.executeCommand(commandObjects.functionFlush(FlushMode.SYNC)),
        equalTo("OK"));
  }

  /**
   * Convenience method for subclasses, for running any {@link CommandObject}.
   */
  protected <T> T exec(CommandObject<T> commandObject) {
    return commandExecutor.executeCommand(commandObject);
  }

}
