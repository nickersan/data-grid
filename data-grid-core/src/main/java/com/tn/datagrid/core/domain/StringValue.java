package com.tn.datagrid.core.domain;

public class StringValue extends ObjectValue<String, StringValue>
{
  public StringValue(Identity<String, StringValue> identity, String value)
  {
    super(identity, value);
  }

  public static TypeFactory<String, StringValue> newType(String name)
  {
    return () -> new Type<>(StringValue.class, name);
  }
}
