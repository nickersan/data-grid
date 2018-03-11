package com.tn.datagrid.core.domain;

import static java.util.Objects.hash;

import static com.google.common.base.MoreObjects.toStringHelper;

public abstract class ObjectValue<T, V extends Value<T, V>> extends Value<T, V>
{
  private T value;

  public ObjectValue(Identity<T, V> identity, T value)
  {
    super(identity);
    this.value = value;
  }

  @Override
  public T get()
  {
    return this.value;
  }

  @Override
  public boolean equals(Object other)
  {
    return this == other || (
      other != null &&
      getClass().equals(other.getClass()) &&
      getIdentity().equals(((ObjectValue)other).getIdentity()) &&
      get().equals(((ObjectValue)other).get())
    );
  }

  @Override
  public int hashCode()
  {
    return hash(getIdentity(), get());
  }

  @Override
  public String toString()
  {
    return toStringHelper(this)
      .add("identity", getIdentity())
      .add("value", get())
      .toString();
  }
}
