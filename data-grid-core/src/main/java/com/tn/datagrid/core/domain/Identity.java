package com.tn.datagrid.core.domain;

import java.io.Serializable;

public abstract class Identity<T, V extends Value<T, V>> implements Serializable
{
  private Type<T, V> type;

  public Identity(Type<T, V> type)
  {
    this.type = type;
  }

  public Type<T, V> getType()
  {
    return type;
  }

  public abstract boolean equals(Object other);

  public abstract int hashCode();

  public abstract String toString();
}
