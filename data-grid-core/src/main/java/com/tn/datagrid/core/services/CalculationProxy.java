package com.tn.datagrid.core.services;

import com.hazelcast.spi.AbstractDistributedObject;
import com.hazelcast.spi.NodeEngine;

import com.tn.datagrid.core.domain.CalculatedIdentity;

public class CalculationProxy<T> extends AbstractDistributedObject<CalculationService> implements Calculation<T>
{
  public CalculationProxy(NodeEngine nodeEngine, CalculationService service)
  {
    super(nodeEngine, service);
  }

  @Override
  public String getName()
  {
    return null;
  }

  @Override
  public String getServiceName()
  {
    return null;
  }

  @Override
  public T perform(CalculatedIdentity<T, ?, ?> calculatedIdentity)
  {
    return null;
  }
}
