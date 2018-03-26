package com.tn.datagrid.cache.loaders;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.NearCacheConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MapLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tn.datagrid.core.domain.CalculatedIdentity;
import com.tn.datagrid.core.domain.Identity;

public class CalculatedValueMapLoader<T, LT, RT> implements MapLoader<CalculatedIdentity<T, LT, RT>, T>
{
  private static Logger logger = LoggerFactory.getLogger(CalculatedValueMapLoader.class);

  private HazelcastInstance hazelcastInstance;

  public CalculatedValueMapLoader()
  {
    EvictionConfig evictionConfig = new EvictionConfig()
      .setMaximumSizePolicy(EvictionConfig.MaxSizePolicy.ENTRY_COUNT)
      .setEvictionPolicy(EvictionPolicy.LFU)
      .setSize(1000);

    NearCacheConfig nearCacheConfig = new NearCacheConfig()
      .setName("primary.*")
      .setInMemoryFormat(InMemoryFormat.OBJECT)
      .setInvalidateOnChange(true)
      .setEvictionConfig(evictionConfig);

    Map<String, NearCacheConfig> nearCacheConfigMap = new HashMap<>();
    nearCacheConfigMap.put(nearCacheConfig.getName(), nearCacheConfig);

    ClientConfig clientConfig = new ClientConfig();
    clientConfig.setNearCacheConfigMap(nearCacheConfigMap);
    
    this.hazelcastInstance = HazelcastClient.newHazelcastClient();
  }

  @Override
  public T load(CalculatedIdentity<T, LT, RT> identity)
  {
    logger.debug("Loading calculated value: {}", identity);

    return identity.getOperator().apply(
      getValue(identity.getLeftIdentity()),
      getValue(identity.getRightIdentity())
    );
  }

  @Override
  public Map<CalculatedIdentity<T, LT, RT>, T> loadAll(Collection<CalculatedIdentity<T, LT, RT>> identities)
  {
    Map<CalculatedIdentity<T, LT, RT>, T> values = new HashMap<>();

    for (CalculatedIdentity<T, LT, RT> identity : identities)
    {
      values.put(identity, load(identity));
    }

    return values;
  }

  @Override
  public Iterable<CalculatedIdentity<T, LT, RT>> loadAllKeys()
  {
    return Collections.emptyList();
  }

  private <T1> T1 getValue(Identity identity)
  {
    logger.debug("Getting value for: {}", identity);

    T1 value;

    if (identity instanceof CalculatedIdentity)
    {
      IMap<Identity, T1> calculated = hazelcastInstance.getMap(identity.getLocation());
      value = calculated.get(identity);
    }
    else
    {
      IMap<Identity, T1> primaryIntegers = hazelcastInstance.getMap(identity.getLocation());
      value = primaryIntegers.get(identity);
    }

    logger.debug("Got value: {}", value);

    return value;
  }
}
