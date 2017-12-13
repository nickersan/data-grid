package com.tn.datagrid.core.aggregator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Predicate;

import com.hazelcast.aggregation.Aggregator;

import com.tn.datagrid.core.domain.Identity;
import com.tn.datagrid.core.domain.Type;
import com.tn.datagrid.core.domain.Value;

public class TypeAggregator extends Aggregator<Map.Entry<Identity<?, ?>, Value<?, ?>>, Collection<Type<?, ?>>>
{
  private Collection<Type<?, ?>> types = new HashSet<>();
  private Predicate<Type<?, ?>> predicate;

  public TypeAggregator()
  {
    this((type) -> true);
  }

  public TypeAggregator(Predicate<Type<?, ?>> predicate)
  {
    this.predicate = predicate;
  }

  @Override
  public void accumulate(Map.Entry<Identity<?, ?>, Value<?, ?>> entry)
  {
    Type<?, ?> type = entry.getValue().getType();

    if (this.predicate == null || this.predicate.test(type))
    {
      types.add(type);
    }
  }

  @Override
  public void combine(Aggregator aggregator)
  {
    this.types.addAll(((TypeAggregator)aggregator).types);
  }

  @Override
  public Collection<Type<?, ?>> aggregate()
  {
    return this.types;
  }
}
