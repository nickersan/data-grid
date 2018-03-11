package com.tn.datagrid.core.domain;

import static com.google.common.base.MoreObjects.toStringHelper;

public class CalculatedIdentity<T, V extends Value<T, V>, RT, RV extends Value<RT, RV>> extends Identity<T, V>
{
  private Operator<T, V, RT, RV> operator;
  private Identity<T, V> leftIdentity;
  private Identity<T, V> rightIdentity;

  public CalculatedIdentity(Type<T, V> type, Operator<T, V, RT, RV> operator, Identity<T, V> leftIdentity, Identity<T, V> rightIdentity)
  {
    super(type);
    this.operator = operator;
    this.leftIdentity = leftIdentity;
    this.rightIdentity = rightIdentity;
  }

  public Identity<T, V> getLeftIdentity()
  {
    return this.leftIdentity;
  }

  public Operator<T, V, RT, RV> getOperator()
  {
    return this.operator;
  }

  public Identity<T, V> getRightIdentity()
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
