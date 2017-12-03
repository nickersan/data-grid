package com.tn.datagrid.core.domain;

public interface TypeFactory<T, V extends Value<T, V>>
{
  public Type<T, V> build();
}
