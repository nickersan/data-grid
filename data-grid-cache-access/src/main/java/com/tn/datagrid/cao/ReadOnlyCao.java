package com.tn.datagrid.cao;

import static java.util.stream.Collectors.toMap;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import com.hazelcast.core.HazelcastException;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.query.Predicate;

import com.tn.datagrid.core.domain.Location;
import com.tn.datagrid.core.domain.Pair;
import com.tn.datagrid.core.domain.identity.Identity;
import com.tn.datagrid.core.predicate.At;
import com.tn.datagrid.core.projections.VersionProjection;

public class ReadOnlyCao<V> extends AbstractCao<V> implements ReadCao<V>
{
  public ReadOnlyCao(Function<Location, HazelcastInstance> hazelcastInstanceProvider)
  {
    super(hazelcastInstanceProvider);
  }

  @Override
  public Map<Identity, V> getAllAt(Collection<Identity> identities, int version) throws CaoException
  {
    return null;
  }

  @Override
  public Map<Identity, V> getAllAt(Location location, Predicate<Identity, V> predicate, int version) throws CaoException
  {
    try
    {
      return this.getMap(location).project(new VersionProjection<>(), new At<>(predicate, version)).stream()
        .filter((pair) -> pair.getRight() != null)
        .collect(toMap(Pair::getLeft, Pair::getRight));
    }
    catch (HazelcastException e)
    {
      throw new CaoException("Failed to getAll at: " + location + " with: " + predicate, e);
    }
  }

//  @Override
//  public Map<Identity, V> getAll(Collection<Identity> identities) throws CaoException
//  {
//    return null;
//  }
//
//  @Override
//  public Map<Identity, V> getAll(Location location, Predicate<Identity, V> predicate) throws CaoException
//  {
//    try
//    {
//      return this.getMap(location).project(new VersionProjection<>(), predicate instanceof At ? (At<V>) predicate : new At<>(predicate)).stream()
//        .filter((pair) -> pair.getRight() != null)
//        .collect(toMap(Pair::getLeft, Pair::getRight));
//    }
//    catch (HazelcastException e)
//    {
//      throw new CaoException("Failed to getAll at: " + location + " with: " + predicate, e);
//    }
//  }
}
