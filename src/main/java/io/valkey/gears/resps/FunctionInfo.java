package io.valkey.gears.resps;

import io.valkey.Builder;
import io.valkey.BuilderFactory;
import io.valkey.util.KeyValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionInfo {
  private final String name;
  private final String description;
  private final boolean isAsync;

  private final List<String> flags;

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public boolean isAsync() {
    return isAsync;
  }

  public List<String> getFlags() {
    return flags;
  }

  public FunctionInfo(String name, String description, boolean isAsync, List<String> flags) {
    this.name = name;
    this.description = description;
    this.isAsync = isAsync;
    this.flags = flags;
  }

  public static final Builder<List<FunctionInfo>> FUNCTION_INFO_LIST = new Builder<List<FunctionInfo>>() {
    @Override
    public List<FunctionInfo> build(Object data) {
      List<Object> dataAsList = (List<Object>) data;
      if (!dataAsList.isEmpty()) {
        boolean isListOfList = dataAsList.get(0).getClass().isAssignableFrom(ArrayList.class);

        if (isListOfList) {
          if (((List<List<Object>>)data).get(0).get(0) instanceof KeyValue) {
            List<List<KeyValue>> dataAsKeyValues = (List<List<KeyValue>>)data;
            return dataAsKeyValues.stream().map(keyValues -> {
              String name = null;
              String description = null;
              List<String> flags = Collections.emptyList();
              boolean isAsync = false;
              for (KeyValue kv : keyValues) {
                switch (BuilderFactory.STRING.build(kv.getKey())) {
                  case "name":
                    name = BuilderFactory.STRING.build(kv.getValue());
                    break;
                  case "description":
                    description = BuilderFactory.STRING.build(kv.getValue());
                    break;
                  case "raw-arguments":
                    flags = BuilderFactory.STRING_LIST.build(kv.getValue());
                    break;
                  case "is_async":
                    isAsync = BuilderFactory.BOOLEAN.build(kv.getValue());
                    break;
                }
              }
              return new FunctionInfo(name, description, isAsync, flags);
            }).collect(Collectors.toList());
          } else {
            return dataAsList.stream().map((pairObject) -> (List<Object>) pairObject)
              .map((pairList) -> new FunctionInfo( //
                BuilderFactory.STRING.build(pairList.get(7)),     // name
                BuilderFactory.STRING.build(pairList.get(1)),     // description
                BuilderFactory.BOOLEAN.build(pairList.get(5)),    // is_async
                BuilderFactory.STRING_LIST.build(pairList.get(3)) // flags
              )).collect(Collectors.toList());
          }
        } else {
          return dataAsList.stream() //
            .map(BuilderFactory.STRING::build) //
            .map((name) -> new FunctionInfo(name, null, false, null)) //
            .collect(Collectors.toList());
        }
      } else {
        return Collections.emptyList();
      }
    }
  };
}

