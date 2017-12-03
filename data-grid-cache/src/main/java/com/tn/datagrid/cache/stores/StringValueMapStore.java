package com.tn.datagrid.cache.stores;

import static java.util.Collections.*;

import static com.tn.datagrid.core.domain.Value.*;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.tn.datagrid.core.domain.Identity;
import com.tn.datagrid.core.domain.StringValue;

public class StringValueMapStore extends AbstractMapStore<String, StringValue>
{
  private static final String JSON_FILE_STRINGS = "strings.json";

  @Override
  public StringValue load(Identity<String, StringValue> identity)
  {
    return loadAll(singleton(identity)).get(identity);
  }

  @Override
  public Map<Identity<String, StringValue>, StringValue> loadAll(Collection<Identity<String, StringValue>> identities)
  {
    try (JsonReader reader = Json.createReader(ClassLoader.getSystemResourceAsStream(JSON_FILE_STRINGS)))
    {
      return reader.readArray().stream()
        .filter(JsonObject.class::isInstance)
        .map(JsonObject.class::cast)
        .map(this::toStringValue)
        .filter((stringValue) -> identities.contains(stringValue.getIdentity()))
        .collect(byIdentity());
    }
  }

  @Override
  public Iterable<Identity<String, StringValue>> loadAllKeys()
  {
    try (JsonReader reader = Json.createReader(ClassLoader.getSystemResourceAsStream("strings.json")))
    {
      return reader.readArray().stream()
        .filter(JsonObject.class::isInstance)
        .map(JsonObject.class::cast)
        .map((object) -> object.getJsonObject(JSON_IDENTITY))
        .filter(Objects::nonNull)
        .map(this::toIdentity)::iterator;
    }
  }

  private StringValue toStringValue(JsonObject value)
  {
    return new StringValue(toIdentity(value.getJsonObject(JSON_IDENTITY)), value.getString(JSON_VALUE));
  }
}
