package com.tn.datagrid.cao;

import java.util.Optional;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tn.datagrid.core.domain.CalculatedIdentity;
import com.tn.datagrid.core.domain.Identity;

public class CalculatedValueGetter<T> implements ValueGetter<T>
{
  private static Logger logger = LoggerFactory.getLogger(CalculatedValueGetter.class);

  private HazelcastInstance hazelcastInstance;

  public CalculatedValueGetter(HazelcastInstance hazelcastInstance)
  {
    this.hazelcastInstance = hazelcastInstance;
  }

  @Override
  public Optional<T> get(Identity identity)
  {
    return Optional.ofNullable(getValue(identity));
  }

  private <T1> T1 getValue(Identity identity)
  {
    logger.debug("Getting value for: {}", identity);

    T1 value;

    if (identity instanceof CalculatedIdentity)
    {
      IMap<Identity, T1> calculatedIntegers = hazelcastInstance.getMap("calculated.integers");
      value = calculatedIntegers.get(identity);

      if (value == null)
      {
        @SuppressWarnings("unchecked")
        CalculatedIdentity<T1, ?, ?> calculatedIdentity = (CalculatedIdentity)identity;

        logger.debug("Calculating value for: {}", calculatedIdentity);

        value = calculatedIdentity.getOperator().apply(
          getValue(calculatedIdentity.getLeftIdentity()),
          getValue(calculatedIdentity.getRightIdentity())
        );

        logger.debug("Calculating value: {} for: {}", value, calculatedIdentity);

        calculatedIntegers.put(identity, value);
      }
    }
    else
    {
      IMap<Identity, T1> primaryIntegers = hazelcastInstance.getMap("primary.integers");
      value =  primaryIntegers.get(identity);
    }

    logger.debug("Got value: {}", value);

    return value;
  }
}
