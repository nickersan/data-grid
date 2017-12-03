package com.tn.datagrid.cache.stores;

import java.util.HashMap;
import java.util.Map;
import javax.json.JsonObject;

import com.hazelcast.core.MapLoader;

import com.tn.datagrid.core.domain.ChildIdentity;
import com.tn.datagrid.core.domain.Identity;
import com.tn.datagrid.core.domain.IntValue;
import com.tn.datagrid.core.domain.NumericIdentity;
import com.tn.datagrid.core.domain.StringValue;
import com.tn.datagrid.core.domain.Type;
import com.tn.datagrid.core.domain.Value;

public abstract class AbstractMapStore<T, V extends Value<T, V>> implements MapLoader<Identity<T, V>, V>
{
  protected static final String JSON_ID = "id";
  protected static final String JSON_IDENTITY = "identity";
  protected static final String JSON_NAME = "name";
  protected static final String JSON_PARENT = "parent";
  protected static final String JSON_TYPE = "type";
  protected static final String JSON_VALUE = "value";
  protected static final String JSON_VALUE_TYPE = "valueType";

  private static final Map<String, Class<? extends Value<?, ?>>> VALUE_TYPES = new HashMap<>();
  static
  {
    VALUE_TYPES.put("int", IntValue.class);
    VALUE_TYPES.put("string", StringValue.class);
  }

  protected Identity<T, V> toIdentity(JsonObject identity)
  {
    JsonObject type = identity.getJsonObject(JSON_TYPE);
    JsonObject parent = identity.getJsonObject(JSON_PARENT);

    if (parent != null)
    {
      return new ChildIdentity<>(toType(type), identity.getInt(JSON_ID), toIdentity(parent));
    }
    else
    {
      return new NumericIdentity<>(toType(type), identity.getInt(JSON_ID));
    }
  }

  private Type<T, V> toType(JsonObject object)
  {
    @SuppressWarnings("unchecked")
    Class<V> valueType = (Class<V>)VALUE_TYPES.get(object.getString(JSON_VALUE_TYPE));

    if (valueType == null)
    {
      throw new IllegalStateException("Unknown value type: " + object.getString(JSON_VALUE_TYPE));
    }

    return new Type<>(valueType, object.getString(JSON_NAME));
  }
}
