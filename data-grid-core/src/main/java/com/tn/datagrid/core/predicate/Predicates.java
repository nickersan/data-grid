package com.tn.datagrid.core.predicate;

import java.io.Serializable;
import java.util.function.Predicate;

import com.tn.datagrid.core.domain.ChildIdentity;
import com.tn.datagrid.core.domain.Type;
import com.tn.datagrid.core.domain.Value;

public class Predicates
{
  @SafeVarargs
  public static <T> Predicate<T> and(Predicate<T>... predicates)
  {
    return new SerializablePredicate<T>()
    {
      @Override
      public boolean test(T t)
      {
        for (Predicate<T> predicate : predicates)
        {
          if (!predicate.test(t))
          {
            return false;
          }
        }

        return true;
      }
    };
  }

  @SafeVarargs
  public static <T> Predicate<T> or(Predicate<T>... predicates)
  {
    return new SerializablePredicate<T>()
    {
      @Override
      public boolean test(T t)
      {
        for (Predicate<T> predicate : predicates)
        {
          if (predicate.test(t))
          {
            return true;
          }
        }

        return false;
      }
    };
  }

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
    public static <T, V extends Value<?, ?>> Predicate<V> isA(String typeName)
    {
      return new SerializablePredicate<V>()
      {
        @Override
        public boolean test(V value)
        {
          return typeName.equals(value.getType().getName());
        }
      };
    }

    public static <T, V extends Value<?, ?>> Predicate<V> isA(Type<?, ?> type)
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

    public static <T, V extends Value<?, ?>> Predicate<V> childrenOf(Value<?, ?> parent)
    {
      return childrenOf(parent, false);
    }

    public static <T, V extends Value<?, ?>> Predicate<V> childrenOf(Value<?, ?> parent, boolean recursive)
    {
      return new SerializablePredicate<V>()
      {
        @Override
        public boolean test(V value)
        {
          return value.getIdentity() instanceof ChildIdentity && ((ChildIdentity<?, ?>)value.getIdentity()).isChildOf(parent, recursive);
        }
      };
    }
  }

  private static abstract class SerializablePredicate<T> implements Predicate<T>, Serializable
  {
  }
}
