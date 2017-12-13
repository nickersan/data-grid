package com.tn.datagrid.core.domain;

import static java.util.Objects.*;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class IntValue extends Value<Integer, IntValue>
{
  private int value;

  public IntValue(Identity<Integer, IntValue> identity, int value)
  {
    super(identity);
    this.value = value;
  }

  @Override
  public Integer get()
  {
    return this.value;
  }

  public int getPrimitive()
  {
    return this.value;
  }

  @Override
  public boolean equals(Object other)
  {
    return this == other || (
      other != null &&
      getClass().equals(other.getClass()) &&
      getIdentity().equals(((IntValue)other).getIdentity()) &&
      getPrimitive() == ((IntValue)other).getPrimitive()
    );
  }

  @Override
  public int hashCode()
  {
    return hash(getIdentity(), getPrimitive());
  }

  @Override
  public String toString()
  {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("identity", getIdentity())
      .append("value", getPrimitive())
      .toString();
  }
}
