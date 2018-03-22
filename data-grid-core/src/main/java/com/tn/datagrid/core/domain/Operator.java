package com.tn.datagrid.core.domain;

public interface Operator<T, LT, RT>
{
  public T apply(LT left, RT right);
}
