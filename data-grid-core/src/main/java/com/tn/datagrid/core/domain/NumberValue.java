package com.tn.datagrid.core.domain;

import static com.google.common.base.MoreObjects.toStringHelper;

public abstract class NumberValue<T extends Number, V extends NumberValue<T, V>> extends Value<T, V>
{
  public NumberValue(Identity<T, V> identity)
  {
    super(identity);
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
