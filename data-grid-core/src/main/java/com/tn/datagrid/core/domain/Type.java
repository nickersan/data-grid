package com.tn.datagrid.core.domain;

import static java.util.Objects.*;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

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
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("valueType", getValueType())
      .append("name", getName())
      .toString();
  }
}
