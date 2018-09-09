package com.tn.datagrid.core.domain;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.util.Collection;
import java.util.Set;

import com.tn.datagrid.core.domain.identity.Identity;

public class LineItemDefinition extends Definition
{
  private Collection<Identity> dimensionIdentities;

  public LineItemDefinition(String name, Identity dimensionIdentity)
  {
    this(name, Set.of(dimensionIdentity));
  }

  public LineItemDefinition(String name, Collection<Identity> dimensionIdentities)
  {
    super(name);
    this.dimensionIdentities = Set.copyOf(dimensionIdentities);
  }

  @Override
  public boolean equals(Object other)
  {
    return this == other || (
      other != null &&
        this.getClass().equals(other.getClass()) &&
        this.getName().equals(((ListDefinition)other).getName())
    );
  }

  @Override
  public int hashCode()
  {
    return this.name.hashCode();
  }

  @Override
  public String toString()
  {
    return toStringHelper(this)
      .add("name", this.getName())
      .toString();
  }
}
