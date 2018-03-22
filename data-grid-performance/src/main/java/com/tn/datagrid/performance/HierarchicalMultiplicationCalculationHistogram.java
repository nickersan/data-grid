package com.tn.datagrid.performance;

import static com.tn.datagrid.core.util.NumberUtils.multiple;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import com.tn.datagrid.core.domain.CalculatedIdentity;
import com.tn.datagrid.core.domain.Identity;
import com.tn.datagrid.core.domain.NumericIdentity;
import com.tn.datagrid.core.domain.Operators;

/**
 * Tests a time taken to do a hierarchical A * (B * C) calculation.
 */
public class HierarchicalMultiplicationCalculationHistogram extends CalculationHistogram
{
  private static final Identity IDENTITY_A = new NumericIdentity(1);
  private static final Identity IDENTITY_B = new NumericIdentity(2);
  private static final Identity IDENTITY_C = new NumericIdentity(3);
  private static final Identity IDENTITY_CALCULATED_2 = new CalculatedIdentity<>(Operators.multiply(), IDENTITY_B, IDENTITY_C);
  private static final Identity IDENTITY_CALCULATED_1 = new CalculatedIdentity<>(Operators.multiply(), IDENTITY_A, IDENTITY_CALCULATED_2);
  private static final Integer VALUE_A = 5;
  private static final Integer VALUE_B = 7;
  private static final Integer VALUE_C = 3;

  private IMap<Identity, Number> calculatedIntegers;

  public static void main(String[] args)
  {
    new HierarchicalMultiplicationCalculationHistogram().run(System.out);
  }

  @Override
  protected void setup(HazelcastInstance hazelcastInstance)
  {
    this.calculatedIntegers = hazelcastInstance.getMap(MAP_CALCULATED_INTEGERS);

    IMap<Identity, Number> primaryIntegers = hazelcastInstance.getMap(MAP_PRIMARY_INTEGERS);
    primaryIntegers.put(IDENTITY_A, VALUE_A);
    primaryIntegers.put(IDENTITY_B, VALUE_B);
    primaryIntegers.put(IDENTITY_C, VALUE_C);
  }

  @Override
  protected void test()
  {
    this.calculatedIntegers.clear();

    long start = System.nanoTime();
    Number result = calculatedIntegers.get(IDENTITY_CALCULATED_1);
    recordValue(System.nanoTime() - start);

    if (!result.equals(multiple(VALUE_A, (multiple(VALUE_B, VALUE_C)))))
    {
      throw new IllegalStateException("Calculation failed: " + result);
    }
  }
}
