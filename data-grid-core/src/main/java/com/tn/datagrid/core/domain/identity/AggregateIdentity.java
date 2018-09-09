package com.tn.datagrid.core.domain.identity;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.util.Collection;

import com.tn.datagrid.core.domain.AggregateOperator;
import com.tn.datagrid.core.domain.Location;

public class AggregateIdentity<T> extends AbstractIdentity
{
  private AggregateOperator<T> operator;
  private Identity rootIdentity;
  private Collection<Identity> identities;

  public AggregateIdentity(Location location, AggregateOperator<T> operator, Identity rootIdentity, Collection<Identity> identities)
  {
    super(location);
    this.operator = operator;
    this.rootIdentity = rootIdentity;
    this.identities = identities;
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
