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
    return getAll(singleton(identity)).values().stream().findFirst();
  }

  Map<Identity, V> getAll(Collection<Identity> identities) throws CaoException;

  Map<Identity, V> getAll(Location location, Predicate<Identity, V> predicate) throws CaoException;
}
