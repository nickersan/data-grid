package com.tn.datagrid.core.domain.identity;

import static com.google.common.base.MoreObjects.toStringHelper;

public class ChildIdentity extends NumericIdentity
{
  private Identity parentIdentity;

  public ChildIdentity(Identity parentIdentity, int id)
  {
    super(parentIdentity.getLocation(), id);
    this.parentIdentity = parentIdentity;
  }

  public Identity getParentIdentity()
  {
    return parentIdentity;
  }

  public boolean isChild(Identity parentIdentity)
  {
    return this.parentIdentity.equals(parentIdentity);
  }

  @Override
  public boolean equals(Object other)
  {
    return this == other || (
      other != null &&
        this.getClass().equals(other.getClass()) &&
        this.getLocation().equals(((ChildIdentity)other).getLocation()) &&
        this.get() == ((ChildIdentity)other).get() &&
        this.parentIdentity.equals(((ChildIdentity)other).parentIdentity)
    );
  }

  @Override
  public int hashCode()
  {
    return super.hashCode();
  }

  @Override
  public String toString()
  {
    return toStringHelper(this)
      .add("location", this.getLocation())
      .add("id", this.get())
      .add("parentIdentity", this.parentIdentity)
      .toString();
  }
}
