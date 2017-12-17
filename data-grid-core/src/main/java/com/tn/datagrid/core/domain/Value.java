package com.tn.datagrid.core.domain;

import static java.util.function.Function.*;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public abstract class Value<T, V extends Value<T, V>> implements Serializable
{
  private Identity<T, V> identity;

  public Value(Identity<T, V> identity)
  {
    this.identity = identity;
  }

  public abstract T get();

  public Identity<T, V> getIdentity()
  {
    return this.identity;
  }

  public Type<T, V> getType()
  {
    return getIdentity().getType();
  }

  public static <T, V extends Value<T, V>> Collector<V , ?, Map<Identity<T, V> ,V>> byIdentity()
  {
    return Collectors.toMap(Value::getIdentity, identity());
  }

  public abstract boolean equals(Object other);

  public abstract int hashCode();

  public abstract String toString();

  public static <C extends Comparable<C>, T, V extends Value<T, V>> Comparator<V> compareBy(Function<V, C> getter)
  {
    return (value1, value2) ->
    {
      C comparable1 = getter.apply(value1);
      C comparable2 = getter.apply(value2);

      return comparable1.compareTo(comparable2);
    };
  }
}
