package com.tn.datagrid.core.domain;

import static java.lang.String.format;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tn.datagrid.core.util.NumberUtils;

public class Operators
{
  private static Logger logger = LoggerFactory.getLogger(Operators.class);

  private static final String FORMAT_VERSIONED_OPERATOR_SYMBOL = "@{%d}";
  private static final String OPERATOR_SYMBOL_LATEST = "@latest";
  private static final String OPERATOR_SYMBOL_MULTIPLY = "*";
  private static final String OPERATOR_SYMBOL_SUM = "SUM";

  public static <T> BiOperator<T, Versioned<T>, Versioned<T>> closest(BiOperator<T, T, T> operator, int version)
  {
    return new AbstractBiOperator<>(format(FORMAT_VERSIONED_OPERATOR_SYMBOL, version), ((AbstractBiOperator<T, T, T>)operator).getReturnType())
    {
      @Override
      protected T doApply(Versioned<T> left, Versioned<T> right)
      {
        logger.trace("Closest - left: {}, right: {}", left, right);

        Optional<Versioned<T>> leftClosest = left.getClosest(version);
        //noinspection OptionalIsPresent
        if (!leftClosest.isPresent())
        {
          return null;
        }

        Optional<Versioned<T>> rightClosest = right.getClosest(version);
        //noinspection OptionalIsPresent
        if (!rightClosest.isPresent())
        {
          return null;
        }

        return operator.apply(leftClosest.get().get(), rightClosest.get().get());
      }
    };
  }

  public static <T> BiOperator<T, Versioned<T>, Versioned<T>> latest(BiOperator<T, T, T> operator)
  {
    return new AbstractBiOperator<>(OPERATOR_SYMBOL_LATEST, ((AbstractBiOperator<T, T, T>)operator).getReturnType())
    {
      @Override
      protected T doApply(Versioned<T> left, Versioned<T> right)
      {
        logger.trace("Latest - left: {}, right: {}", left, right);
        return operator.apply(left.get(), right.get());
      }
    };
  }

  public static <T> BiOperator<T, Versioned<T>, T> latestLeft(BiOperator<T, T, T> operator)
  {
    return new AbstractBiOperator<>(OPERATOR_SYMBOL_LATEST, ((AbstractBiOperator<T, T, T>)operator).getReturnType())
    {
      @Override
      protected T doApply(Versioned<T> left, T right)
      {
        logger.trace("Latest - left: {}, right: {}", left, right);
        return operator.apply(left.get(), right);
      }
    };
  }

  public static <T> BiOperator<T, T, Versioned<T>> latestRight(BiOperator<T, T, T> operator)
  {
    return new AbstractBiOperator<>(OPERATOR_SYMBOL_LATEST, ((AbstractBiOperator<T, T, T>)operator).getReturnType())
    {
      @Override
      protected T doApply(T left, Versioned<T> right)
      {
        logger.trace("Latest - left: {}, right: {}", left, right);
        return operator.apply(left, right.get());
      }
    };
  }

  public static BiOperator<Number, Number, Number> multiply()
  {
    return new AbstractBiOperator<>(OPERATOR_SYMBOL_MULTIPLY, Number.class)
    {
      @Override
      protected Number doApply(Number left, Number right)
      {
        logger.trace("Multiply - left: {}, right: {}", left, right);
        return NumberUtils.multiply(left, right);
      }
    };
  }

  public static AggregateOperator<Number> sum()
  {
    return new AbstractAggregateOperator<>(OPERATOR_SYMBOL_SUM, Number.class)
    {
      @Override
      protected Number doApply(Stream<Number> values)
      {
        return values.reduce(NumberUtils::add).orElse(0);
      }
    };
  }

  private abstract static class AbstractAggregateOperator<T> extends AbstractOperator<T> implements AggregateOperator<T>
  {
    public AbstractAggregateOperator(String symbol, Class<T> returnType)
    {
      super(symbol, returnType);
    }

    @Override
    public T apply(Collection<T> values)
    {
      return doApply(values.stream().filter(Objects::nonNull));
    }

    protected abstract T doApply(Stream<T> values);
  }

  private abstract static class AbstractBiOperator<T, LT, RT> extends AbstractOperator<T> implements BiOperator<T, LT, RT>
  {
    public AbstractBiOperator(String symbol, Class<T> returnType)
    {
      super(symbol, returnType);
    }

    @Override
    public T apply(LT left, RT right)
    {
      if (left == null || right == null)
      {
        return null;
      }

      return doApply(left, right);
    }

    protected abstract T doApply(LT left, RT right);
  }

  private abstract static class AbstractOperator<T> implements Serializable
  {
    private String symbol;
    private Class<T> returnType;

    public AbstractOperator(String symbol, Class<T> returnType)
    {
      this.symbol = symbol;
      this.returnType = returnType;
    }

    public Class<T> getReturnType()
    {
      return returnType;
    }

    public String getSymbol()
    {
      return symbol;
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
