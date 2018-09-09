package com.tn.datagrid.acceptance.steps;

import static java.util.Collections.*;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tn.datagrid.core.domain.identity.Identity;
import com.tn.datagrid.core.util.StringUtils;

public abstract class AbstractSteps
{
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private static Map<Object, Identity> identities = new HashMap<>();

  protected static Identity getIdentity(Object value)
  {
    return identities.get(value);
  }

  protected static void setIdentity(Object value, Identity identity)
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

  protected Collection<String> splitList(String s)
  {
    return Stream.of(s.split(StringUtils.COMMA))
      .filter((token) -> !token.isEmpty())
      .map((token) -> token.split(StringUtils.AMPERSAND))
      .flatMap(Stream::of)
      .filter((token) -> !token.isEmpty())
      .map(String::trim)
      .collect(Collectors.toList());
  }
}
