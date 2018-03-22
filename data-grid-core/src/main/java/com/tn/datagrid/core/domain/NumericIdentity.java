package com.tn.datagrid.core.domain;

import static com.google.common.base.MoreObjects.toStringHelper;

public class NumericIdentity extends Identity
{
  private int id;

  public NumericIdentity(String location, int id)
  {
    super(location);
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
      this.getClass().equals(other.getClass()) &&
      this.getLocation().equals(((NumericIdentity)other).getLocation()) &&
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
      .add("location", this.getLocation())
      .add("id", this.id)
      .toString();
  }
}
