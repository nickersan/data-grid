package com.tn.datagrid.core.domain;

import static com.google.common.base.MoreObjects.toStringHelper;

import com.hazelcast.core.PartitionAware;

public class NumericIdentity<T, V extends Value<T, V>> extends Identity<T, V> //implements PartitionAware<Integer>
{
  private int id;
  private int partitionKey;

  public NumericIdentity(Type<T, V> type, int id)
  {
    super(type);
    this.id = id;
    this.partitionKey = 1;
  }

  public NumericIdentity(Type<T, V> type, int id, int partitionKey)
  {
    super(type);
    this.id = id;
    this.partitionKey = partitionKey;
  }

  public int get()
  {
    return id;
  }

//  @Override
//  public Integer getPartitionKey()
//  {
//    return this.partitionKey;
//  }

  @Override
  public boolean equals(Object other)
  {
    return this == other || (
      other != null &&
      this.getClass().equals(other.getClass()) &&
      this.getType().equals(((NumericIdentity)other).getType()) &&
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
