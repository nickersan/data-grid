package com.tn.datagrid.cao;

import static java.util.Collections.singleton;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import com.tn.datagrid.core.domain.Identity;

public interface ValueGetter<V>
{
  public default Optional<V> get(Identity identity) throws CaoException
  {
    return getAll(singleton(identity)).values().stream().findFirst();
  }

  public Map<Identity, V> getAll(Collection<Identity> identities) throws CaoException;
}
