package com.tn.datagrid.acceptance.steps;

import static java.util.Collections.*;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tn.datagrid.core.domain.identity.Identity;

public abstract class AbstractSteps
{
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private static Map<String, Identity> identities = new HashMap<>();

  protected static Identity getIdentity(String value)
  {
    return identities.get(value);
  }

  protected static void setIdentity(String value, Identity identity)
  {
    identities.put(value, identity);
  }

  protected static Collection<Identity> getIdentities()
  {
    return unmodifiableSet(Set.copyOf(identities.values()));
  }

  protected static void clearIdentities()
  {
    identities.clear();
  }

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
