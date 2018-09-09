package com.tn.datagrid.core.domain;

import java.util.Collection;

@FunctionalInterface
public interface AggregateOperator<T>
{
  T apply(Collection<T> values);
}
