package com.tn.datagrid.core.domain;

import static com.google.common.base.MoreObjects.toStringHelper;

import com.hazelcast.core.PartitionAware;

public class NumericIdentity extends Identity implements PartitionAware<Integer>
{
  private int id;

  //Doesn't make sense but without this the cache loading for complex calculations dead-locks.
  @SuppressWarnings({"FieldCanBeLocal", "unused"})
  private int spurious;

  public NumericIdentity(int id)
  {
    this(id, 1);
  }

  public NumericIdentity(int id, int partitionKey)
  {
    this.id = id;
    this.spurious = partitionKey;
  }

  public int get()
  {
    return id;
  }

  @Override
  public Integer getPartitionKey()
  {
    return this.spurious;
  }

  @Override
  public boolean equals(Object other)
  {
    return this == other || (
      other != null &&
      this.getClass().equals(other.getClass()) &&
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
      .add("id", this.id)
      .toString();
  }
}
