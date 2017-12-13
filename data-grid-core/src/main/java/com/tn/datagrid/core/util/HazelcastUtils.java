package com.tn.datagrid.core.util;

import static java.util.stream.Collectors.*;

import java.util.Collection;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class HazelcastUtils
{
  public static Collection<IMap<?, ?>> getMaps(HazelcastInstance hazelcastInstance)
  {
    //noinspection unchecked
    return hazelcastInstance.getDistributedObjects().stream()
      .filter((distributedObject) -> distributedObject instanceof IMap)
      .map((map) -> (IMap<?, ?>)map)
      .collect(toSet());
  }
}
