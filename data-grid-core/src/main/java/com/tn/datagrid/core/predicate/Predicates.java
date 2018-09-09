package com.tn.datagrid.core.predicate;

import java.util.Collection;

import com.hazelcast.query.Predicate;

import com.tn.datagrid.core.domain.identity.Identity;

public class Predicates
{
  public static <V> Predicate<Identity, V> and(Collection<Predicate<Identity, V>> predicates)
  {
    //noinspection unchecked
    return and(predicates.toArray(new Predicate[0]));
  }

  @SafeVarargs
  public static <V> Predicate<Identity, V> and(Predicate<Identity, V>... predicates)
  {
    //noinspection unchecked
    return com.hazelcast.query.Predicates.and(predicates);
  }

  public static <V> Predicate<Identity, V> childOf(Identity parentIdentity)
  {
    return new ChildOf<>(parentIdentity);
  }

  public static <V> Predicate<Identity, V> childOf(Identity parentIdentity, boolean recursive)
  {
    return new ChildOf<>(parentIdentity, recursive);
  }

  public static <V> Predicate<Identity, V> eq(Identity identity)
  {
    return new Eq<>(identity);
  }

  public static <V> Predicate<Identity, V> or(Collection<Predicate<Identity, V>> predicates)
  {
    //noinspection unchecked
    return or(predicates.toArray(new Predicate[0]));
  }

  @SafeVarargs
  public static <V> Predicate<Identity, V> or(Predicate<Identity, V>... predicates)
  {
    //noinspection unchecked
    return com.hazelcast.query.Predicates.or(predicates);
  }
}
