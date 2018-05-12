package com.tn.datagrid.core.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class VersionedTest
{
  @Test
  public void testGetVersioned()
  {
    Versioned<Integer> version1 = new Versioned<>(2, 100);
    Versioned<Integer> version2 = version1.update(3, 101);
    Versioned<Integer> version3 = version2.update(5, 102);

    assertEquals(Integer.valueOf(102), version3.getClosest(10).orElseThrow(IllegalStateException::new).get());
    assertEquals(Integer.valueOf(102), version3.getClosest(5).orElseThrow(IllegalStateException::new).get());
    assertEquals(Integer.valueOf(101), version3.getClosest(4).orElseThrow(IllegalStateException::new).get());
    assertEquals(Integer.valueOf(101), version3.getClosest(3).orElseThrow(IllegalStateException::new).get());
    assertEquals(Integer.valueOf(100), version3.getClosest(2).orElseThrow(IllegalStateException::new).get());
    assertFalse(version3.getClosest(1).isPresent());
  }
}
