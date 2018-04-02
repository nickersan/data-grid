package com.tn.datagrid.cao;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.IMap;
import com.hazelcast.core.Member;
import com.hazelcast.core.PartitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tn.datagrid.core.domain.CalculatedIdentity;
import com.tn.datagrid.core.domain.Identity;
import com.tn.datagrid.core.tasks.CalculatorTask;
import com.tn.datagrid.core.util.ConcurrencyUtils;

public class CalculatedValueCao<T> implements ValueGetter<T>
{
  private static final String DEFAULT_EXECUTOR_SERVICE = "default";

  private static Logger logger = LoggerFactory.getLogger(CalculatedValueCao.class);

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
    //return identities.parallelStream().collect(toMap(Function.identity(), this::resolveValue));
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

  private <T1> T1 resolveValue(Identity identity)
  {
    logger.trace("Resolving value for: {}", identity);

    IMap<Identity, T1> map = hazelcastInstance.getMap(identity.getLocation());
    T1 value;

    if (identity instanceof CalculatedIdentity)
    {
      value = map.get(identity);

      if (value == null)
      {
        @SuppressWarnings("unchecked")
        CalculatedIdentity<T1, ?, ?> calculatedIdentity = (CalculatedIdentity)identity;

        logger.trace("Calculating value for: {}", calculatedIdentity);

        value = calculatedIdentity.getOperator().apply(
          resolveValue(calculatedIdentity.getLeftIdentity()),
          resolveValue(calculatedIdentity.getRightIdentity())
        );

        hazelcastInstance.getMap(identity.getLocation()).putAsync(identity, value);
      }
    }
    else
    {
      //TODO: handle missing primary value - Nick Holt 2018/3/22
      value = map.get(identity);
    }

    logger.trace("Got value: {} for: {}", value, identity);

    return value;
  }
}
