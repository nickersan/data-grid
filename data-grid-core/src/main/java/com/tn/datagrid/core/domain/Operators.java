package com.tn.datagrid.core.domain;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.io.Serializable;

public class Operators
{
  public static Operator<Integer, IntValue, Integer, IntValue> multiply()
  {
    return new AbstractOperator<Integer, IntValue, Integer, IntValue>("*", Integer.class)
    {
      @Override
      public IntValue apply(CalculatedIdentity<Integer, IntValue, Integer, IntValue> resultIdentity, IntValue left, IntValue right)
      {
        return new IntValue(resultIdentity, left.get() * right.get());
      }
    };
  }

  private abstract static class AbstractOperator<T, V extends Value<T, V>, RT, RV extends Value<RT, RV>> implements Operator<T, V, RT, RV>, Serializable
  {
    private String symbol;
    private Class<T> returnType;

    public AbstractOperator(String symbol, Class<T> returnType)
    {
      this.symbol = symbol;
      this.returnType = returnType;
    }

    @Override
    public boolean equals(Object other)
    {
      return this == other || (
        other != null &&
        getClass().equals(other.getClass()) &&
        this.symbol.equals(((AbstractOperator)other).symbol) &&
        this.returnType.equals(((AbstractOperator)other).returnType)
      );
    }

    @Override
    public int hashCode()
    {
      return this.symbol.hashCode();
    }

    @Override
    public String toString()
    {
      return toStringHelper(this)
        .add("symbol", this.symbol)
        .add("returnType", this.returnType)
        .toString();
    }
  }
}
