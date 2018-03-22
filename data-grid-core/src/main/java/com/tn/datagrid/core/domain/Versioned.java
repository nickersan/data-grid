package com.tn.datagrid.core.domain;

import java.io.Serializable;
import java.util.Optional;

public class Versioned<T> implements Serializable
{
  private Versioned<T> previous;
  private T value;
  private int version;

  public Versioned(int version, T value)
  {
    this.version = version;
    this.value = value;
  }

  public T get()
  {
    return this.value;
  }

  public Optional<Versioned<T>> getClosest(int version)
  {
    if (this.version <= version)
    {
      return Optional.of(this);
    }
    else if (this.previous != null)
    {
      return this.previous.getClosest(version);
    }
    else
    {
      return Optional.empty();
    }
  }

  public Versioned<T> update(int version, T value)
  {
    if (version > this.version)
    {
      Versioned<T> newVersion = new Versioned<>(version, value);
      newVersion.previous = this;

      return newVersion;
    }
    else
    {
      throw new IllegalArgumentException("Version must be later than current version");
    }
  }
}
