package com.tn.datagrid.core.predicate;

import java.io.Serializable;
import java.util.function.Predicate;

import com.tn.datagrid.core.domain.ChildIdentity;
import com.tn.datagrid.core.domain.Type;
import com.tn.datagrid.core.domain.Value;

public class Predicates
{
  public static class Types
  {
    public static Predicate<Type<?, ?>> named(String name)
    {
      return new SerializablePredicate<Type<?, ?>>()
      {
        @Override
        public boolean test(Type<?, ?> type)
        {
          return name.equalsIgnoreCase(type.getName());
        }
      };
    }
  }

  public static class Values
  {
    public static <T, V extends Value<T, V>> Predicate<V> isA(Type<?, ?> type)
    {
      return new SerializablePredicate<V>()
      {
        @Override
        public boolean test(V value)
        {
          return type.equals(value.getType());
        }
      };
    }

    public static <T, V extends Value<T, V>> Predicate<V> childrenOf(Value<?, ?> parent)
    {
      return childrenOf(parent, false);
    }

    public static <T, V extends Value<T, V>> Predicate<V> childrenOf(Value<?, ?> parent, boolean recursive)
    {
      return new SerializablePredicate<V>()
      {
        @Override
        public boolean test(V value)
        {
          return value.getIdentity() instanceof ChildIdentity && ((ChildIdentity<?, ?>)value.getIdentity()).isChildOf(value, recursive);
        }
      };
    }
  }

  private static abstract class SerializablePredicate<T> implements Predicate<T>, Serializable
  {
  }
}
