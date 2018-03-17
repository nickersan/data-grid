package com.tn.datagrid.core.domain;

import static java.util.Objects.hash;

import static com.google.common.base.MoreObjects.toStringHelper;

public class IntValue extends NumberValue<Integer, IntValue>
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
    return hash(getIdentity());
  }

  @Override
  public String toString()
  {
    return toStringHelper(this)
      .add("identity", getIdentity())
      .add("value", getPrimitive())
      .toString();
  }
}
