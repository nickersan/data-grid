package com.tn.datagrid.core.predicate;

import java.util.Map;
import java.util.function.Predicate;

public class PredicateWrapper<K, V> implements com.hazelcast.query.Predicate<K, V>
{
  private Predicate<V> predicate;

  public PredicateWrapper(Predicate<V> predicate)
  {
    this.predicate = predicate;
  }

  @Override
  public boolean apply(Map.Entry<K, V> entry)
  {
    return predicate.test(entry.getValue());
  }
}
