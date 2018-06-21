package com.tn.datagrid.cao;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.Member;
import com.hazelcast.core.PartitionService;
import com.hazelcast.query.Predicate;

import com.tn.datagrid.core.domain.Location;
import com.tn.datagrid.core.domain.identity.Identity;
import com.tn.datagrid.core.tasks.QueryTask;
import com.tn.datagrid.core.util.ConcurrencyUtils;

public class ValueCao<T> implements ReadCao<T>
{
  private static final String DEFAULT_EXECUTOR_SERVICE = "default";

  private String executorService;
  private HazelcastInstance hazelcastInstance;

  public ValueCao(HazelcastInstance hazelcastInstance)
  {
    this.hazelcastInstance = hazelcastInstance;
    this.executorService = DEFAULT_EXECUTOR_SERVICE;
  }

  public void setExecutorService(String executorService)
  {
    this.executorService = executorService;
  }

  @Override
  public Map<Identity, T> getAll(Collection<Identity> identities) throws CaoException
  {
    return getAllWithTask(identities);
  }

  @Override
  public Map<Identity, T> getAllAt(Collection<Identity> identities, int version) throws CaoException
  {
    return null;
  }

  @Override
  public Map<Identity, T> getAll(Location location, Predicate<Identity, T> predicate) throws CaoException
  {
    return null;
  }

  @Override
  public Map<Identity, T> getAllAt(Location location, Predicate<Identity, T> predicate, int version) throws CaoException
  {
    return null;
  }

  private Map<Identity, T> getAllWithTask(Collection<Identity> identities) throws CaoException
  {
    try
    {
      PartitionService partitionService = this.hazelcastInstance.getPartitionService();
      Map<Member, List<Identity>> identitiesByMember = identities.stream().collect(groupingBy((identity) -> partitionService.getPartition(identity).getOwner()));

      IExecutorService executorService = this.hazelcastInstance.getExecutorService(this.executorService);

      return ConcurrencyUtils.getMap(
        identitiesByMember.entrySet().stream()
          .map((entry) -> executorService.submitToMember(new QueryTask<T>(entry.getValue()), entry.getKey()))
          .collect(toList())
      );
    }
    catch (InterruptedException | ExecutionException e)
    {
      throw new CaoException("Failed to get while waiting for future to complete", e);
    }
  }
}
