package com.tn.datagrid.performance;

import static com.tn.datagrid.core.domain.Operators.latest;
import static com.tn.datagrid.core.domain.Operators.latestLeft;
import static com.tn.datagrid.core.domain.Operators.multiply;
import static com.tn.datagrid.core.util.NumberUtils.multiply;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import com.tn.datagrid.cao.CalculatedValueGetter;
import com.tn.datagrid.cao.ValueGetter;
import com.tn.datagrid.core.domain.CalculatedIdentity;
import com.tn.datagrid.core.domain.Identity;
import com.tn.datagrid.core.domain.NumericIdentity;
import com.tn.datagrid.core.domain.Versioned;

/**
 * Tests a time taken to do a hierarchical A * (B * C) calculation.
 */
public class HierarchicalMultiplicationCalculationHistogram extends CalculationHistogram
{
  private static final Identity IDENTITY_A = new NumericIdentity(1, 1);
  private static final Identity IDENTITY_B = new NumericIdentity(2, 1);
  private static final Identity IDENTITY_C = new NumericIdentity(3, 1);
  private static final Identity IDENTITY_CALCULATED_2 = new CalculatedIdentity<>(latest(multiply()), IDENTITY_B, IDENTITY_C);
  private static final Identity IDENTITY_CALCULATED_1 = new CalculatedIdentity<>(latestLeft(multiply()), IDENTITY_A, IDENTITY_CALCULATED_2);
  private static final Versioned<Integer> VALUE_A = new Versioned<>(0, 5).update(3, 50);
  private static final Versioned<Integer> VALUE_B = new Versioned<>(1, 7).update(4, 70);
  private static final Versioned<Integer> VALUE_C = new Versioned<>(2, 3).update(5, 80);

  private IMap<Identity, Number> calculatedIntegers;
  private ValueGetter<Number> calculatedValueGetter;

  public static void main(String[] args)
  {
    new HierarchicalMultiplicationCalculationHistogram().run(System.out);
  }

  @Override
  protected void setup(HazelcastInstance hazelcastInstance)
  {
    this.calculatedIntegers = hazelcastInstance.getMap(MAP_CALCULATED_INTEGERS);
    this.calculatedValueGetter = new CalculatedValueGetter<>(hazelcastInstance);

    IMap<Identity, Versioned<Integer>> primaryIntegers = hazelcastInstance.getMap(MAP_PRIMARY_INTEGERS);
    primaryIntegers.put(IDENTITY_A, VALUE_A);
    primaryIntegers.put(IDENTITY_B, VALUE_B);
    primaryIntegers.put(IDENTITY_C, VALUE_C);
  }

  @Override
  protected void test()
  {
    this.calculatedIntegers.clear();

    long start = System.nanoTime();
    Number result = calculatedValueGetter.get(IDENTITY_CALCULATED_1).get();
    recordValue(System.nanoTime() - start);

    if (!result.equals(multiply(VALUE_A.get(), (multiply(VALUE_B.get(), VALUE_C.get())))))
    {
      throw new IllegalStateException("Calculation failed: " + result);
    }
  }
}
