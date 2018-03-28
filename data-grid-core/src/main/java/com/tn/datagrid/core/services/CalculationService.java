package com.tn.datagrid.core.services;

import java.util.Properties;

import com.hazelcast.core.DistributedObject;
import com.hazelcast.spi.ManagedService;
import com.hazelcast.spi.NodeEngine;
import com.hazelcast.spi.RemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalculationService implements ManagedService, RemoteService
{
  private static Logger logger = LoggerFactory.getLogger(CalculationService.class);

  @Override
  public void init(NodeEngine nodeEngine, Properties properties)
  {
    logger.info("Initializing");
  }

  @Override
  public void reset()
  {
    logger.info("Reset");
  }

  @Override
  public void shutdown(boolean terminate)
  {
    logger.info("Shutdown");
  }

  @Override
  public DistributedObject createDistributedObject(String objectName)
  {
    return null;
  }

  @Override
  public void destroyDistributedObject(String objectName)
  {

  }
}
