package com.tn.datagrid.core.processors;

import java.util.Map;

import com.hazelcast.map.AbstractEntryProcessor;

import com.tn.datagrid.core.domain.Versioned;

public class CreateProcessor<K, V> extends AbstractEntryProcessor<K, Versioned<V>>
{
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
      entry.setValue(new Versioned<>(version, this.value));
      return true;
    }
    else
    {
      return false;
    }
  }
}
