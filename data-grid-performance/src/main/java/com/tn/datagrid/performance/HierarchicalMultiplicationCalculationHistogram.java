package com.tn.datagrid.performance;

import static com.tn.datagrid.core.util.NumberUtils.multiple;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import com.tn.datagrid.core.domain.CalculatedIdentity;
import com.tn.datagrid.core.domain.Identity;
import com.tn.datagrid.core.domain.NumberValue;
import com.tn.datagrid.core.domain.NumericIdentity;
import com.tn.datagrid.core.domain.Operators;
import com.tn.datagrid.core.domain.Type;

/**
 * Tests a time taken to do a hierarchical A * (B * C) calculation.
 */
public class HierarchicalMultiplicationCalculationHistogram extends CalculationHistogram
{
  private static final Type<Number, NumberValue> NUMBER_TYPE = new Type<>(NumberValue.class, "number");
  private static final NumberValue A = new NumberValue(new NumericIdentity<>(NUMBER_TYPE, 1, 1), 5);
  private static final NumberValue B = new NumberValue(new NumericIdentity<>(NUMBER_TYPE, 2, 1), 7);
  private static final NumberValue C = new NumberValue(new NumericIdentity<>(NUMBER_TYPE, 3, 1), 3);
  private static final Identity<Number, NumberValue> CALCULATED_IDENTITY_2 = new CalculatedIdentity<>(NUMBER_TYPE, Operators.multiply(), B.getIdentity(), C.getIdentity());
  private static final Identity<Number, NumberValue> CALCULATED_IDENTITY_1 = new CalculatedIdentity<>(NUMBER_TYPE, Operators.multiply(), A.getIdentity(), CALCULATED_IDENTITY_2);

  private IMap<Identity<Number, NumberValue>, NumberValue> calculatedIntegers;

  public static void main(String[] args)
  {
    new HierarchicalMultiplicationCalculationHistogram().run(System.out);
  }

  @Override
  protected void setup(HazelcastInstance hazelcastInstance)
  {
    this.calculatedIntegers = hazelcastInstance.getMap(MAP_CALCULATED_INTEGERS);

    IMap<Identity<Number, NumberValue>, NumberValue> primaryIntegers = hazelcastInstance.getMap(MAP_PRIMARY_INTEGERS);
    primaryIntegers.put(A.getIdentity(), A);
    primaryIntegers.put(B.getIdentity(), B);
    primaryIntegers.put(C.getIdentity(), C);
  }

  @Override
  protected void test()
  {
    this.calculatedIntegers.clear();

    long start = System.nanoTime();
    NumberValue result = calculatedIntegers.get(CALCULATED_IDENTITY_1);
    recordValue(System.nanoTime() - start);

    if (!result.get().equals(multiple(A.get(), (multiple(B.get(), C.get())))))
    {
      throw new IllegalStateException("Calculation failed: " + result);
    }
  }
}
