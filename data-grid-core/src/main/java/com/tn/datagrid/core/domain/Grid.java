package com.tn.datagrid.core.domain;

import static java.util.Collections.unmodifiableList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
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

  public List<Cell<X, Y>> getColumn(X x)
  {
    return this.yAxis.stream().map((y) -> Grid.cell(x, y)).collect(Collectors.toList());
  }

  public Map<X, List<Cell<X, Y>>> getColumns()
  {
    return this.xAxis.stream().collect(toMap(identity(), this::getColumn));
  }

  public List<Cell<X, Y>> getOrigin()
  {
    return this.getOrigin(xAxis.size() > yAxis.size() ? OriginDirection.VERTICAL : OriginDirection.HORIZONTAL);
  }

  public List<Cell<X, Y>> getOrigin(OriginDirection originDirection)
  {
    return originDirection.getOrigin(this);
  }

  public List<Cell<X, Y>> getRow(Y y)
  {
    return this.xAxis.stream().map((x) -> Grid.cell(x, y)).collect(Collectors.toList());
  }

  public Map<Y, List<Cell<X, Y>>> getRows()
  {
    return this.yAxis.stream().collect(toMap(identity(), this::getRow));
  }

  public Grid<X, Y> aggregateColumns(Collector<? super X, ?, X> collector)
  {
    List<X> xAxis = new ArrayList<>();
    xAxis.add(this.xAxis.stream().collect(collector));
    xAxis.addAll(this.xAxis);

    return new Grid<>(xAxis, this.yAxis);
  }

  public Grid<X, Y> aggregateRows(Collector<? super Y, ?, Y> collector)
  {
    List<Y> yAxis = new ArrayList<>();
    yAxis.add(this.yAxis.stream().collect(collector));
    yAxis.addAll(this.yAxis);

    return new Grid<>(this.xAxis, yAxis);
  }

  protected static <X, Y> Cell<X, Y> cell(X x, Y y)
  {
    return new Cell<>(x, y);
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

  public enum OriginDirection
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
}
