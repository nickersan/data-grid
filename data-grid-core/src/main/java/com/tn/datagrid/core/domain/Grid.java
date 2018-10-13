package com.tn.datagrid.core.domain;

import static java.util.Collections.unmodifiableList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Grid<X, Y>
{
  private List<X> xAxis;
  private List<Y> yAxis;

  public Grid(List<X> xAxis, List<Y> yAxis)
  {
    this.xAxis = unmodifiableList(xAxis);
    this.yAxis = unmodifiableList(yAxis);
  }

  @Override
  public boolean equals(Object other)
  {
    return this == other || (
    other != null &&
    this.getClass().equals(other.getClass()) &&
    this.xAxis.equals(((Grid)other).xAxis) &&
    this.yAxis.equals(((Grid)other).yAxis)
    );
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(this.xAxis, this.yAxis);
  }

  @Override
  public String toString()
  {
    return toStringHelper(this)
      .add("xAxis", this.xAxis)
      .add("yAxis", this.yAxis)
      .toString();
  }

  protected static <X, Y> Cell<X, Y> cell(X x, Y y)
  {
    return new Cell<>(x, y);
  }

  public List<Cell<X, Y>> getOrigin()
  {
    return this.getOrigin(xAxis.size() > yAxis.size() ? AggregateDirection.VERTICAL : AggregateDirection.HORIZONTAL);
  }

  public List<Cell<X, Y>> getOrigin(AggregateDirection aggregateDirection)
  {
    return aggregateDirection.getOrigin(this);
  }

  public <T> GridJoiner<T> as(T total)
  {
    return new GridJoiner<>(total);
  }

  public enum AggregateDirection
  {
    HORIZONTAL
    {
      @Override
      public <X, Y> List<Cell<X, Y>> getOrigin(Grid<X, Y> grid)
      {
        return grid.getColumns().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
      }
    },

    VERTICAL
    {
      @Override
      public <X, Y> List<Cell<X, Y>> getOrigin(Grid<X, Y> grid)
      {
        return grid.getRows().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
      }
    };

    public abstract <X, Y> List<Cell<X, Y>> getOrigin(Grid<X, Y> grid);
  }

  public List<Cell<X, Y>> getColumn(X x)
  {
    return this.yAxis.stream().map((y) -> Grid.cell(x, y)).collect(Collectors.toList());
  }

  public Map<X, List<Cell<X, Y>>> getColumns()
  {
    return this.xAxis.stream().collect(toMap(identity(), this::getColumn));
  }

  public List<Cell<X, Y>> getRow(Y y)
  {
    return this.xAxis.stream().map((x) -> Grid.cell(x, y)).collect(Collectors.toList());
  }

  public Map<Y, List<Cell<X, Y>>> getRows()
  {
    return this.yAxis.stream().collect(toMap(identity(), this::getRow));
  }

  public static class Cell<X, Y>
  {
    private Object x;
    private Object y;

    private Cell(X x, Y y)
    {
      this.x = x;
      this.y = y;
    }

    public Object getX()
    {
      return x;
    }

    public Object getY()
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

  public class GridJoiner<T>
  {
    private T total;

    private GridJoiner(T total)
    {
      this.total = total;
    }

    public <T1> GridJoinerHorizontal<T, T1> joinRight(T1 total, Grid<T, T> grid2)
    {
      return null;
    }
  }

  public class GridJoinerHorizontal<T1, T2>
  {

  }
}
