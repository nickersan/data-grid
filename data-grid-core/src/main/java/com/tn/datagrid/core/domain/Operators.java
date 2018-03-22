package com.tn.datagrid.core.domain;

import static java.lang.String.*;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.io.Serializable;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tn.datagrid.core.util.NumberUtils;

public class Operators
{
  private static Logger logger = LoggerFactory.getLogger(Operators.class);

  private static final String FORMAT_VERSIONED_OPERATOR_SYMBOL = "@{%d}";
  private static final String OPERATOR_SYMBOL_LATEST = "@latest";
  private static final String OPERATOR_SYMBOL_MULTIPLY = "*";

  public static <T> Operator<T, Versioned<T>, Versioned<T>> closest(Operator<T, T, T> operator, int version)
  {
    return new AbstractOperator<T, Versioned<T>, Versioned<T>>(format(FORMAT_VERSIONED_OPERATOR_SYMBOL, version), ((AbstractOperator<T, T, T>)operator).returnType)
    {
      @Override
      public T apply(Versioned<T> left, Versioned<T> right)
      {
        logger.trace("Closest - left: {}, right: {}", left, right);

        Optional<Versioned<T>> leftClosest = left.getClosest(version);
        if (!leftClosest.isPresent())
        {
          return null;
        }

        Optional<Versioned<T>> rightClosest = right.getClosest(version);
        if (!rightClosest.isPresent())
        {
          return null;
        }

        return operator.apply(leftClosest.get().get(), rightClosest.get().get());
      }
    };
  }

  public static <T> Operator<T, Versioned<T>, Versioned<T>> latest(Operator<T, T, T> operator)
  {
    return new AbstractOperator<T, Versioned<T>, Versioned<T>>(OPERATOR_SYMBOL_LATEST, ((AbstractOperator<T, T, T>)operator).returnType)
    {
      @Override
      public T apply(Versioned<T> left, Versioned<T> right)
      {
        logger.trace("Latest - left: {}, right: {}", left, right);
        return operator.apply(left.get(), right.get());
      }
    };
  }

  public static <T> Operator<T, Versioned<T>, T> latestLeft(Operator<T, T, T> operator)
  {
    return new AbstractOperator<T, Versioned<T>, T>(OPERATOR_SYMBOL_LATEST, ((AbstractOperator<T, T, T>)operator).returnType)
    {
      @Override
      public T apply(Versioned<T> left, T right)
      {
        logger.trace("Latest - left: {}, right: {}", left, right);
        return operator.apply(left.get(), right);
      }
    };
  }

  public static <T> Operator<T, T, Versioned<T>> latestRight(Operator<T, T, T> operator)
  {
    return new AbstractOperator<T, T, Versioned<T>>(OPERATOR_SYMBOL_LATEST, ((AbstractOperator<T, T, T>)operator).returnType)
    {
      @Override
      public T apply(T left, Versioned<T> right)
      {
        logger.trace("Latest - left: {}, right: {}", left, right);
        return operator.apply(left, right.get());
      }
    };
  }

  public static Operator<Number, Number, Number> multiply()
  {
    return new AbstractOperator<Number, Number, Number>(OPERATOR_SYMBOL_MULTIPLY, Number.class)
    {
      @Override
      public Number apply(Number left, Number right)
      {
        logger.trace("Multiply - left: {}, right: {}", left, right);
        return NumberUtils.multiply(left, right);
      }
    };
  }

  private abstract static class AbstractOperator<T, LT, RT> implements Operator<T, LT, RT>, Serializable
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
