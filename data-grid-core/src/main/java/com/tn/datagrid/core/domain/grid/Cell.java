package com.tn.datagrid.core.domain.grid;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.util.Objects;

public class Cell<X, Y>
{
  private X x;
  private Y y;

  Cell(X x, Y y)
  {
    this.x = x;
    this.y = y;
  }

  public X getX()
  {
    return x;
  }

  public Y getY()
  {
    return y;
  }

  @Override
  public boolean equals(Object other)
  {
    return this == other || (
      other != null &&
        this.getClass().equals(other.getClass()) &&
        this.x.equals(((Cell)other).x) &&
        this.y.equals(((Cell)other).y)
    );
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(this.x, this.y);
  }

  @Override
  public String toString()
  {
    return toStringHelper(this)
      .add("x", this.x)
      .add("y", this.y)
      .toString();
  }
}
