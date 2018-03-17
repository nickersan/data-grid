package com.tn.datagrid.core.domain;

public interface Operator<T, V extends Value<T, V>, LT, LV extends Value<LT, LV>, RT, RV extends Value<RT, RV>>
{
  public V apply(CalculatedIdentity<T, V, LT, LV, RT, RV> resultIdentity, LV left, RV right);
}
