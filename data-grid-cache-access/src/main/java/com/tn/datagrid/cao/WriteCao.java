package com.tn.datagrid.cao;

import java.util.Collection;
import java.util.Optional;

import com.tn.datagrid.core.domain.Location;
import com.tn.datagrid.core.domain.Pair;
import com.tn.datagrid.core.domain.identity.Identity;

public interface WriteCao<V>
{
  Identity create(Location location, V value) throws CaoException;

  Identity create(Identity parentIdentity, V value) throws CaoException;

  Identity create(Location location, Collection<Identity> identities, V value) throws CaoException;

  void update(Identity identity, V value) throws CaoException;

  default Optional<Pair<Identity, V>> delete(Identity identity) throws CaoException
  {
    return this.delete(identity, false);
  }

  Optional<Pair<Identity, V>> delete(Identity identity, boolean hard) throws CaoException;
}
