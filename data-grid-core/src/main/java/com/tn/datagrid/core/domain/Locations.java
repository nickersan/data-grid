package com.tn.datagrid.core.domain;

public class Locations
{
  private static final String MAP_NAME_LISTS = "primary.lists";

  public static Location listsLocation()
  {
    return new Location(MAP_NAME_LISTS);
  }
}
