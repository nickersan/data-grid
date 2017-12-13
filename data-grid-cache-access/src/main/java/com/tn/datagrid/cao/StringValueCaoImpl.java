package com.tn.datagrid.cao;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import com.tn.datagrid.core.domain.Identity;
import com.tn.datagrid.core.domain.StringValue;
import com.tn.datagrid.core.predicate.PredicateWrapper;

public class StringValueCaoImpl implements StringValueCao
{
  private static final String CACHE_STRINGS = "strings";

  private IMap<Identity<?, StringValue>, StringValue> cache;

  public StringValueCaoImpl(HazelcastInstance hazelcastInstance)
  {
    this.cache = hazelcastInstance.getMap(CACHE_STRINGS);
  }

  @Override
  public Optional<StringValue> get(Identity<?, StringValue> identity)
  {
    return Optional.ofNullable(this.cache.get(identity));
  }

  @Override
  public Collection<StringValue> get(Predicate<StringValue> predicate)
  {
    return this.cache.values(new PredicateWrapper<>(predicate));
  }
}
