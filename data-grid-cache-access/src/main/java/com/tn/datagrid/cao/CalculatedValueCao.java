package com.tn.datagrid.cao;

import static java.util.Collections.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tn.datagrid.core.domain.CalculatedIdentity;
import com.tn.datagrid.core.domain.Identity;

public class CalculatedValueCao<T> implements ValueGetter<T>
{
  private static Logger logger = LoggerFactory.getLogger(CalculatedValueCao.class);

  private HazelcastInstance hazelcastInstance;

  public CalculatedValueCao(HazelcastInstance hazelcastInstance)
  {
    this.hazelcastInstance = hazelcastInstance;
  }

  @Override
  public Optional<T> get(Identity identity)
  {
    Map<Identity, Object> knownValues = loadKnownValues(singleton(identity));
    return Optional.ofNullable(resolveValue(identity, knownValues));
  }

  @Override
  public Map<Identity, T> getAll(Collection<Identity> identities)
  {
    Map<Identity, Object> knownValues = loadKnownValues(identities);
    return identities.stream().collect(Collectors.toMap(Function.identity(), (identity) -> resolveValue(identity, knownValues)));
  }

  private Map<Identity, Object> loadKnownValues(Collection<Identity> identities)
  {
    Map<Identity, Object> knownValues = new HashMap<>();

    Map<String, Set<Identity>> identitiesByLocation = new HashMap<>();
    identities.forEach((identity) -> groupIdentitiesByLocation(identity, identitiesByLocation));

    for (Map.Entry<String, Set<Identity>> identitiesForLocation : identitiesByLocation.entrySet())
    {
      knownValues.putAll(this.hazelcastInstance.<Identity, Object>getMap(identitiesForLocation.getKey()).getAll(identitiesForLocation.getValue()));
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
      //TODO: handle missing primary value - Nick Holt 2018/3/22

      //noinspection unchecked
      value = (T1)knownValues.get(identity);
    }

    logger.trace("Got value: {} for: {}", value, identity);

    return value;
  }
}
