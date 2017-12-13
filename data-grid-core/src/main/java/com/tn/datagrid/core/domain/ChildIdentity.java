package com.tn.datagrid.core.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ChildIdentity<T, V extends Value<T, V>> extends NumericIdentity<T, V>
{
  private Identity<?, ?> parentIdentity;

  public ChildIdentity(Type<T, V> type, int id, Value<?, ?> parent)
  {
    this(type, id, parent.getIdentity());
  }

  public ChildIdentity(Type<T, V> type, int id, Identity<?, ?> parentIdentity)
  {
    super(type, id);
    this.parentIdentity = parentIdentity;
  }

  public Identity<?, ?> getParentIdentity()
  {
    return parentIdentity;
  }

  public boolean isChildOf(Value<?, ?> value, boolean recursive)
  {
    return isChildOf(value.getIdentity(), recursive);
  }

  public boolean isChildOf(Identity<?, ?> identity, boolean recursive)
  {
    return parentIdentity.equals(identity) || (
      recursive &&
      parentIdentity instanceof ChildIdentity &&
      ((ChildIdentity<?, ?>)parentIdentity).isChildOf(identity, true)
    );
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
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("id", this.get())
      .append("parentIdentity", this.parentIdentity)
      .toString();
  }
}
