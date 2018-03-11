package com.tn.datagrid.performance;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import com.tn.datagrid.core.domain.CalculatedIdentity;
import com.tn.datagrid.core.domain.Identity;
import com.tn.datagrid.core.domain.IntValue;
import com.tn.datagrid.core.domain.NumericIdentity;
import com.tn.datagrid.core.domain.Operators;
import com.tn.datagrid.core.domain.Type;

/**
 * Tests a time taken to do a simple A * B calculation.
 */
public class SimpleMultiplicationCalculationHistogram extends CalculationHistogram
{
  private static final Type<Integer, IntValue> INT_TYPE = new Type<>(IntValue.class, "int");
  private static final IntValue A = new IntValue(new NumericIdentity<>(INT_TYPE, 1), 5);
  private static final IntValue B = new IntValue(new NumericIdentity<>(INT_TYPE, 2), 7);
  private static final Identity<Integer, IntValue> CALCULATED_IDENTITY = new CalculatedIdentity<>(INT_TYPE, Operators.multiply(), A.getIdentity(), B.getIdentity());

  private IMap<Identity<Integer, IntValue>, IntValue> calculatedIntegers;

  public static void main(String[] args)
  {
    new SimpleMultiplicationCalculationHistogram().run(System.out);
  }

  @Override
  protected void setup(HazelcastInstance hazelcastInstance)
  {
    this.calculatedIntegers = hazelcastInstance.getMap(MAP_CALCULATED_INTEGERS);

    IMap<Identity<Integer, IntValue>, IntValue> primaryIntegers = hazelcastInstance.getMap(MAP_PRIMARY_INTEGERS);
    primaryIntegers.put(A.getIdentity(), A);
    primaryIntegers.put(B.getIdentity(), B);
  }

  @Override
  protected void test()
  {
    this.calculatedIntegers.clear();

    long start = System.nanoTime();
    IntValue result = this.calculatedIntegers.get(CALCULATED_IDENTITY);
    recordValue(System.nanoTime() - start);

    if (result.get() != A.get() * B.get())
    {
      throw new IllegalStateException("Calculation failed: " + result);
    }
  }
}
