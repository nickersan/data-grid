package com.tn.datagrid.core.domain;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.io.Serializable;

public class ListDefinition implements Serializable
{
  private String name;

  public ListDefinition(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
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
