package com.tn.datagrid.core.domain;

import static com.google.common.base.MoreObjects.toStringHelper;

import javax.annotation.Nonnull;

import com.hazelcast.core.PartitionAware;

public class NumericIdentity extends Identity implements Comparable<NumericIdentity>
{
  private int id;

  public NumericIdentity(String location, int id)
  {
    super(location);
    this.id = id;
  }

  public int get()
  {
    return id;
  }

  @Override
  public boolean equals(Object other)
  {
    return this == other || (
      other != null &&
      this.getClass().equals(other.getClass()) &&
      this.getLocation().equals(((NumericIdentity)other).getLocation()) &&
      this.id == ((NumericIdentity)other).id
    );
  }

  @Override
  public int hashCode()
  {
    return this.id;
  }

  @Override
  public String toString()
  {
    return toStringHelper(this)
      .add("location", this.getLocation())
      .add("id", this.id)
      .toString();
  }

  @Override
  public int compareTo(@Nonnull NumericIdentity other)
  {
    return Integer.compare(this.id, other.id);
  }
}
