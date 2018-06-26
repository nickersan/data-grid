package com.tn.datagrid.core.predicate;

import java.util.Map;

import com.hazelcast.query.Predicate;

import com.tn.datagrid.core.domain.identity.ChildIdentity;
import com.tn.datagrid.core.domain.identity.Identity;

public class ChildOf<V> implements Predicate<Identity, V>
{
  private Identity parentIdentity;
  private boolean recursive;

  public ChildOf(Identity parentIdentity)
  {
    this(parentIdentity, false);
  }

  public ChildOf(Identity parentIdentity, boolean recursive)
  {
    this.parentIdentity = parentIdentity;
    this.recursive = recursive;
  }

  @Override
  public boolean apply(Map.Entry<Identity, V> entry)
  {
    return entry.getKey() instanceof ChildIdentity && ((ChildIdentity)entry.getKey()).isChild(this.parentIdentity, this.recursive);
  }
}
