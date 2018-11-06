package com.tn.datagrid.core.domain.grid;

import static java.util.stream.Collectors.joining;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class GridTest
{
  @Test
  public void testAggregateColumns()
  {
    Grid<String, String> grid = new Grid<>(List.of("A", "B", "C"), List.of("1", "2"));
    Grid<String, String> aggregateGrid = grid.aggregateColumns(joining());

    assertEquals(List.of("ABC", "A", "B", "C"), aggregateGrid.getXAxis());
  }

  @Test
  public void testAggregateColumnsWithFilter()
  {
    Grid<String, String> grid = new Grid<>(List.of("A", "B", "C"), List.of("1", "2"));
    Grid<String, String> aggregateGrid = grid.aggregateColumns("A"::equals, joining());

    assertEquals(List.of("A", "A", "B", "C"), aggregateGrid.getXAxis());
  }

  @Test
  public void testAggregateRows()
  {
    Grid<String, String> grid = new Grid<>(List.of("A", "B", "C"), List.of("1", "2"));
    Grid<String, String> aggregateGrid = grid.aggregateRows(joining());

    assertEquals(List.of("12", "1", "2"), aggregateGrid.getYAxis());
  }

  @Test
  public void testAggregateRowsWithFilter()
  {
    Grid<String, String> grid = new Grid<>(List.of("A", "B", "C"), List.of("1", "2"));
    Grid<String, String> aggregateGrid = grid.aggregateRows("1"::equals, joining());

    assertEquals(List.of("1", "1", "2"), aggregateGrid.getYAxis());
  }

  @Test
  public void testColumns()
  {
    Grid<String, String> grid = new Grid<>(List.of("A", "B", "C"), List.of("1", "2"));

    assertEquals(
      List.of(Grid.cell("A", "1"), Grid.cell("A", "2")),
      grid.getColumn("A")
    );

    assertEquals(
      List.of(Grid.cell("B", "1"), Grid.cell("B", "2")),
      grid.getColumn("B")
    );

    assertEquals(
      List.of(Grid.cell("C", "1"), Grid.cell("C", "2")),
      grid.getColumn("C")
    );

    assertEquals(
      Map.of(
        "A", List.of(Grid.cell("A", "1"), Grid.cell("A", "2")),
        "B", List.of(Grid.cell("B", "1"), Grid.cell("B", "2")),
        "C", List.of(Grid.cell("C", "1"), Grid.cell("C", "2"))
      ),
      grid.getColumns()
    );
  }

  @Test
  public void testRows()
  {
    Grid<String, String> grid = new Grid<>(List.of("A", "B", "C"), List.of("1", "2"));

    assertEquals(
      List.of(Grid.cell("A", "1"), Grid.cell("B", "1"), Grid.cell("C", "1")),
      grid.getRow("1")
    );

    assertEquals(
      List.of(Grid.cell("A", "2"), Grid.cell("B", "2"), Grid.cell("C", "2")),
      grid.getRow("2")
    );

    assertEquals(
      Map.of(
        "1", List.of(Grid.cell("A", "1"), Grid.cell("B", "1"), Grid.cell("C", "1")),
        "2", List.of(Grid.cell("A", "2"), Grid.cell("B", "2"), Grid.cell("C", "2"))
      ),
      grid.getRows()
    );
  }

  @Test
  public void testJoin() throws Exception
  {
//    Grid<String, String> grid1 = new Grid<>(List.of("A", "B", "C"), List.of("1", "2", "3"));
//    Grid<String, String> grid2 = new Grid<>(List.of("D", "E", "F"), List.of("1", "2", "4"));
//    Grid<String, String> grid3 = new Grid<>(List.of("G", "H", "I"), List.of("1", "2"));
//
//    assertEquals(
//      new Grid<>(List.of("A", "B", "C", "D", "E", "F"/*, "G", "H", "I" */), List.of("1", "2", "3", "4")),
//      grid2.joinLeft(grid1)//.joinRight(grid3)
//    );
  }

//  @Test
//  public void testJoinLeftMismatchedYAxis()
//  {
//
//  }
//
//  @Test
//  public void testOriginHorizontal()
//  {
//    Grid<String, String> grid = new Grid<>(List.of("A", "B"), List.of("1", "2", "3"));
//
//    List<Grid.Cell<String, String>> expectedCells = new ArrayList<>();
//    expectedCells.addAll(grid.getColumn("A"));
//    expectedCells.addAll(grid.getColumn("B"));
//
//    assertEquals(
//      expectedCells,
//      grid.getOrigin()
//    );
//
//    assertEquals(
//      expectedCells,
//      grid.getOrigin(Grid.OriginDirection.HORIZONTAL)
//    );
//  }
//
//  @Test
//  public void testOriginVertical()
//  {
//    Grid<String, String> grid = new Grid<>(List.of("A", "B", "C"), List.of("1", "2"));
//
//    List<Grid.Cell<String, String>> expectedCells = new ArrayList<>();
//    expectedCells.addAll(grid.getRow("1"));
//    expectedCells.addAll(grid.getRow("2"));
//
//    assertEquals(
//      expectedCells,
//      grid.getOrigin()
//    );
//
//    assertEquals(
//      expectedCells,
//      grid.getOrigin(Grid.OriginDirection.VERTICAL)
//    );
//  }

  @Test
  public void testRealExample() throws Exception
  {






//    Identity expectedAggregateIdentity = new AggregateIdentity<>(aggregateValuesLocation, Operators.sum(), q2, apr, may, jun);
//
//    grid1.aggregateRows((x) -> !(x instanceof AggregateIdentity), AggregateIdentity.collector(aggregateValuesLocation, Operators.sum(), small));
//
//    assertEquals(
//      List.of(expectedAggregateIdentity, apr, may, jun),
//      grid1.getYAxis()
//    );
  }


}
