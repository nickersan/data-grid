package com.tn.datagrid.performance;

import java.io.PrintStream;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import org.HdrHistogram.Histogram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CalculationHistogram
{
  protected static final String MAP_PRIMARY_INTEGERS = "primary.integers";
  protected static final String MAP_CALCULATED_INTEGERS = "calculated.integers";

  private static final long WARMUP_TIME = 30000;
  private static final long RUN_TIME = 180000;

  private static Logger logger = LoggerFactory.getLogger(CalculationHistogram.class);

  //private Histogram histogram = new Histogram(10_000_000_000L, 3);
  private Histogram histogram = new Histogram(3600_000_000_000L, 3);

  public final void run(PrintStream out)
  {
    HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient();

    try
    {
      logger.info("Setting up");
      setup(hazelcastInstance);
      logger.info("Set up");

      long startTime = System.currentTimeMillis();

      logger.info("Warming up");
      while (System.currentTimeMillis() - startTime < WARMUP_TIME)
      {
        test();
      }
      logger.info("Warmed up");

      this.histogram.reset();

      logger.info("Running test");
      while (System.currentTimeMillis() - startTime < RUN_TIME)
      {
        test();
      }
      logger.info("Run= test");

      logger.info("Recorded latencies [in usec]:");

      this.histogram.outputPercentileDistribution(out, 1000.0);
    }
    catch (Exception e)
    {
      logger.error("Failed during test", e);
    }
    finally
    {
      hazelcastInstance.shutdown();
    }
  }

  protected abstract void setup(HazelcastInstance hazelcastInstance);

  protected abstract void test() throws Exception;

  protected final void recordValue(long value)
  {
    this.histogram.recordValue(value);
  }
}
