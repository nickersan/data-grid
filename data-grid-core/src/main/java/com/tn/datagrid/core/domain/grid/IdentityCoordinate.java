package com.tn.datagrid.core.domain.grid;

import com.tn.datagrid.core.domain.AggregateOperator;
import com.tn.datagrid.core.domain.identity.ChildIdentity;
import com.tn.datagrid.core.domain.identity.Identity;
import com.tn.datagrid.core.domain.identity.NumericIdentity;

public abstract class IdentityCoordinate extends AbstractCoordinate<Identity, Identity>
{
  private IdentityCoordinate(Identity coordinate)
  {
    super(coordinate);
  }

  public static IdentityCoordinate from(ChildIdentity identity)
  {
    
  }

  public static IdentityCoordinate from(NumericIdentity identity)
  {

  }

  public static IdentityCoordinate from(NumericIdentity identity, AggregateOperator aggregateOperator)
  {

  }

  private static class SimpleIdentityCoordinate extends IdentityCoordinate
  {
    private SimpleIdentityCoordinate(NumericIdentity coordinate)
    {
      super(coordinate);
    }

    @Override
    protected Identity toCell(Coordinate<Identity> coordinate)
    {
      return null;
    }
  }
}
