package com.tn.datagrid.core.domain;

import static com.google.common.base.MoreObjects.toStringHelper;

public class NumericIdentity<T, V extends Value<T, V>> extends Identity<T, V> //implements PartitionAware<Integer>
{
  private int id;

  //Doesn't make sense but without this the cache loading for complex calculations dead-locks.
  @SuppressWarnings({"FieldCanBeLocal", "unused"})
  private int spurious;

  public NumericIdentity(Type<T, V> type, int id)
  {
    super(type);
    this.id = id;
    this.spurious = 1;
  }

  public int get()
  {
    return id;
  }

//  @Override
//  public Integer getPartitionKey()
//  {
//    return this.spurious;
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
      .add("type", this.getType())
      .add("id", this.id)
      .toString();
  }
}
