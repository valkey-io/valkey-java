package io.valkey.params;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.valkey.CommandArguments;
import io.valkey.Protocol.Keyword;
import io.valkey.util.KeyValue;

public class ModuleLoadExParams implements IParams {

  private final List<KeyValue<String, String>> configs = new ArrayList<>();
  private final List<String> args = new ArrayList<>();

  public ModuleLoadExParams() {
  }

  public ModuleLoadExParams moduleLoadexParams() {
    return new ModuleLoadExParams();
  }

  public ModuleLoadExParams config(String name, String value) {
    this.configs.add(KeyValue.of(name, value));
    return this;
  }

  public ModuleLoadExParams arg(String arg) {
    this.args.add(arg);
    return this;
  }

  @Override
  public void addParams(CommandArguments args) {

    this.configs.forEach(kv -> args.add(Keyword.CONFIG).add(kv.getKey()).add(kv.getValue()));

    if (!this.args.isEmpty()) {
      args.add(Keyword.ARGS).addObjects(this.args);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ModuleLoadExParams that = (ModuleLoadExParams) o;
    return Objects.equals(configs, that.configs) && Objects.equals(args, that.args);
  }

  @Override
  public int hashCode() {
    return Objects.hash(configs, args);
  }
}
