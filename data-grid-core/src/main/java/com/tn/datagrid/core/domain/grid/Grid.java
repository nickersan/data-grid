package com.tn.datagrid.core.domain.grid;

import static java.util.function.Function.identity;
import static java.util.function.Predicate.isEqual;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collector;

public class Grid<X, Y>
{
  private List<X> xAxis;
  private List<Y> yAxis;

  public Grid(Collection<X> xAxis, Collection<Y> yAxis)
  {
    this.xAxis = List.copyOf(xAxis);
    this.yAxis = List.copyOf(yAxis);
  }

  public List<Cell<X, Y>> getColumn(X x)
  {
    return this.getColumn(x, Grid::cell);
  }

  public <T> List<T> getColumn(X x, BiFunction<X, Y, T> cellFactory)
  {
    if (this.xAxis.stream().noneMatch(isEqual(x)))
    {
      throw new IllegalArgumentException("Unknown: " + x);
    }

    return this.yAxis.stream().map((y) -> cellFactory.apply(x, y)).collect(toList());
  }

  public Map<X, List<Cell<X, Y>>> getColumns()
  {
    return this.xAxis.stream().collect(toMap(identity(), this::getColumn));
  }

  public <T> Map<X, List<T>> getColumns(BiFunction<X, Y, T> cellFactory)
  {
    return this.xAxis.stream().collect(toMap(identity(), (y) -> this.getColumn(y, cellFactory)));
  }

//  public List<Cell<X, Y>> getOrigin()
//  {
//    return this.getOrigin(xAxis.size() > yAxis.size() ? OriginDirection.VERTICAL : OriginDirection.HORIZONTAL);
//  }
//
//  public List<Cell<X, Y>> getOrigin(OriginDirection originDirection)
//  {
//    return originDirection.getOrigin(this);
//  }

  public List<Cell<X, Y>> getRow(Y y)
  {
    return this.getRow(y, Grid::cell);
  }

  public <T> List<T> getRow(Y y, BiFunction<X, Y, T> cellFactory)
  {
    if (this.yAxis.stream().noneMatch(isEqual(y)))
    {
      throw new IllegalArgumentException("Unknown: " + y);
    }

    return this.xAxis.stream().map((x) -> cellFactory.apply(x, y)).collect(toList());
  }

  public Map<Y, List<Cell<X, Y>>> getRows()
  {
    return this.yAxis.stream().collect(toMap(identity(), this::getRow));
  }

  public <T> Map<Y, List<T>> getRows(BiFunction<X, Y, T> cellFactory)
  {
    return this.yAxis.stream().collect(toMap(identity(), (y) -> this.getRow(y, cellFactory)));
  }

  public List<X> getXAxis()
  {
    return this.xAxis;
  }

  public List<Y> getYAxis()
  {
    return this.yAxis;
  }

  public Grid<X, Y> aggregateColumns(Collector<? super X, ?, X> collector)
  {
    return this.aggregateColumns((column) -> true, collector);
  }

  public Grid<X, Y> aggregateColumns(Predicate<X> predicate, Collector<? super X, ?, X> collector)
  {
    List<X> xAxis = new ArrayList<>();
    xAxis.add(this.xAxis.stream().filter(predicate).collect(collector));
    xAxis.addAll(this.xAxis);

    return new Grid<>(xAxis, this.yAxis);
  }

  public Grid<X, Y> aggregateRows(Collector<? super Y, ?, Y> collector)
  {
    return this.aggregateRows((row) -> true, collector);
  }

  public Grid<X, Y> aggregateRows(Predicate<Y> predicate, Collector<? super Y, ?, Y> collector)
  {
    List<Y> yAxis = new ArrayList<>();
    yAxis.add(this.yAxis.stream().filter(predicate).collect(collector));
    yAxis.addAll(this.yAxis);

    return new Grid<>(this.xAxis, yAxis);
  }

  public Grid<X, Y> joinLeft(Grid<X, Y> grid) throws IllegalGridJoinException
  {
    throw new UnsupportedOperationException();
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

  //  public enum OriginDirection
//  {
//    HORIZONTAL
//      {
//        @Override
//        public <X, Y> List<Cell<X, Y>> getOrigin(Grid<X, Y> grid)
//        {
//          return grid.getColumns().values().stream().flatMap(Collection::stream).collect(toList());
//        }
//      },
//
//    VERTICAL
//      {
//        @Override
//        public <X, Y> List<Cell<X, Y>> getOrigin(Grid<X, Y> grid)
//        {
//          return grid.getRows().values().stream().flatMap(Collection::stream).collect(toList());
//        }
//      };
//
//    public abstract <X, Y> List<Cell<X, Y>> getOrigin(Grid<X, Y> grid);
//  }
}

