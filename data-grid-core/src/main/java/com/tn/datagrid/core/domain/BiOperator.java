package com.tn.datagrid.core.domain;

@FunctionalInterface
public interface BiOperator<T, LT, RT>
{
  T apply(LT left, RT right);
}
