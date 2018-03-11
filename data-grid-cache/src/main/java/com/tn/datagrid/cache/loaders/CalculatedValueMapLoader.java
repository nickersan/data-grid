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

import com.tn.datagrid.core.domain.CalculatedIdentity;
import com.tn.datagrid.core.domain.Identity;
import com.tn.datagrid.core.domain.Value;

public class CalculatedValueMapLoader<T, V extends Value<T, V>, RT, RV extends Value<RT, RV>> implements MapLoader<CalculatedIdentity<T, V, RT, RV>, RV>
{
  private HazelcastInstance hazelcastInstance;

  public CalculatedValueMapLoader()
  {
    this.hazelcastInstance = HazelcastClient.newHazelcastClient();
  }

  @Override
  public RV load(CalculatedIdentity<T, V, RT, RV> identity)
  {
    return identity.getOperator().apply(
      identity,
      getValue(identity.getLeftIdentity()),
      getValue(identity.getRightIdentity())
    );
  }

  private V getValue(Identity<T, V> identity)
  {
    if (identity instanceof CalculatedIdentity)
    {
      IMap<Identity, V> calculatedIntegers = hazelcastInstance.getMap("calculated.integers");
      return calculatedIntegers.get(identity);
    }
    else
    {
      IMap<Identity, V> primaryIntegers = hazelcastInstance.getMap("primary.integers");
      return primaryIntegers.get(identity);
    }
  }

  @Override
  public Map<CalculatedIdentity<T, V, RT, RV>, RV> loadAll(Collection<CalculatedIdentity<T, V, RT, RV>> collection)
  {
    return collection.stream()
      .map(this::load)
      .filter(Objects::nonNull)
      .collect(toMap((value) -> (CalculatedIdentity<T, V, RT, RV>)value.getIdentity(), identity()));
  }

  @Override
  public Iterable<CalculatedIdentity<T, V, RT, RV>> loadAllKeys()
  {
    return Collections.emptyList();
  }
}
