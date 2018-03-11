package com.tn.datagrid.performance;

import java.io.PrintStream;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import org.HdrHistogram.Histogram;

public abstract class CalculationHistogram
{
  protected static final String MAP_PRIMARY_INTEGERS = "primary.integers";
  protected static final String MAP_CALCULATED_INTEGERS = "calculated.integers";

  private static final long WARMUP_TIME = 5000;
  private static final long RUN_TIME = 20000;

  //private Histogram histogram = new Histogram(10_000_000_000L, 3);
  private Histogram histogram = new Histogram(3600_000_000_000L, 3);

  public final void run(PrintStream out)
  {
    HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient();

    try
    {
      setup(hazelcastInstance);

      long startTime = System.currentTimeMillis();

      while (System.currentTimeMillis() - startTime < WARMUP_TIME)
      {
        test();
      }

      this.histogram.reset();

      while (System.currentTimeMillis() - startTime < RUN_TIME)
      {
        test();
      }

      System.out.println("Recorded latencies [in usec]:");

      this.histogram.outputPercentileDistribution(out, 1000.0);
    }
    finally
    {
      hazelcastInstance.shutdown();
    }
  }

  protected abstract void setup(HazelcastInstance hazelcastInstance);

  protected abstract void test();

  protected final void recordValue(long value)
  {
    this.histogram.recordValue(value);
  }
}
