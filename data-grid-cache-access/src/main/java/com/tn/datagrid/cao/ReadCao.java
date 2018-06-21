package com.tn.datagrid.cao;

import static java.util.Collections.singleton;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import com.hazelcast.query.Predicate;

import com.tn.datagrid.core.domain.Location;
import com.tn.datagrid.core.domain.identity.Identity;

public interface ReadCao<V>
{
  default Optional<V> get(Identity identity) throws CaoException
  {
    return this.getAll(singleton(identity)).values().stream().findFirst();
  }

  default Optional<V> getAt(Identity identity, int version) throws CaoException
  {
    return this.getAllAt(singleton(identity), version).values().stream().findFirst();
  }

  default Map<Identity, V> getAll(Collection<Identity> identities) throws CaoException
  {
    return this.getAllAt(identities, Integer.MAX_VALUE);
  }

  Map<Identity, V> getAllAt(Collection<Identity> identities, int  version) throws CaoException;

  default Map<Identity, V> getAll(Location location, Predicate<Identity, V> predicate) throws CaoException
  {
    return this.getAllAt(location, predicate, Integer.MAX_VALUE);
  }

  Map<Identity, V> getAllAt(Location location, Predicate<Identity, V> predicate, int version) throws CaoException;
}
