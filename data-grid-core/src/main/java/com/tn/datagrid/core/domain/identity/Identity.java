package com.tn.datagrid.core.domain.identity;

import java.io.Serializable;

import com.tn.datagrid.core.domain.Location;

public interface Identity extends Serializable
{
  public Location getLocation();
}
