package com.tn.datagrid.cao;

import java.util.Optional;

import com.hazelcast.core.IMap;

import com.tn.datagrid.core.domain.Identity;
import com.tn.datagrid.core.domain.StringValue;

public class StringValueCaoImpl implements StringValueCao
{
  private IMap<Identity<?, StringValue>, StringValue> cache;

  public StringValueCaoImpl(IMap<Identity<?, StringValue>, StringValue> cache)
  {
    this.cache = cache;
  }

  @Override
  public Optional<StringValue> get(Identity<?, StringValue> identity)
  {
    return Optional.ofNullable(this.cache.get(identity));
  }
}
