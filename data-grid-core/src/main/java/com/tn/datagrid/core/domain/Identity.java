package com.tn.datagrid.core.domain;

import java.io.Serializable;

public abstract class Identity implements Serializable
{
  public abstract boolean equals(Object other);

  public abstract int hashCode();

  public abstract String toString();
}
