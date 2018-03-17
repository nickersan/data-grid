package com.tn.datagrid.core.domain;

import static java.util.Objects.hash;

import static com.google.common.base.MoreObjects.toStringHelper;

public class NumberValue extends Value<Number, NumberValue>
{
  private Number value;

  public NumberValue(Identity<Number, NumberValue> identity, Number value)
  {
    super(identity);
    this.value = value;
  }

  @Override
  public Number get()
  {
    return this.value;
  }

  @Override
  public boolean equals(Object other)
  {
    return this == other || (
      other != null &&
      getClass().equals(other.getClass()) &&
      getIdentity().equals(((NumberValue)other).getIdentity()) &&
      get().equals(((NumberValue)other).get())
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
      .add("value", get())
      .toString();
  }
}
