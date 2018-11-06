package com.tn.datagrid.core.domain.identity;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.tn.datagrid.core.domain.AggregateOperator;
import com.tn.datagrid.core.domain.Location;
import com.tn.datagrid.core.domain.Operators;

public class AggregateIdentity<T> extends AbstractIdentity
{
  private AggregateOperator<T> operator;
  private Identity rootIdentity;
  private Collection<Identity> identities;

  public AggregateIdentity(Location location, AggregateOperator<T> operator, Identity rootIdentity, Identity... identities)
  {
    this(location, operator, rootIdentity, List.of(identities));
  }

  public AggregateIdentity(Location location, AggregateOperator<T> operator, Identity rootIdentity, Collection<Identity> identities)
  {
    super(location);
    this.operator = operator;
    this.rootIdentity = rootIdentity;
    this.identities = new HashSet<>(identities);
  }

  public Collection<Identity> getIdentities()
  {
    return identities;
  }

  public AggregateOperator<T> getOperator()
  {
    return operator;
  }

  public Identity getRootIdentity()
  {
    return rootIdentity;
  }

  public static <T> Collector<? super Identity, ?, Identity> collector(Location location, AggregateOperator<T> aggregateOperator, Identity rootIdentity)
  {
    return collectingAndThen(toList(), (identities) -> new AggregateIdentity<>(location, aggregateOperator, rootIdentity, identities));
  }

  public AggregateIdentity<T> by(Location location, Identity other)
  {
    return new AggregateIdentity<>(
      this.getLocation(),
      this.operator,
      this.rootIdentity,
      this.identities.stream().map((identity) -> IdentityCombiner.combine(location, identity, other)).collect(toList())
    );
  }

  @Override
  public boolean equals(Object other)
  {
    return this == other || (
      other != null &&
        this.getClass().equals(other.getClass()) &&
        this.getLocation().equals(((AggregateIdentity)other).getLocation()) &&
        this.operator.equals(((AggregateIdentity)other).operator) &&
        this.rootIdentity.equals(((AggregateIdentity)other).rootIdentity) &&
        this.identities.equals(((AggregateIdentity)other).identities)
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
      .add("operator", this.operator)
      .add("rootIdentity", this.rootIdentity)
      .add("identities", this.identities)
      .toString();
  }
}
