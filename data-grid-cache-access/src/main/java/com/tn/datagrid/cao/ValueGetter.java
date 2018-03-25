package com.tn.datagrid.cao;

import java.util.Optional;

import com.tn.datagrid.core.domain.Identity;

public interface ValueGetter<V>
{
  public Optional<V> get(Identity identity);
}
