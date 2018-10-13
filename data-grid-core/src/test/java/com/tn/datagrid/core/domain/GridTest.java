package com.tn.datagrid.core.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class GridTest
{
  @Test
  public void testAggregateColumns()
  {
    Grid<String, String> grid = new Grid<>(List.of("A", "B", "C"), List.of("1", "2"));

    assertEquals(
      new Grid<>(List.of("ABC", "A", "B", "C"), List.of("1", "2")),
      grid.aggregateColumns(Collectors.joining())
    );
  }

  @Test
  public void testAggregateRows()
  {
    Grid<String, String> grid = new Grid<>(List.of("A", "B", "C"), List.of("1", "2"));

    assertEquals(
      new Grid<>(List.of("A", "B", "C"), List.of("12", "1", "2")),
      grid.aggregateRows(Collectors.joining())
    );
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
    Grid<String, String> grid1 = new Grid<>(List.of("A", "B", "C"), List.of("1", "2"));
    Grid<String, String> grid2 = new Grid<>(List.of("D", "E", "F"), List.of("1", "2"));
    Grid<String, String> grid3 = new Grid<>(List.of("G", "H", "I"), List.of("1", "2"));


//    assertEquals(
//      new Grid<>(List.of("GT", "G1", "A", "B", "C", "G2", "D", "E", "F", "G3", "G", "H", "I"), List.of("1", "2")),
//      grid1.joinRight("G1").add("G2", grid2).add("G3", grid3).grid("GT")
//    );
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
