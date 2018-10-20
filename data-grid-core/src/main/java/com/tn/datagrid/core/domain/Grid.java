package com.tn.datagrid.core.domain;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Grid<X, Y>
{
  private Map<String, X> aggregateColumns;
  private Map<String, Y> aggregateRows;
  private List<X> xAxis;
  private List<Y> yAxis;

  public Grid(List<X> xAxis, List<Y> yAxis)
  {
    this(emptyMap(), emptyMap(), xAxis, yAxis);
  }

  protected Grid(Map<String, X> aggregateColumns, Map<String, Y> aggregateRows, List<X> xAxis, List<Y> yAxis)
  {
    this.aggregateColumns = unmodifiableMap(aggregateColumns);
    this.aggregateRows = unmodifiableMap(aggregateRows);
    this.xAxis = unmodifiableList(xAxis);
    this.yAxis = unmodifiableList(yAxis);
  }

  public boolean isAggregateColumn(X column)
  {
    return this.aggregateColumns.containsValue(column);
  }

  public boolean isNotAggregateColumn(X column)
  {
    return !this.isAggregateColumn(column);
  }

  public Optional<X> getAggregateColumn(String name)
  {
    return Optional.ofNullable(this.aggregateColumns.get(name));
  }

  public Map<String, X> getAggregateColumns()
  {
    return this.aggregateColumns;
  }

  public boolean isAggregateRow(Y row)
  {
    return this.aggregateRows.containsValue(row);
  }

  public boolean isNotAggregateRow(Y row)
  {
    return !this.isAggregateRow(row);
  }

  public Optional<Y> getAggregateRow(String name)
  {
    return Optional.ofNullable(this.aggregateRows.get(name));
  }

  public Map<String, Y> getAggregateRows()
  {
    return this.aggregateRows;
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

  public Grid<X, Y> aggregateColumns(String name, Collector<? super X, ?, X> collector)
  {
    return this.aggregateColumns(name, (column) -> true, collector);
  }

  public Grid<X, Y> aggregateColumns(String name, Predicate<X> filter, Collector<? super X, ?, X> collector)
  {
    X aggregateColumn = this.xAxis.stream().filter(filter).collect(collector);
    Map<String, X> aggregateColumns = new HashMap<>(this.aggregateColumns);
    aggregateColumns.put(name, aggregateColumn);

    List<X> xAxis = new ArrayList<>();
    xAxis.add(aggregateColumn);
    xAxis.addAll(this.xAxis);

    return new Grid<>(aggregateColumns, this.aggregateRows, xAxis, this.yAxis);
  }

  public Grid<X, Y> aggregateRows(String name, Collector<? super Y, ?, Y> collector)
  {
    return this.aggregateRows(name, (row) -> true, collector);
  }

  public Grid<X, Y> aggregateRows(String name, Predicate<Y> filter, Collector<? super Y, ?, Y> collector)
  {
    Y aggregateRow = this.yAxis.stream().filter(filter).collect(collector);
    Map<String, Y> aggregateRows = new HashMap<>(this.aggregateRows);
    aggregateRows.put(name, aggregateRow);

    List<Y> yAxis = new ArrayList<>();
    yAxis.add(aggregateRow);
    yAxis.addAll(this.yAxis);

    return new Grid<>(this.aggregateColumns, aggregateRows, this.xAxis, yAxis);
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
        this.aggregateColumns.equals(((Grid)other).aggregateColumns) &&
        this.aggregateRows.equals(((Grid)other).aggregateRows) &&
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
      .add("aggregateColumns", this.aggregateColumns)
      .add("aggregateRows", this.aggregateRows)
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
