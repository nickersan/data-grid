package com.tn.datagrid.core.domain;

import static java.util.Objects.hash;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.io.Serializable;

public class Type<T, V extends Value<T, V>> implements Serializable
{
  private Class<V> valueType;
  private String name;

  public Type(Class<V> valueType, String name)
  {
    this.valueType = valueType;
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

  public Class<V> getValueType()
  {
    return valueType;
  }

  public boolean equals(Object other)
  {
    return this == other || (
      other != null &&
      getClass().equals(other.getClass()) &&
      getValueType().equals(((Type)other).getValueType()) &&
      getName().equals(((Type)other).getName())
    );
  }

  public int hashCode()
  {
    return hash(getValueType(), getName());
  }

  public String toString()
  {
    return toStringHelper(this)
      .add("valueType", getValueType())
      .add("name", getName())
      .toString();
  }
}
