package com.tn.datagrid.cao;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

import com.tn.datagrid.core.domain.Identity;
import com.tn.datagrid.core.domain.Value;

public interface ValueCao<T, V extends Value<T, V>>
{
  public Optional<V> get(Identity<?, V> identity);

  public Collection<V> get(Predicate<V> predicate);
}
