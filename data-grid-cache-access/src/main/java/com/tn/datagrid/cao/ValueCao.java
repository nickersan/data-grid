package com.tn.datagrid.cao;

import java.util.Optional;

import com.tn.datagrid.core.domain.Identity;
import com.tn.datagrid.core.domain.Value;

public interface ValueCao<T, V extends Value<T, V>>
{
  public Optional<V> get(Identity<?, V> identity);
}
