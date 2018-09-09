package com.tn.datagrid.core.domain;

public class Locations
{
  private static final String MAP_NAME_CALCULATED_VALUES = "calculated.values";
  private static final String MAP_NAME_LINE_ITEMS = "primary.lineItems";
  private static final String MAP_NAME_LISTS = "primary.lists";

  public static Location lineItemsLocation()
  {
    return new Location(MAP_NAME_LINE_ITEMS);
  }

  public static Location listsLocation()
  {
    return new Location(MAP_NAME_LISTS);
  }

  public static Location calculatedValues()
  {
    return new Location(MAP_NAME_CALCULATED_VALUES);
  }
}
