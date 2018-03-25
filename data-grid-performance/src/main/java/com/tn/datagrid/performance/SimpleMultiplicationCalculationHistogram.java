package com.tn.datagrid.performance;

import static com.tn.datagrid.core.domain.Operators.closest;
import static com.tn.datagrid.core.domain.Operators.latest;
import static com.tn.datagrid.core.domain.Operators.multiply;
import static com.tn.datagrid.core.util.NumberUtils.multiply;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import com.tn.datagrid.core.domain.CalculatedIdentity;
import com.tn.datagrid.core.domain.Identity;
import com.tn.datagrid.core.domain.NumericIdentity;
import com.tn.datagrid.core.domain.Versioned;

/**
 * Tests a time taken to do a simple A * B calculation.
 */
public class SimpleMultiplicationCalculationHistogram extends CalculationHistogram
{
  private static final Identity IDENTITY_A = new NumericIdentity(1, MAP_PRIMARY_INTEGERS, 1);
  private static final Identity IDENTITY_B = new NumericIdentity(100, MAP_PRIMARY_INTEGERS, 2);
  private static final Identity IDENTITY_CALCULATED_LATEST = new CalculatedIdentity<>(MAP_CALCULATED_INTEGERS, latest(multiply()), IDENTITY_A, IDENTITY_B);
  private static final Identity IDENTITY_CALCULATED_CLOSEST = new CalculatedIdentity<>(MAP_CALCULATED_INTEGERS, closest(multiply(), 1), IDENTITY_A, IDENTITY_B);
  private static final Versioned<Integer> VALUE_A = new Versioned<>(0, 5).update(2, 6);
  private static final Versioned<Integer> VALUE_B = new Versioned<>(1, 7).update(3, 10);

  private IMap<Identity, Integer> calculatedIntegers;
  //private ValueGetter<Number> calculatedValueGetter;

  public static void main(String[] args)
  {
    new SimpleMultiplicationCalculationHistogram().run(System.out);
  }

  @Override
  protected void setup(HazelcastInstance hazelcastInstance)
  {
    this.calculatedIntegers = hazelcastInstance.getMap(MAP_CALCULATED_INTEGERS);
    //this.calculatedValueGetter = new CalculatedValueCao<>(hazelcastInstance);

    IMap<Identity, Versioned<Integer>> primaryIntegers = hazelcastInstance.getMap(MAP_PRIMARY_INTEGERS);
    primaryIntegers.put(IDENTITY_A, VALUE_A);
    primaryIntegers.put(IDENTITY_B, VALUE_B);
  }

  @Override
  protected void test()
  {
    this.calculatedIntegers.clear();

    long start = System.nanoTime();
    Number resultLatest = this.calculatedIntegers.get(IDENTITY_CALCULATED_LATEST);
//    Number resultLatest = this.calculatedValueGetter.get(IDENTITY_CALCULATED_CLOSEST).get();
    recordValue(System.nanoTime() - start);

    //if (!resultLatest.equals(multiply(VALUE_A.getClosest(1).get().get(), VALUE_B.getClosest(1).get().get())))
    if (!resultLatest.equals(multiply(VALUE_A.get(), VALUE_B.get())))
    {
      throw new IllegalStateException("Calculation failed: " + resultLatest);
    }
  }
}
