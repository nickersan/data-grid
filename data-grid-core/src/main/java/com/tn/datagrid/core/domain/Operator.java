package com.tn.datagrid.core.domain;

public interface Operator<T, V extends Value<T, V>, RT, RV extends Value<RT, RV>>
{
  public RV apply(CalculatedIdentity<T, V, RT, RV> resultIdentity, V left, V right);
}
