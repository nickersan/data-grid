package com.tn.datagrid.core.domain.identity;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.*;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.tn.datagrid.core.domain.Location;

public class CompositeIdentity extends AbstractIdentity
{
  private List<Identity> referencingIdentities;

  public CompositeIdentity(Location location, Identity... referencingIdentities)
  {
    this(location, asList(referencingIdentities));
  }

  public CompositeIdentity(Location location, Collection<Identity> referencingIdentities)
  {
    super(location);
    this.referencingIdentities = compact(referencingIdentities).stream().sorted().collect(toList());
  }

  @Override
  public boolean equals(Object other)
  {
    return this == other || (
      other != null &&
      this.getClass().equals(other.getClass()) &&
      this.getLocation().equals(((CompositeIdentity)other).getLocation()) &&
      this.referencingIdentities.equals(((CompositeIdentity)other).referencingIdentities)
    );
  }

  @Override
  public int hashCode()
  {
    return this.referencingIdentities.hashCode();
  }

  @Override
  public String toString()
  {
    return toStringHelper(this)
      .add("location", this.getLocation())
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
