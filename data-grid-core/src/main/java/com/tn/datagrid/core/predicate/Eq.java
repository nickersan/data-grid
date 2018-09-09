package com.tn.datagrid.core.predicate;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.util.Map;

import com.hazelcast.query.Predicate;

import com.tn.datagrid.core.domain.identity.Identity;

public class Eq<V> implements Predicate<Identity, V>
{
  private Identity identity;

  public Eq(Identity identity)
  {
    this.identity = identity;
  }

  @Override
  public boolean apply(Map.Entry<Identity, V> entry)
  {
    return entry.getKey().equals(this.identity);
  }

  @Override
  public String toString()
  {
    return toStringHelper(this)
      .add("identity", this.identity)
      .toString();
  }
}
