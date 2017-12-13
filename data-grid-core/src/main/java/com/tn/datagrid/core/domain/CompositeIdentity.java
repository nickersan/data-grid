package com.tn.datagrid.core.domain;

import static java.util.Arrays.*;

import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class CompositeIdentity<T, V extends Value<T, V>> extends NumericIdentity<T, V>
{
  private Collection<Identity<?, ?>> referencingIdentities;

  public CompositeIdentity(Type<T, V> type, int id, Identity<?, ?>... referencingIdentities)
  {
    this(type, id, asList(referencingIdentities));
  }

  public CompositeIdentity(Type<T, V> type, int id, Collection<Identity<?, ?>> referencingIdentities)
  {
    super(type, id);
    this.referencingIdentities = compact(referencingIdentities);
  }

  @Override
  public boolean equals(Object other)
  {
    return this == other || (
      other != null &&
      getClass().equals(other.getClass()) &&
      this.get() == ((CompositeIdentity)other).get() &&
      this.referencingIdentities.equals(((CompositeIdentity)other).referencingIdentities)
    );
  }

  @Override
  public int hashCode()
  {
    return this.get();
  }

  @Override
  public String toString()
  {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("id", this.get())
      .append("referencingIdentities", this.referencingIdentities)
      .toString();
  }

  private Collection<Identity<?, ?>> compact(Collection<Identity<?, ?>> referencingIdentities)
  {
    Collection<Identity<?, ?>> compactedReferencingIdentities = new HashSet<>();

    for (Identity<?, ?> referencingIdentity : referencingIdentities)
    {
      if (referencingIdentity instanceof CompositeIdentity)
      {
        compactedReferencingIdentities.addAll(((CompositeIdentity<?, ?>)referencingIdentity).referencingIdentities);
      }
      else
      {
        compactedReferencingIdentities.add(referencingIdentity);
      }
    }

    return compactedReferencingIdentities;
  }
}
