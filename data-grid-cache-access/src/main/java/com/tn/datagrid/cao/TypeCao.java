package com.tn.datagrid.cao;

import java.util.Collection;
import java.util.function.Predicate;

import com.tn.datagrid.core.domain.Type;

public interface TypeCao
{
  public Collection<Type<?, ?>> get(Predicate<Type<?, ?>> predicate) throws CaoException;

  public Collection<Type<?, ?>> getAll() throws CaoException;
}
