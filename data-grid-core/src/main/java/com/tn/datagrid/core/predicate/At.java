package com.tn.datagrid.core.predicate;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;

import com.hazelcast.query.Predicate;

import com.tn.datagrid.core.domain.Versioned;
import com.tn.datagrid.core.domain.identity.Identity;

public class At<V> implements Predicate<Identity, Versioned<V>>
{
  private int version;
  private Predicate<Identity, V> predicate;

  public At()
  {
    this(Integer.MAX_VALUE);
  }

  public At(int version)
  {
    this(null, version);
  }

  public At(Predicate<Identity, V> predicate)
  {
    this(predicate, Integer.MAX_VALUE);
  }

  public At(Predicate<Identity, V> predicate, int version)
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

  @Override
  public String toString()
  {
    return toStringHelper(this)
      .add("version", this.version)
      .add("predicate", this.predicate)
      .toString();
  }
}
