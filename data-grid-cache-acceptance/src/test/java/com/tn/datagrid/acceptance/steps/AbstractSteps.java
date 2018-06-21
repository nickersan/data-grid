package com.tn.datagrid.acceptance.steps;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSteps
{
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  protected void fail(String message)
  {
    logger.error(message);
    Assert.fail(message);
  }

  protected void fail(String message, Throwable e)
  {
    logger.error(message, e);
    Assert.fail(message);
  }
}
