package com.tn.datagrid.core.domain;

import static com.google.common.base.MoreObjects.toStringHelper;

public class CalculatedIdentity<T, LT, RT> extends Identity
{
  private Operator<T, LT, RT> operator;
  private Identity leftIdentity;
  private Identity rightIdentity;

  public CalculatedIdentity(Operator<T, LT, RT> operator, Identity leftIdentity, Identity rightIdentity)
  {
    this.operator = operator;
    this.leftIdentity = leftIdentity;
    this.rightIdentity = rightIdentity;
  }

  public Identity getLeftIdentity()
  {
    return this.leftIdentity;
  }

  public Operator<T, LT, RT> getOperator()
  {
    return this.operator;
  }

  public Identity getRightIdentity()
  {
    return this.rightIdentity;
  }

  @Override
  public boolean equals(Object other)
  {
    return this == other || (
      other != null &&
      getClass().equals(other.getClass()) &&
      this.operator.equals(((CalculatedIdentity)other).operator) &&
      this.leftIdentity.equals(((CalculatedIdentity)other).leftIdentity) &&
      this.rightIdentity.equals(((CalculatedIdentity)other).rightIdentity)
    );
  }

  @Override
  public int hashCode()
  {
    return this.operator.hashCode();
  }

  @Override
  public String toString()
  {
    return toStringHelper(this)
      .add("leftIdentity", this.leftIdentity)
      .add("operator", this.operator)
      .add("rightIdentity", this.rightIdentity)
      .toString();
  }
}
