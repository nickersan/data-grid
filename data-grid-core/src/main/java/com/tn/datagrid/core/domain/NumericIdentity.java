package com.tn.datagrid.core.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class NumericIdentity<T, V extends Value<T, V>> extends Identity<T, V>
{
  private int id;

  public NumericIdentity(Type<T, V> type, int id)
  {
    super(type);
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
      getClass().equals(other.getClass()) &&
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
    return new ToStringBuilder(ToStringStyle.SHORT_PREFIX_STYLE)
      .append("id", this.id)
      .toString();
  }
}
