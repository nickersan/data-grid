package com.tn.datagrid.core.domain;

import static com.google.common.base.MoreObjects.toStringHelper;

import static com.tn.datagrid.core.util.NumberUtils.multiple;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Operators
{
  private static Logger logger = LoggerFactory.getLogger(Operators.class);

  public static Operator<Number, NumberValue, Number, NumberValue, Number, NumberValue> multiply()
  {
    return new AbstractOperator<Number, NumberValue, Number, NumberValue, Number, NumberValue>("*", Number.class)
    {
      @Override
      public NumberValue apply(CalculatedIdentity<Number, NumberValue, Number, NumberValue, Number, NumberValue> resultIdentity, NumberValue left, NumberValue right)
      {
        logger.debug("Multiply - left: {}, right: {}", left, right);
        return new NumberValue(resultIdentity, multiple(left.get(), right.get()));
      }
    };
  }

  private abstract static class AbstractOperator<T, V extends Value<T, V>, LT, LV extends Value<LT, LV>, RT, RV extends Value<RT, RV>> implements Operator<T, V, LT, LV, RT, RV>, Serializable
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
