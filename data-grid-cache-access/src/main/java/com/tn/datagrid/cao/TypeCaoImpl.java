package com.tn.datagrid.cao;

import static java.util.stream.Collectors.*;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import com.tn.datagrid.core.aggregator.TypeAggregator;
import com.tn.datagrid.core.domain.Identity;
import com.tn.datagrid.core.domain.Type;
import com.tn.datagrid.core.domain.Value;
import com.tn.datagrid.core.util.HazelcastUtils;

public class TypeCaoImpl implements TypeCao
{
  private HazelcastInstance hazelcastInstance;

  public TypeCaoImpl(HazelcastInstance hazelcastInstance)
  {
    this.hazelcastInstance = hazelcastInstance;
  }

  @Override
  public Collection<Type<?, ?>> get(Predicate<Type<?, ?>> predicate)
  {
    return HazelcastUtils.getMaps(this.hazelcastInstance).stream()
      .map(doGet(predicate))
      .flatMap(Collection::stream)
      .collect(toSet());
  }

  @Override
  public Collection<Type<?, ?>> getAll()
  {
    return HazelcastUtils.getMaps(this.hazelcastInstance).stream()
      .map(doGet(null))
      .flatMap(Collection::stream)
      .collect(toSet());
  }

  private Function<IMap, Collection<Type<?, ?>>> doGet(Predicate<Type<?, ?>> predicate)
  {
    //noinspection unchecked
    return (map) -> ((IMap<Identity<?, ?>, Value<?, ?>>)map).aggregate(new TypeAggregator(predicate));
  }
}
