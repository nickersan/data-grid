package com.tn.datagrid.core.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ConcurrencyUtils
{
  public static <K, V> Map<K, V> getMap(Collection<Future<Map<K, V>>> futures) throws ExecutionException, InterruptedException
  {
    Map<K, V> map = new HashMap<>();

    for (Future<Map<K, V>> future : futures)
    {
      map.putAll(future.get());
    }

    return map;
  }
}
