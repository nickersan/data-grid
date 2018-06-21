package com.tn.datagrid.core.processors;

import java.util.Map;

import com.hazelcast.map.AbstractEntryProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tn.datagrid.core.domain.Versioned;

public class CreateProcessor<K, V> extends AbstractEntryProcessor<K, Versioned<V>>
{
  private static Logger logger = LoggerFactory.getLogger(CreateProcessor.class);

  private int version;
  private V value;

  public CreateProcessor(int version, V value)
  {
    this.version = version;
    this.value = value;
  }

  @Override
  public Object process(Map.Entry<K, Versioned<V>> entry)
  {
    if (entry.getValue() == null)
    {
      logger.debug("Create: {} -> {} at version: {}", entry.getKey(), this.value, this.version);

      entry.setValue(new Versioned<>(this.version, this.value));
      return true;
    }
    else
    {
      return false;
    }
  }
}
