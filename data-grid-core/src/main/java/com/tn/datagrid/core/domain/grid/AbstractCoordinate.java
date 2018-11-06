package com.tn.datagrid.core.domain.grid;

import static com.google.common.base.MoreObjects.toStringHelper;

public abstract class AbstractCoordinate<T, C> implements Coordinate<T>
{
  private T coordinate;

  public AbstractCoordinate(T coordinate)
  {
    this.coordinate = coordinate;
  }

  @Override
  public T get()
  {
    return this.coordinate;
  }

  @Override
  public boolean equals(Object other)
  {
    return this == other || (
      other != null &&
      this.getClass().equals(other.getClass()) &&
      this.coordinate.equals(((AbstractCoordinate)other).coordinate)
    );
  }

  @Override
  public int hashCode()
  {
    return this.coordinate.hashCode();
  }

  @Override
  public String toString()
  {
    return toStringHelper(this)
      .add("coordinate", this.coordinate)
      .toString();
  }

  protected abstract C toCell(Coordinate<T> coordinate);
}
