package com.tn.datagrid.core.projections;

import java.util.Map;
import java.util.Optional;

import com.hazelcast.projection.Projection;

import com.tn.datagrid.core.domain.Pair;
import com.tn.datagrid.core.domain.Versioned;
import com.tn.datagrid.core.domain.identity.Identity;

public class VersionProjection<V> extends Projection<Map.Entry<Identity, Versioned<V>>, Pair<Identity, V>>
{
  private int version;

  public VersionProjection()
  {
    this(Integer.MAX_VALUE);
  }

  public VersionProjection(int version)
  {
    this.version = version;
  }

  @Override
  public Pair<Identity, V> transform(Map.Entry<Identity, Versioned<V>> entry)
  {
    return new Pair<>(entry.getKey(), entry.getValue().getClosest(this.version).map(Versioned::get).orElse(null));
  }
}
