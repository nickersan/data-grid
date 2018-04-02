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

import com.tn.datagrid.core.domain.Identity;
import com.tn.datagrid.core.tasks.CalculatorTask;
import com.tn.datagrid.core.util.ConcurrencyUtils;

public class CalculatedValueCao<T> implements ValueGetter<T>
{
  private static final String DEFAULT_EXECUTOR_SERVICE = "default";

  private String executorService;
  private HazelcastInstance hazelcastInstance;

  public CalculatedValueCao(HazelcastInstance hazelcastInstance)
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

  private Map<Identity, T> getAllWithTask(Collection<Identity> identities) throws CaoException
  {
    try
    {
      PartitionService partitionService = this.hazelcastInstance.getPartitionService();
      Map<Member, List<Identity>> identitiesByMember = identities.stream().collect(groupingBy((identity) -> partitionService.getPartition(identity).getOwner()));

      IExecutorService executorService = this.hazelcastInstance.getExecutorService(this.executorService);

      return ConcurrencyUtils.getMap(
        identitiesByMember.entrySet().stream()
          .map((entry) -> executorService.submitToMember(new CalculatorTask<T>(entry.getValue()), entry.getKey()))
          .collect(toList())
      );
    }
    catch (InterruptedException | ExecutionException e)
    {
      throw new CaoException("Failed to get while waiting for future to complete", e);
    }
  }
}
