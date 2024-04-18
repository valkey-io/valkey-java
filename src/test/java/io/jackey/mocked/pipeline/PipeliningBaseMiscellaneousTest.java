package io.jackey.mocked.pipeline;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.jackey.BuilderFactory;
import io.jackey.CommandArguments;
import io.jackey.CommandObject;
import io.jackey.PipeliningBase;
import io.jackey.Protocol;
import io.jackey.Response;
import io.jackey.args.Rawable;
import io.jackey.commands.ProtocolCommand;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * {@link PipeliningBase} tests that don't really fall into any category of commands.
 */
public class PipeliningBaseMiscellaneousTest extends PipeliningBaseMockedTestBase {

  @Test
  public void testSendCommandWithStringArgs() {
    ProtocolCommand cmd = Protocol.Command.GET;
    String arg1 = "key1";
    String arg2 = "key2";

    Response<Object> response = pipeliningBase.sendCommand(cmd, arg1, arg2);

    MatcherAssert.assertThat(commands, hasSize(1));

    List<Rawable> arguments = new ArrayList<>();
    commands.get(0).getArguments().forEach(arguments::add);

    assertThat(arguments.stream().map(Rawable::getRaw).collect(Collectors.toList()), contains(
        Protocol.Command.GET.getRaw(),
        arg1.getBytes(),
        arg2.getBytes()
    ));

    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testSendCommandWithByteArgs() {
    ProtocolCommand cmd = Protocol.Command.SET;
    byte[] arg1 = "key1".getBytes();
    byte[] arg2 = "value1".getBytes();

    Response<Object> response = pipeliningBase.sendCommand(cmd, arg1, arg2);

    MatcherAssert.assertThat(commands, hasSize(1));

    List<Rawable> arguments = new ArrayList<>();
    commands.get(0).getArguments().forEach(arguments::add);

    assertThat(arguments.stream().map(Rawable::getRaw).collect(Collectors.toList()), contains(
        Protocol.Command.SET.getRaw(),
        arg1,
        arg2
    ));

    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testExecuteCommand() {
    CommandArguments commandArguments = new CommandArguments(Protocol.Command.GET).key("key1");
    CommandObject<String> commandObject = new CommandObject<>(commandArguments, BuilderFactory.STRING);

    Response<String> response = pipeliningBase.executeCommand(commandObject);

    MatcherAssert.assertThat(commands, contains(commandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testMultipleCommands() {
    when(commandObjects.exists("key1")).thenReturn(booleanCommandObject);
    when(commandObjects.exists("key2")).thenReturn(booleanCommandObject);

    Response<Boolean> result1 = pipeliningBase.exists("key1");
    Response<Boolean> result2 = pipeliningBase.exists("key2");

    MatcherAssert.assertThat(commands, Matchers.contains(
        booleanCommandObject,
        booleanCommandObject
    ));

    assertThat(result1, Matchers.is(predefinedResponse));
    assertThat(result2, Matchers.is(predefinedResponse));
  }

}
