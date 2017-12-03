package com.tn.datagrid.core.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ChildIdentity<T, V extends Value<T, V>> extends NumericIdentity<T, V>
{
  private Identity<?, ?> parentIdentity;

  public ChildIdentity(Type<T, V> type, int id, Identity<?, ?> parentIdentity)
  {
    super(type, id);
    this.parentIdentity = parentIdentity;
  }

  @Override
  public boolean equals(Object other)
  {
    return this == other || (
      other != null &&
      getClass().equals(other.getClass()) &&
      this.get() == ((ChildIdentity)other).get() &&
      this.parentIdentity.equals(((ChildIdentity)other).parentIdentity)
    );
  }

  @Override
  public int hashCode()
  {
    return this.get();
  }

  @Override
  public String toString()
  {
    return new ToStringBuilder(ToStringStyle.SHORT_PREFIX_STYLE)
      .append("id", this.get())
      .append("parentIdentity", this.parentIdentity)
      .toString();
  }
}
