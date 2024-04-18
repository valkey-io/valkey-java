package io.jackey.mocked.pipeline;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import io.jackey.Response;
import io.jackey.args.FlushMode;
import io.jackey.args.FunctionRestorePolicy;
import io.jackey.resps.FunctionStats;
import io.jackey.resps.LibraryInfo;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public class PipeliningBaseScriptingAndFunctionsCommandsTest extends PipeliningBaseMockedTestBase {

  @Test
  public void testEval() {
    String script = "return 'Hello, world!'";
    when(commandObjects.eval(script)).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.eval(script);

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testEvalBinary() {
    byte[] script = "return 'Hello, world!'".getBytes();

    when(commandObjects.eval(script)).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.eval(script);

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testEvalWithParams() {
    String script = "return KEYS[1] .. ARGV[1]";
    int keyCount = 1;

    when(commandObjects.eval(script, keyCount, "key", "arg")).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.eval(script, keyCount, "key", "arg");

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testEvalWithParamsBinary() {
    byte[] script = "return KEYS[1]".getBytes();
    int keyCount = 1;
    byte[] param1 = "key1".getBytes();

    when(commandObjects.eval(script, keyCount, param1)).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.eval(script, keyCount, param1);

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testEvalWithLists() {
    String script = "return KEYS[1] .. ARGV[1]";
    List<String> keys = Collections.singletonList("key");
    List<String> args = Collections.singletonList("arg");

    when(commandObjects.eval(script, keys, args)).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.eval(script, keys, args);

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testEvalWithListsBinary() {
    byte[] script = "return {KEYS[1], ARGV[1]}".getBytes();
    List<byte[]> keys = Collections.singletonList("key1".getBytes());
    List<byte[]> args = Collections.singletonList("arg1".getBytes());

    when(commandObjects.eval(script, keys, args)).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.eval(script, keys, args);

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testEvalWithSampleKey() {
    String script = "return 'Hello, world!'";

    when(commandObjects.eval(script, "key")).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.eval(script, "key");

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testEvalWithSampleKeyBinary() {
    byte[] script = "return 'Hello, world!'".getBytes();
    byte[] sampleKey = "sampleKey".getBytes();

    when(commandObjects.eval(script, sampleKey)).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.eval(script, sampleKey);

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testEvalReadonly() {
    String script = "return KEYS[1] .. ARGV[1]";
    List<String> keys = Collections.singletonList("key");
    List<String> args = Collections.singletonList("arg");

    when(commandObjects.evalReadonly(script, keys, args)).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.evalReadonly(script, keys, args);

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testEvalReadonlyBinary() {
    byte[] script = "return {KEYS[1], ARGV[1]}".getBytes();
    List<byte[]> keys = Collections.singletonList("key1".getBytes());
    List<byte[]> args = Collections.singletonList("arg1".getBytes());

    when(commandObjects.evalReadonly(script, keys, args)).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.evalReadonly(script, keys, args);

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testEvalsha() {
    String sha1 = "somehash";

    when(commandObjects.evalsha(sha1)).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.evalsha(sha1);

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testEvalshaBinary() {
    byte[] sha1 = "abcdef1234567890".getBytes();

    when(commandObjects.evalsha(sha1)).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.evalsha(sha1);

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testEvalshaWithParams() {
    String sha1 = "somehash";
    int keyCount = 1;

    when(commandObjects.evalsha(sha1, keyCount, "key", "arg")).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.evalsha(sha1, keyCount, "key", "arg");

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testEvalshaWithParamsBinary() {
    byte[] sha1 = "abcdef1234567890".getBytes();
    int keyCount = 1;
    byte[] param1 = "key1".getBytes();

    when(commandObjects.evalsha(sha1, keyCount, param1)).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.evalsha(sha1, keyCount, param1);

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testEvalshaWithLists() {
    String sha1 = "somehash";
    List<String> keys = Collections.singletonList("key");
    List<String> args = Collections.singletonList("arg");

    when(commandObjects.evalsha(sha1, keys, args)).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.evalsha(sha1, keys, args);

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testEvalshaWithListsBinary() {
    byte[] sha1 = "abcdef1234567890".getBytes();
    List<byte[]> keys = Collections.singletonList("key1".getBytes());
    List<byte[]> args = Collections.singletonList("arg1".getBytes());

    when(commandObjects.evalsha(sha1, keys, args)).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.evalsha(sha1, keys, args);

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testEvalshaWithSampleKey() {
    String sha1 = "somehash";

    when(commandObjects.evalsha(sha1, "key")).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.evalsha(sha1, "key");

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testEvalshaWithSampleKeyBinary() {
    byte[] sha1 = "abcdef1234567890".getBytes();
    byte[] sampleKey = "sampleKey".getBytes();

    when(commandObjects.evalsha(sha1, sampleKey)).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.evalsha(sha1, sampleKey);

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testEvalshaReadonly() {
    String sha1 = "somehash";
    List<String> keys = Collections.singletonList("key");
    List<String> args = Collections.singletonList("arg");

    when(commandObjects.evalshaReadonly(sha1, keys, args)).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.evalshaReadonly(sha1, keys, args);

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testEvalshaReadonlyBinary() {
    byte[] sha1 = "abcdef1234567890".getBytes();
    List<byte[]> keys = Collections.singletonList("key1".getBytes());
    List<byte[]> args = Collections.singletonList("arg1".getBytes());

    when(commandObjects.evalshaReadonly(sha1, keys, args)).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.evalshaReadonly(sha1, keys, args);

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testFcall() {
    String name = "functionName";
    List<String> keys = Collections.singletonList("key");
    List<String> args = Collections.singletonList("arg");

    when(commandObjects.fcall(name, keys, args)).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.fcall(name, keys, args);

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testFcallBinary() {
    byte[] name = "functionName".getBytes();
    List<byte[]> keys = Collections.singletonList("key".getBytes());
    List<byte[]> args = Collections.singletonList("arg".getBytes());

    when(commandObjects.fcall(name, keys, args)).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.fcall(name, keys, args);

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testFcallReadonly() {
    String name = "functionName";
    List<String> keys = Collections.singletonList("key");
    List<String> args = Collections.singletonList("arg");

    when(commandObjects.fcallReadonly(name, keys, args)).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.fcallReadonly(name, keys, args);

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testFcallReadonlyBinary() {
    byte[] name = "functionName".getBytes();
    List<byte[]> keys = Collections.singletonList("key".getBytes());
    List<byte[]> args = Collections.singletonList("arg".getBytes());

    when(commandObjects.fcallReadonly(name, keys, args)).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.fcallReadonly(name, keys, args);

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testFunctionDelete() {
    String libraryName = "libraryName";

    when(commandObjects.functionDelete(libraryName)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.functionDelete(libraryName);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testFunctionDeleteBinary() {
    byte[] libraryName = "libraryName".getBytes();

    when(commandObjects.functionDelete(libraryName)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.functionDelete(libraryName);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testFunctionDump() {
    when(commandObjects.functionDump()).thenReturn(bytesCommandObject);

    Response<byte[]> response = pipeliningBase.functionDump();

    MatcherAssert.assertThat(commands, Matchers.contains(bytesCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testFunctionFlush() {
    when(commandObjects.functionFlush()).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.functionFlush();

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testFunctionFlushWithMode() {
    FlushMode mode = FlushMode.SYNC;

    when(commandObjects.functionFlush(mode)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.functionFlush(mode);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testFunctionKill() {
    when(commandObjects.functionKill()).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.functionKill();

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testFunctionList() {
    when(commandObjects.functionList()).thenReturn(listLibraryInfoCommandObject);

    Response<List<LibraryInfo>> response = pipeliningBase.functionList();

    MatcherAssert.assertThat(commands, Matchers.contains(listLibraryInfoCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testFunctionListBinary() {
    when(commandObjects.functionListBinary()).thenReturn(listObjectCommandObject);

    Response<List<Object>> response = pipeliningBase.functionListBinary();

    MatcherAssert.assertThat(commands, Matchers.contains(listObjectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testFunctionListWithPattern() {
    String libraryNamePattern = "lib*";

    when(commandObjects.functionList(libraryNamePattern)).thenReturn(listLibraryInfoCommandObject);

    Response<List<LibraryInfo>> response = pipeliningBase.functionList(libraryNamePattern);

    MatcherAssert.assertThat(commands, Matchers.contains(listLibraryInfoCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testFunctionListWithPatternBinary() {
    byte[] libraryNamePattern = "lib*".getBytes();

    when(commandObjects.functionList(libraryNamePattern)).thenReturn(listObjectCommandObject);

    Response<List<Object>> response = pipeliningBase.functionList(libraryNamePattern);

    MatcherAssert.assertThat(commands, Matchers.contains(listObjectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testFunctionListWithCode() {
    when(commandObjects.functionListWithCode()).thenReturn(listLibraryInfoCommandObject);

    Response<List<LibraryInfo>> response = pipeliningBase.functionListWithCode();

    MatcherAssert.assertThat(commands, Matchers.contains(listLibraryInfoCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testFunctionListWithCodeBinary() {
    when(commandObjects.functionListWithCodeBinary()).thenReturn(listObjectCommandObject);

    Response<List<Object>> response = pipeliningBase.functionListWithCodeBinary();

    MatcherAssert.assertThat(commands, Matchers.contains(listObjectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testFunctionListWithCodeAndPattern() {
    String libraryNamePattern = "lib*";

    when(commandObjects.functionListWithCode(libraryNamePattern)).thenReturn(listLibraryInfoCommandObject);

    Response<List<LibraryInfo>> response = pipeliningBase.functionListWithCode(libraryNamePattern);

    MatcherAssert.assertThat(commands, Matchers.contains(listLibraryInfoCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testFunctionListWithCodeAndPatternBinary() {
    byte[] libraryNamePattern = "lib*".getBytes();

    when(commandObjects.functionListWithCode(libraryNamePattern)).thenReturn(listObjectCommandObject);

    Response<List<Object>> response = pipeliningBase.functionListWithCode(libraryNamePattern);

    MatcherAssert.assertThat(commands, Matchers.contains(listObjectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testFunctionLoad() {
    String functionCode = "return 'Hello, world!'";

    when(commandObjects.functionLoad(functionCode)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.functionLoad(functionCode);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testFunctionLoadBinary() {
    byte[] functionCode = "return 'Hello, world!'".getBytes();

    when(commandObjects.functionLoad(functionCode)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.functionLoad(functionCode);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testFunctionLoadReplace() {
    String functionCode = "return 'Hello, world!'";

    when(commandObjects.functionLoadReplace(functionCode)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.functionLoadReplace(functionCode);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testFunctionLoadReplaceBinary() {
    byte[] functionCode = "return 'Hello, world!'".getBytes();

    when(commandObjects.functionLoadReplace(functionCode)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.functionLoadReplace(functionCode);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testFunctionRestore() {
    byte[] serializedValue = "serialized".getBytes();

    when(commandObjects.functionRestore(serializedValue)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.functionRestore(serializedValue);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testFunctionRestoreWithPolicy() {
    byte[] serializedValue = "serialized".getBytes();
    FunctionRestorePolicy policy = FunctionRestorePolicy.FLUSH;

    when(commandObjects.functionRestore(serializedValue, policy)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.functionRestore(serializedValue, policy);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testFunctionStats() {
    when(commandObjects.functionStats()).thenReturn(functionStatsCommandObject);

    Response<FunctionStats> response = pipeliningBase.functionStats();

    MatcherAssert.assertThat(commands, Matchers.contains(functionStatsCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testFunctionStatsBinary() {
    when(commandObjects.functionStatsBinary()).thenReturn(objectCommandObject);

    Response<Object> response = pipeliningBase.functionStatsBinary();

    MatcherAssert.assertThat(commands, Matchers.contains(objectCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testScriptExistsWithKeyAndSha1s() {
    String[] sha1 = { "somehash1", "somehash2" };

    when(commandObjects.scriptExists("key", sha1)).thenReturn(listBooleanCommandObject);

    Response<List<Boolean>> response = pipeliningBase.scriptExists("key", sha1);

    MatcherAssert.assertThat(commands, Matchers.contains(listBooleanCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testScriptExistsWithKeyAndSha1sBinary() {
    byte[] sampleKey = "sampleKey".getBytes();
    byte[] sha1 = "abcdef1234567890".getBytes();

    when(commandObjects.scriptExists(sampleKey, sha1)).thenReturn(listBooleanCommandObject);

    Response<List<Boolean>> response = pipeliningBase.scriptExists(sampleKey, sha1);

    MatcherAssert.assertThat(commands, Matchers.contains(listBooleanCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testScriptFlush() {
    when(commandObjects.scriptFlush("key")).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.scriptFlush("key");

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testScriptFlushBinary() {
    byte[] sampleKey = "sampleKey".getBytes();

    when(commandObjects.scriptFlush(sampleKey)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.scriptFlush(sampleKey);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testScriptFlushWithMode() {
    FlushMode flushMode = FlushMode.SYNC;

    when(commandObjects.scriptFlush("key", flushMode)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.scriptFlush("key", flushMode);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testScriptFlushWithModeBinary() {
    byte[] sampleKey = "sampleKey".getBytes();
    FlushMode flushMode = FlushMode.SYNC;

    when(commandObjects.scriptFlush(sampleKey, flushMode)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.scriptFlush(sampleKey, flushMode);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testScriptKill() {
    when(commandObjects.scriptKill("key")).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.scriptKill("key");

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testScriptKillBinary() {
    byte[] sampleKey = "sampleKey".getBytes();

    when(commandObjects.scriptKill(sampleKey)).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.scriptKill(sampleKey);

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testScriptLoad() {
    String script = "return 'Hello, world!'";

    when(commandObjects.scriptLoad(script, "key")).thenReturn(stringCommandObject);

    Response<String> response = pipeliningBase.scriptLoad(script, "key");

    MatcherAssert.assertThat(commands, Matchers.contains(stringCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

  @Test
  public void testScriptLoadBinary() {
    byte[] script = "return 'Hello, world!'".getBytes();
    byte[] sampleKey = "sampleKey".getBytes();

    when(commandObjects.scriptLoad(script, sampleKey)).thenReturn(bytesCommandObject);

    Response<byte[]> response = pipeliningBase.scriptLoad(script, sampleKey);

    MatcherAssert.assertThat(commands, Matchers.contains(bytesCommandObject));
    assertThat(response, Matchers.is(predefinedResponse));
  }

}
