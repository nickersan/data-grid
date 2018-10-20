package com.tn.datagrid.core.domain;

import static java.util.Collections.emptyMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class GridTest
{
  @Test
  public void testAggregateColumns()
  {
    String aggregateColumnName = "Test";

    Grid<String, String> grid = new Grid<>(List.of("A", "B", "C"), List.of("1", "2"));
    Grid<String, String> aggregateGrid = grid.aggregateColumns(aggregateColumnName, Collectors.joining());

    assertEquals(new Grid<>(Map.of(aggregateColumnName, "ABC"), emptyMap(), List.of("ABC", "A", "B", "C"), List.of("1", "2")), aggregateGrid);
    assertEquals("ABC", aggregateGrid.getAggregateColumn(aggregateColumnName).get());
    assertEquals("ABC", aggregateGrid.getAggregateColumns().get(aggregateColumnName));

    assertTrue(aggregateGrid.isAggregateColumn("ABC"));
    assertFalse(aggregateGrid.isAggregateColumn("A"));
    assertFalse(aggregateGrid.isAggregateColumn("B"));
    assertFalse(aggregateGrid.isAggregateColumn("C"));

    assertFalse(aggregateGrid.isNotAggregateColumn("ABC"));
    assertTrue(aggregateGrid.isNotAggregateColumn("A"));
    assertTrue(aggregateGrid.isNotAggregateColumn("B"));
    assertTrue(aggregateGrid.isNotAggregateColumn("C"));
  }

  @Test
  public void testAggregateColumnsWithFilter()
  {
    String aggregateColumnName = "Test";

    Grid<String, String> grid = new Grid<>(List.of("A", "B", "C"), List.of("1", "2"));
    Grid<String, String> aggregateGrid = grid.aggregateColumns(aggregateColumnName, "A"::equals, Collectors.joining());

    assertEquals(new Grid<>(Map.of(aggregateColumnName, "A"), emptyMap(), List.of("A", "A", "B", "C"), List.of("1", "2")), aggregateGrid);
    assertEquals("A", aggregateGrid.getAggregateColumn(aggregateColumnName).get());
    assertEquals("A", aggregateGrid.getAggregateColumns().get(aggregateColumnName));

    assertTrue(aggregateGrid.isAggregateColumn("A"));
    assertFalse(aggregateGrid.isAggregateColumn("B"));
    assertFalse(aggregateGrid.isAggregateColumn("C"));

    assertFalse(aggregateGrid.isNotAggregateColumn("A"));
    assertTrue(aggregateGrid.isNotAggregateColumn("B"));
    assertTrue(aggregateGrid.isNotAggregateColumn("C"));
  }

  @Test
  public void testAggregateRows()
  {
    String aggregateRowName = "Test";

    Grid<String, String> grid = new Grid<>(List.of("A", "B", "C"), List.of("1", "2"));
    Grid<String, String> aggregateGrid = grid.aggregateRows(aggregateRowName, Collectors.joining());

    assertEquals(new Grid<>(emptyMap(), Map.of(aggregateRowName, "12"), List.of("A", "B", "C"), List.of("12", "1", "2")), aggregateGrid);
    assertEquals("12", aggregateGrid.getAggregateRow(aggregateRowName).get());
    assertEquals("12", aggregateGrid.getAggregateRows().get(aggregateRowName));

    assertTrue(aggregateGrid.isAggregateRow("12"));
    assertFalse(aggregateGrid.isAggregateRow("1"));
    assertFalse(aggregateGrid.isAggregateRow("2"));

    assertFalse(aggregateGrid.isNotAggregateRow("12"));
    assertTrue(aggregateGrid.isNotAggregateRow("1"));
    assertTrue(aggregateGrid.isNotAggregateRow("2"));
  }

  @Test
  public void testAggregateRowsWithFilter()
  {
    String aggregateRowName = "Test";

    Grid<String, String> grid = new Grid<>(List.of("A", "B", "C"), List.of("1", "2"));
    Grid<String, String> aggregateGrid = grid.aggregateRows(aggregateRowName, "1"::equals, Collectors.joining());

    assertEquals(new Grid<>(emptyMap(), Map.of(aggregateRowName, "1"), List.of("A", "B", "C"), List.of("1", "1", "2")), aggregateGrid);
    assertEquals("1", aggregateGrid.getAggregateRow(aggregateRowName).get());
    assertEquals("1", aggregateGrid.getAggregateRows().get(aggregateRowName));

    assertTrue(aggregateGrid.isAggregateRow("1"));
    assertFalse(aggregateGrid.isAggregateRow("2"));

    assertFalse(aggregateGrid.isNotAggregateRow("1"));
    assertTrue(aggregateGrid.isNotAggregateRow("2"));
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
  public void testJoinRight()
  {
    Grid<String, String> grid1 = new Grid<>(List.of("A", "B", "C"), List.of("1", "2", "3"));
    Grid<String, String> grid2 = new Grid<>(List.of("D", "E", "F"), List.of("1", "2", "4"));
    Grid<String, String> grid3 = new Grid<>(List.of("G", "H", "I"), List.of("1", "2"));

    assertEquals(
      new Grid<>(List.of("A", "B", "C", "D", "E", "F", "G", "H", "I"), List.of("1", "2", "3", "4")),
      grid1.joinRight("G1").add("G2", grid2).add("G3", grid3).grid("GT")
    );
  }

  @Test
  public void testJoinLeftMismatchedYAxis()
  {

  }

  @Test
  public void testOriginHorizontal()
  {
    Grid<String, String> grid = new Grid<>(List.of("A", "B"), List.of("1", "2", "3"));

    List<Grid.Cell<String, String>> expectedCells = new ArrayList<>();
    expectedCells.addAll(grid.getColumn("A"));
    expectedCells.addAll(grid.getColumn("B"));

    assertEquals(
      expectedCells,
      grid.getOrigin()
    );

    assertEquals(
      expectedCells,
      grid.getOrigin(Grid.OriginDirection.HORIZONTAL)
    );
  }

  @Test
  public void testOriginVertical()
  {
    Grid<String, String> grid = new Grid<>(List.of("A", "B", "C"), List.of("1", "2"));

    List<Grid.Cell<String, String>> expectedCells = new ArrayList<>();
    expectedCells.addAll(grid.getRow("1"));
    expectedCells.addAll(grid.getRow("2"));

    assertEquals(
      expectedCells,
      grid.getOrigin()
    );

    assertEquals(
      expectedCells,
      grid.getOrigin(Grid.OriginDirection.VERTICAL)
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
}
