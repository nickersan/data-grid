package com.tn.datagrid.cache.loaders;

import static java.util.stream.Collectors.toMap;

import static com.google.common.base.Functions.identity;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MapLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tn.datagrid.core.domain.CalculatedIdentity;
import com.tn.datagrid.core.domain.Identity;
import com.tn.datagrid.core.domain.Value;

public class CalculatedValueMapLoader<T, V extends Value<T, V>, LT, LV extends Value<LT, LV>, RT, RV extends Value<RT, RV>> implements MapLoader<CalculatedIdentity<T, V, LT, LV, RT, RV>, V>
{
  private static Logger logger = LoggerFactory.getLogger(CalculatedValueMapLoader.class);

  private HazelcastInstance hazelcastInstance;

  public CalculatedValueMapLoader()
  {
    this.hazelcastInstance = HazelcastClient.newHazelcastClient();
  }

  @Override
  public V load(CalculatedIdentity<T, V, LT, LV, RT, RV> identity)
  {
    logger.debug("Loading calculated value: {}", identity);

    return identity.getOperator().apply(
      identity,
      getValue(identity.getLeftIdentity()),
      getValue(identity.getRightIdentity())
    );
  }

  private <T1, V1 extends Value<T1, V1>> V1 getValue(Identity<T1, V1> identity)
  {
    logger.debug("Getting value for: {}", identity);

    V1 value;

    if (identity instanceof CalculatedIdentity)
    {
      IMap<Identity, V1> calculatedIntegers = hazelcastInstance.getMap("calculated.integers");
      value = calculatedIntegers.get(identity);
    }
    else
    {
      IMap<Identity, V1> primaryIntegers = hazelcastInstance.getMap("primary.integers");
      value =  primaryIntegers.get(identity);
    }

    logger.debug("Got value: {}", value);

    return value;
  }

  @Override
  public Map<CalculatedIdentity<T, V, LT, LV, RT, RV>, V> loadAll(Collection<CalculatedIdentity<T, V, LT, LV, RT, RV>> collection)
  {
    return collection.stream()
      .map(this::load)
      .filter(Objects::nonNull)
      .collect(toMap((value) -> (CalculatedIdentity<T, V, LT, LV, RT, RV>)value.getIdentity(), identity()));
  }

  @Override
  public Iterable<CalculatedIdentity<T, V, LT, LV, RT, RV>> loadAllKeys()
  {
    return Collections.emptyList();
  }
}
