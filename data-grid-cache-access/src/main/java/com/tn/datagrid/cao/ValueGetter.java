package com.tn.datagrid.cao;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.tn.datagrid.core.domain.Identity;

public interface ValueGetter<V>
{
  public Optional<V> get(Identity identity);

  public Map<Identity, V> getAll(Set<Identity> identities);
}
