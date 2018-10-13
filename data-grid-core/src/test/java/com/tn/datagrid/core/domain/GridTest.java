package com.tn.datagrid.core.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class GridTest
{
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
      grid.getOrigin(Grid.AggregateDirection.HORIZONTAL)
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
      grid.getOrigin(Grid.AggregateDirection.VERTICAL)
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
