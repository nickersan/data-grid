package com.tn.datagrid.core.domain.identity;

import com.tn.datagrid.core.domain.Location;

public abstract class AbstractIdentity implements Identity
{
  private Location location;

  public AbstractIdentity(Location location)
  {
    this.location = location;
  }

  @Override
  public Location getLocation()
  {
    return this.location;
  }

  public abstract boolean equals(Object other);

  public abstract int hashCode();

  public abstract String toString();
}
