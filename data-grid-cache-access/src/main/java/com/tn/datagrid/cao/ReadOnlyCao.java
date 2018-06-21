package com.tn.datagrid.cao;

import static java.util.stream.Collectors.toMap;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.query.Predicate;

import com.tn.datagrid.core.domain.Location;
import com.tn.datagrid.core.domain.Pair;
import com.tn.datagrid.core.domain.identity.Identity;
import com.tn.datagrid.core.predicate.Version;
import com.tn.datagrid.core.projections.VersionProjection;

public class ReadOnlyCao<V> extends AbstractCao<V> implements ReadCao<V>
{
  public ReadOnlyCao(Function<Location, HazelcastInstance> hazelcastInstanceProvider)
  {
    super(hazelcastInstanceProvider);
  }

  @Override
  public Map<Identity, V> getAll(Collection<Identity> identities) throws CaoException
  {
    return null;
  }

  @Override
  public Map<Identity, V> getAll(Location location, Predicate<Identity, V> predicate)
  {
    return this.getVersionedMap(location).project(new VersionProjection<>(), new Version<>(predicate)).stream()
      .filter((pair) -> pair.getRight() != null)
      .collect(toMap(Pair::getLeft, Pair::getRight));
  }
}
