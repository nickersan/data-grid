package com.tn.datagrid.core.domain;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.io.Serializable;

public class Location implements Serializable
{
  private String mapName;

  public Location(String mapName)
  {
    this.mapName = mapName;
  }

  public String getMapName()
  {
    return mapName;
  }

  @Override
  public boolean equals(Object other)
  {
    return this == other || (
      other != null &&
        this.getClass().equals(other.getClass()) &&
        this.mapName.equals(((Location)other).mapName)
    );
  }

  @Override
  public int hashCode()
  {
    return this.mapName.hashCode();
  }

  @Override
  public String toString()
  {
    return toStringHelper(this)
      .add("mapName", this.mapName)
      .toString();
  }
}
