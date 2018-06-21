package com.tn.datagrid.core.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tn.datagrid.core.domain.Location;

public class HazelcastInstancePool
{
  private static Logger logger = LoggerFactory.getLogger(HazelcastInstancePool.class);

  private Function<Location, HazelcastInstance> hazelcastInstanceProvider;
  private Map<Location, HazelcastInstance> hazelcastInstances;

  public HazelcastInstancePool(Function<Location, HazelcastInstance> hazelcastInstanceProvider)
  {
    this.hazelcastInstanceProvider = hazelcastInstanceProvider;
    this.hazelcastInstances = new ConcurrentHashMap<>();
  }

  public HazelcastInstance getHazelcastInstance(Location location)
  {
    logger.debug("Get hazelcast instance for: {}", location);
    return this.hazelcastInstances.computeIfAbsent(location, this.hazelcastInstanceProvider);
  }

  public void shutdown()
  {
    this.hazelcastInstances.values().forEach(HazelcastInstance::shutdown);
  }

  public static class SingletonHazelcastInstanceProvider
  {
    private HazelcastInstance hazelcastInstance;

    public SingletonHazelcastInstanceProvider(HazelcastInstance hazelcastInstance)
    {
      this.hazelcastInstance = hazelcastInstance;
    }

    public HazelcastInstance getHazelcastInstance(Location location)
    {
      logger.debug("Returning singleton hazelcast instance for: {}", location);
      return this.hazelcastInstance;
    }
  }
}
