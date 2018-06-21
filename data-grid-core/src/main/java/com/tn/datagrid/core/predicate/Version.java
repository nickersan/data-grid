package com.tn.datagrid.core.predicate;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;

import com.hazelcast.query.Predicate;

import com.tn.datagrid.core.domain.Versioned;
import com.tn.datagrid.core.domain.identity.Identity;

public class Version<V> implements Predicate<Identity, Versioned<V>>
{
  private int version;
  private Predicate<Identity, V> predicate;

  public Version()
  {
    this(Integer.MAX_VALUE);
  }

  public Version(int version)
  {
    this(version, null);
  }

  public Version(Predicate<Identity, V> predicate)
  {
    this(Integer.MAX_VALUE, predicate);
  }

  public Version(int version, Predicate<Identity, V> predicate)
  {
    this.version = version;
    this.predicate = predicate;
  }

  @Override
  public boolean apply(Map.Entry<Identity, Versioned<V>> entry)
  {
    Optional<Versioned<V>> closest = entry.getValue().getClosest(this.version);
    return closest.isPresent() && (predicate == null || predicate.apply(new AbstractMap.SimpleEntry<>(entry.getKey(), closest.get().get())));
  }
}
