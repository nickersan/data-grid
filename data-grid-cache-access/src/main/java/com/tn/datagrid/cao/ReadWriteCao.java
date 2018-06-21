package com.tn.datagrid.cao;

import java.util.Optional;
import java.util.function.Function;

import com.hazelcast.core.HazelcastInstance;

import com.tn.datagrid.core.domain.Location;
import com.tn.datagrid.core.domain.Pair;
import com.tn.datagrid.core.domain.identity.ChildIdentity;
import com.tn.datagrid.core.domain.identity.Identity;
import com.tn.datagrid.core.domain.identity.NumericIdentity;
import com.tn.datagrid.core.processors.CreateProcessor;

public class ReadWriteCao<V> extends ReadOnlyCao<V> implements WriteCao<V>
{
  private Function<Location, Integer> idProvider;
  private Function<Location, Integer> versionProvider;

  public ReadWriteCao(Function<Location, HazelcastInstance> hazelcastInstanceProvider, Function<Location, Integer> idProvider, Function<Location, Integer> versionProvider)
  {
    super(hazelcastInstanceProvider);
    this.idProvider = idProvider;
    this.versionProvider = versionProvider;
  }

  @Override
  public Identity create(Location location, V value) throws CaoException
  {
    return doCreate(new NumericIdentity(location, idProvider.apply(location)), value);
  }

  @Override
  public Identity create(Identity parentIdentity, V value) throws CaoException
  {
    return doCreate(new ChildIdentity(parentIdentity, idProvider.apply(parentIdentity.getLocation())), value);
  }

  @Override
  public void update(Identity identity, V value) throws CaoException
  {

  }

  @Override
  public Optional<Pair<Identity, V>> delete(Identity identity, boolean hard)
  {
    //TODO: implement soft delete

    V value = getMap(identity).remove(identity);
    return value != null ? Optional.of(new Pair<>(identity, value)) : Optional.empty();
  }

  private Identity doCreate(Identity identity, V value) throws CaoException
  {
    if (!Boolean.TRUE.equals(getMap(identity.getLocation()).executeOnKey(identity, new CreateProcessor<>(this.versionProvider.apply(identity.getLocation()), value))))
    {
      throw new CaoException("Failed to create entry at location: " + identity.getLocation() + " for value: " + value);
    }

    return identity;
  }
}
