package com.tn.datagrid.core.domain;

import static com.google.common.base.MoreObjects.toStringHelper;

import com.hazelcast.core.PartitionAware;

public class NumericIdentity extends Identity implements PartitionAware<Integer>
{
  private int partition;
  private int id;

  public NumericIdentity(String location, int id)
  {
    super(location);
    this.id = id;
  }

  public NumericIdentity(int partition, String location, int id)
  {
    super(location);
    this.partition = partition;
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
  public Integer getPartitionKey()
  {
    return this.partition;
  }
}
