package com.tn.datagrid.core.services;

import com.hazelcast.core.DistributedObject;

import com.tn.datagrid.core.domain.CalculatedIdentity;

public interface Calculation<T> extends DistributedObject
{
  public T perform(CalculatedIdentity<T, ?, ?> calculatedIdentity);
}
