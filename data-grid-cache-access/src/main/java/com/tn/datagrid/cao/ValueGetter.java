package com.tn.datagrid.cao;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import com.tn.datagrid.core.domain.Identity;

public interface ValueGetter<V>
{
  public Optional<V> get(Identity identity);

  public Map<Identity, V> getAll(Collection<Identity> identities);
}
