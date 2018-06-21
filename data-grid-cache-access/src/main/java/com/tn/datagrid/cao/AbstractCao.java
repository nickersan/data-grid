package com.tn.datagrid.cao;

import java.util.function.Function;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import com.tn.datagrid.core.domain.Location;
import com.tn.datagrid.core.domain.Versioned;
import com.tn.datagrid.core.domain.identity.Identity;

public class AbstractCao<V>
{
  private Function<Location, HazelcastInstance> hazelcastInstanceProvider;

  public AbstractCao(Function<Location, HazelcastInstance> hazelcastInstanceProvider)
  {
    this.hazelcastInstanceProvider = hazelcastInstanceProvider;
  }

  protected IMap<Identity, Versioned<V>> getMap(Identity identity)
  {
    return this.getMap(identity.getLocation());
  }

  protected IMap<Identity, Versioned<V>> getMap(Location location)
  {
    return this.hazelcastInstanceProvider.apply(location).getMap(location.getMapName());
  }
}
