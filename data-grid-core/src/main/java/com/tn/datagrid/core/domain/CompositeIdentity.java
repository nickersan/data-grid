package com.tn.datagrid.core.domain;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.*;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class CompositeIdentity extends NumericIdentity
{
  private List<Identity> referencingIdentities;

  public CompositeIdentity(String location, int id, Identity... referencingIdentities)
  {
    this(location, id, asList(referencingIdentities));
  }

  public CompositeIdentity(String location, int id, Collection<Identity> referencingIdentities)
  {
    super(location, id);
    this.referencingIdentities = compact(referencingIdentities).stream().sorted().collect(toList());
  }

  @Override
  public boolean equals(Object other)
  {
    return this == other || (
      other != null &&
      this.getClass().equals(other.getClass()) &&
      this.getLocation().equals(((CompositeIdentity)other).getLocation()) &&
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
    return toStringHelper(this)
      .add("location", this.getLocation())
      .add("id", this.get())
      .add("referencingIdentities", this.referencingIdentities)
      .toString();
  }

  private Collection<Identity> compact(Collection<Identity> referencingIdentities)
  {
    Collection<Identity> compactedReferencingIdentities = new HashSet<>();

    for (Identity referencingIdentity : referencingIdentities)
    {
      if (referencingIdentity instanceof CompositeIdentity)
      {
        compactedReferencingIdentities.addAll(((CompositeIdentity)referencingIdentity).referencingIdentities);
      }
      else
      {
        compactedReferencingIdentities.add(referencingIdentity);
      }
    }

    return compactedReferencingIdentities;
  }
}
