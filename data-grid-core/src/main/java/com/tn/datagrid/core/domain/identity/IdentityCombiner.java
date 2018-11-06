package com.tn.datagrid.core.domain.identity;

import com.tn.datagrid.core.domain.Location;

public class IdentityCombiner
{
  public static Identity combine(Location location, Identity x, Identity y)
  {
    if (x instanceof AggregateIdentity)
    {
      return ((AggregateIdentity<?>)x).by(location, y);
    }
    else if (y instanceof AggregateIdentity)
    {
      return ((AggregateIdentity<?>)y).by(location, x);
    }
    else
    {
      return new CompositeIdentity(location, x, y);
    }
  }
}
