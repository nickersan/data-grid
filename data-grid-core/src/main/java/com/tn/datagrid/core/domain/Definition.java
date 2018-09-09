package com.tn.datagrid.core.domain;

import java.io.Serializable;

public abstract class Definition implements Serializable
{
  protected String name;

  public Definition(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }
}
