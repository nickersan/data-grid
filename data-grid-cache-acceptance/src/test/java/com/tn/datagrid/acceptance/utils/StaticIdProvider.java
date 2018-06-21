package com.tn.datagrid.acceptance.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import com.tn.datagrid.core.domain.Location;

public class StaticIdProvider implements Function<Location, Integer>
{
  private Map<Location, AtomicInteger> ids = new HashMap<>();

  @Override
  public Integer apply(Location location)
  {
    return ids.computeIfAbsent(location, (l) -> new AtomicInteger()).incrementAndGet();
  }
}
