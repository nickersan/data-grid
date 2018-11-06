package com.tn.datagrid.core.domain.grid;

import static java.util.stream.Collectors.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.tn.datagrid.core.domain.Location;
import com.tn.datagrid.core.domain.Operators;
import com.tn.datagrid.core.domain.identity.AggregateIdentity;
import com.tn.datagrid.core.domain.identity.ChildIdentity;
import com.tn.datagrid.core.domain.identity.CompositeIdentity;
import com.tn.datagrid.core.domain.identity.Identity;
import com.tn.datagrid.core.domain.identity.IdentityCombiner;
import com.tn.datagrid.core.domain.identity.NumericIdentity;

public class GridRealWorldTest
{
  private static final Identity YEAR_LOCATION = new NumericIdentity(new Location("calenders"), 1);
  private static final Identity Q1 = new ChildIdentity(YEAR_LOCATION, 2);
  private static final Identity JAN = new ChildIdentity(Q1, 3);
  private static final Identity FEB = new ChildIdentity(Q1, 4);
  private static final Identity MAR = new ChildIdentity(Q1, 5);
  private static final Identity Q2 = new ChildIdentity(YEAR_LOCATION, 6);
  private static final Identity APR = new ChildIdentity(Q2, 7);
  private static final Identity MAY = new ChildIdentity(Q2, 8);
  private static final Identity JUN = new ChildIdentity(Q2, 9);

  private static final Identity PRODUCTS_LOCATION = new NumericIdentity(new Location("products"), 10);
  private static final Identity LARGE = new ChildIdentity(PRODUCTS_LOCATION, 11);
  private static final Identity BLUE = new ChildIdentity(LARGE, 12);
  private static final Identity GREEN = new ChildIdentity(LARGE, 13);
  private static final Identity SMALL = new ChildIdentity(PRODUCTS_LOCATION, 14);
  private static final Identity RED = new ChildIdentity(SMALL, 15);
  private static final Identity ORANGE = new ChildIdentity(SMALL, 16);

  private static final Location VALUES_LOCATION = new Location("values");
  private static final Location AGGREGATE_VALUES_LOCATION = new Location("aggregateValues");

  @Test
  public void testComplexGridConstruction() throws Exception
  {
    Grid<Identity, Identity> grid1 = testCreateGrid(List.of(APR, MAY, JUN), List.of(RED, ORANGE));
    Grid<Identity, Identity> grid2 = testAggregatingColumns(grid1, Q2, List.of(APR, MAY, JUN));
    Grid<Identity, Identity> grid3 = testAggregatingRows(grid2, SMALL, List.of(RED, ORANGE));
  }

  private Grid<Identity, Identity> testCreateGrid(List<Identity> xAxis, List<Identity> yAxis)
  {
    Grid<Identity, Identity> grid = new Grid<>(xAxis, yAxis);

    Map<Identity, List<Identity>> expectedColumns = new HashMap<>();
    xAxis.forEach(
      (x) ->
      {
        List<Identity> column = new ArrayList<>();
        yAxis.forEach((y) -> column.add(new CompositeIdentity(VALUES_LOCATION, x, y)));
        expectedColumns.put(x, column);
      }
    );

    assertEquals(expectedColumns, grid.getColumns((x, y) -> new CompositeIdentity(VALUES_LOCATION, x, y)));

    Map<Identity, List<Identity>> expectedRows = new HashMap<>();
    yAxis.forEach(
      (y) ->
      {
        List<Identity> row = new ArrayList<>();
        xAxis.forEach((x) -> row.add(new CompositeIdentity(VALUES_LOCATION, x, y)));
        expectedRows.put(y, row);
      }
    );

    assertEquals(expectedRows, grid.getRows((x, y) -> new CompositeIdentity(VALUES_LOCATION, x, y)));

    return grid;
  }

  private Grid<Identity, Identity> testAggregatingColumns(Grid<Identity, Identity> grid, Identity rootIdentity, List<Identity> aggregateIdentities)
  {
    Identity expectedAggregateColumnIdentity = new AggregateIdentity<>(AGGREGATE_VALUES_LOCATION, Operators.sum(), rootIdentity, aggregateIdentities);

    List<Identity> expectedXAxis = new ArrayList<>();
    expectedXAxis.add(expectedAggregateColumnIdentity);
    expectedXAxis.addAll(aggregateIdentities);

    Grid<Identity, Identity> gridWithAggregateColumn = grid.aggregateColumns(AggregateIdentity.collector(AGGREGATE_VALUES_LOCATION, Operators.sum(), rootIdentity));

    assertEquals(
      expectedXAxis,
      gridWithAggregateColumn.getXAxis()
    );

    List<Identity> expectedColumn = new ArrayList<>();

    gridWithAggregateColumn.getYAxis().forEach(
      (y) ->
      {
        List<Identity> expectedRow = aggregateIdentities.stream().map((x) -> IdentityCombiner.combine(VALUES_LOCATION, x, y)).collect(toList());
        Identity expectedAggregate = new AggregateIdentity<>(AGGREGATE_VALUES_LOCATION, Operators.sum(), rootIdentity, expectedRow);
        expectedRow.add(0, expectedAggregate);

        expectedColumn.add(expectedAggregate);

        assertEquals(expectedRow, gridWithAggregateColumn.getRow(y, (x, y1) -> IdentityCombiner.combine(VALUES_LOCATION, x, y1)));
      }
    );

    assertEquals(expectedColumn, gridWithAggregateColumn.getColumn(expectedAggregateColumnIdentity, (x, y) -> IdentityCombiner.combine(VALUES_LOCATION, x, y)));

    return gridWithAggregateColumn;
  }

  private Grid<Identity, Identity> testAggregatingRows(Grid<Identity, Identity> grid, Identity rootIdentity, List<Identity> aggregateIdentities)
  {
    Identity expectedAggregateRowIdentity = new AggregateIdentity<>(AGGREGATE_VALUES_LOCATION, Operators.sum(), rootIdentity, aggregateIdentities);

    List<Identity> expectedYAxis = new ArrayList<>();
    expectedYAxis.add(expectedAggregateRowIdentity);
    expectedYAxis.addAll(aggregateIdentities);

    Grid<Identity, Identity> gridWithAggregateRow = grid.aggregateRows(AggregateIdentity.collector(AGGREGATE_VALUES_LOCATION, Operators.sum(), rootIdentity));

    assertEquals(
      expectedYAxis,
      gridWithAggregateRow.getYAxis()
    );

    List<Identity> expectedRow = new ArrayList<>();

    gridWithAggregateRow.getXAxis().forEach(
      (x) ->
      {
        List<Identity> expectedColumn = aggregateIdentities.stream().map((y) -> IdentityCombiner.combine(VALUES_LOCATION, x, y)).collect(toList());
        Identity expectedAggregate = new AggregateIdentity<>(AGGREGATE_VALUES_LOCATION, Operators.sum(), rootIdentity, expectedColumn);
        expectedColumn.add(0, expectedAggregate);

        expectedRow.add(expectedAggregate);

        assertEquals(expectedColumn, gridWithAggregateRow.getColumn(x, (x1, y) -> IdentityCombiner.combine(VALUES_LOCATION, x1, y)));
      }
    );

    assertEquals(expectedRow, gridWithAggregateRow.getRow(expectedAggregateRowIdentity, (x, y) -> IdentityCombiner.combine(VALUES_LOCATION, x, y)));

    return gridWithAggregateRow;
  }
}
