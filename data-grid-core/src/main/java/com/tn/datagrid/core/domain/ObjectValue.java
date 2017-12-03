package com.tn.datagrid.core.domain;

import static java.util.Objects.hash;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

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
    return new ToStringBuilder(ToStringStyle.SHORT_PREFIX_STYLE)
      .append("identity", getIdentity())
      .append("value", get())
      .toString();
  }
}
