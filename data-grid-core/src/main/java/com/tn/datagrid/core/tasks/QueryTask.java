package com.tn.datagrid.core.tasks;

import static java.util.stream.Collectors.toMap;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tn.datagrid.core.domain.CalculatedIdentity;
import com.tn.datagrid.core.domain.Identity;

public class QueryTask<T> implements Callable<Map<Identity, T>>, HazelcastInstanceAware, Serializable
{
  private static Logger logger = LoggerFactory.getLogger(QueryTask.class);

  private transient HazelcastInstance hazelcastInstance;

  private Collection<Identity> identities;

  public QueryTask(Collection<Identity> identities)
  {
    this.identities = identities;
  }

  @Override
  public void setHazelcastInstance(HazelcastInstance hazelcastInstance)
  {
    this.hazelcastInstance = hazelcastInstance;
  }

  @Override
  public Map<Identity, T> call()
  {
    Map<Identity, Object> knownValues = loadKnownValues(identities);
    return identities.parallelStream().collect(toMap(Function.identity(), (identity) -> resolveValue(identity, knownValues)));
  }

  private Map<Identity, Object> loadKnownValues(Collection<Identity> identities)
  {
    Map<Identity, Object> knownValues = new ConcurrentHashMap<>();

    Map<String, Set<Identity>> identitiesByLocation = new HashMap<>();
    identities.forEach((identity) -> groupIdentitiesByLocation(identity, identitiesByLocation));

    for (Map.Entry<String, Set<Identity>> identitiesForLocation : identitiesByLocation.entrySet())
    {
      IMap<Identity, Object> map = this.hazelcastInstance.getMap(identitiesForLocation.getKey());
      knownValues.putAll(map.getAll(identitiesForLocation.getValue()));
    }

    return knownValues;
  }

  private void groupIdentitiesByLocation(Identity identity, Map<String, Set<Identity>> identitiesByLocation)
  {
    identitiesByLocation.computeIfAbsent(identity.getLocation(), (location) -> new HashSet<>()).add(identity);

    if (identity instanceof CalculatedIdentity)
    {
      CalculatedIdentity calculatedIdentity = (CalculatedIdentity)identity;
      groupIdentitiesByLocation(calculatedIdentity.getLeftIdentity(), identitiesByLocation);
      groupIdentitiesByLocation(calculatedIdentity.getRightIdentity(), identitiesByLocation);
    }
  }

  private <T1> T1 resolveValue(Identity identity, Map<Identity, Object> knownValues)
  {
    logger.trace("Resolving value for: {}", identity);

    T1 value;

    if (identity instanceof CalculatedIdentity)
    {
      //noinspection unchecked
      value = (T1)knownValues.get(identity);

      if (value == null)
      {
        @SuppressWarnings("unchecked")
        CalculatedIdentity<T1, ?, ?> calculatedIdentity = (CalculatedIdentity)identity;

        logger.trace("Calculating value for: {}", calculatedIdentity);

        value = calculatedIdentity.getOperator().apply(
          resolveValue(calculatedIdentity.getLeftIdentity(), knownValues),
          resolveValue(calculatedIdentity.getRightIdentity(), knownValues)
        );

        knownValues.put(identity, value);
        hazelcastInstance.getMap(identity.getLocation()).putAsync(identity, value);
      }
    }
    else
    {
      //noinspection unchecked
      value = (T1)knownValues.get(identity);
    }

    logger.trace("Got value: {} for: {}", value, identity);

    return value;
  }
}
