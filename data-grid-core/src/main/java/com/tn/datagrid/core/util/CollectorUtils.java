package com.tn.datagrid.core.util;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class CollectorUtils
{
  public static <T, R> Collector<T, ?, R> collectAndMap(Function<Collection<T>, R> mapping)
  {
    return new Collector<T, Collection<T>, R>()
    {
      @Override
      public Supplier<Collection<T>> supplier()
      {
        return ArrayList::new;
      }

      @Override
      public BiConsumer<Collection<T>, T> accumulator()
      {
        return Collection::add;
      }

      @Override
      public BinaryOperator<Collection<T>> combiner()
      {
        return (values1, values2) -> Stream.concat(values1.stream(), values2.stream()).collect(toList());
      }

      @Override
      public Function<Collection<T>, R> finisher()
      {
        return mapping;
      }

      @Override
      public Set<Characteristics> characteristics()
      {
        return Collections.emptySet();
      }
    };
  }
}
