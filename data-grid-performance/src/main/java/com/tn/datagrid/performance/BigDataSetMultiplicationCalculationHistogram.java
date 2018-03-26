package com.tn.datagrid.performance;

import static com.tn.datagrid.core.domain.Operators.latest;
import static com.tn.datagrid.core.domain.Operators.multiply;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import com.tn.datagrid.core.domain.CalculatedIdentity;
import com.tn.datagrid.core.domain.CompositeIdentity;
import com.tn.datagrid.core.domain.Identity;
import com.tn.datagrid.core.domain.NumericIdentity;
import com.tn.datagrid.core.domain.Versioned;

public class BigDataSetMultiplicationCalculationHistogram extends CalculationHistogram
{
  private static final int INITIAL_VERSION = 1;
  private static final int PRODUCT_COUNT = 100;
  private static final int PRODUCT_MAX_PRICE = 99;
  private static final int PRODUCT_MIN_PRICE = 49;
  private static final String PRODUCT_PRICES = "primary.productPrices";
  private static final String SALE_VALUES = "calculated.saleValueIdentities";
  private static final String SALES = "primary.sales";
  private static final String SALES_PEOPLE = "primary.salesPeople";
  private static final int SALES_PERSON_COUNT = 10;
  private static final String SALES_PERSON_PREFIX = "SP_";

  private Set<Identity> saleValueIdentities;
  private IMap<Identity, Integer> salesValues;

  public static void main(String[] args)
  {
    new BigDataSetMultiplicationCalculationHistogram().run(System.out);
  }

  @Override
  protected void setup(HazelcastInstance hazelcastInstance)
  {
    this.saleValueIdentities = new HashSet<>();
    this.salesValues = hazelcastInstance.getMap(SALE_VALUES);

    Random random = new Random();

    IMap<Identity, Versioned<Integer>> productPrices = hazelcastInstance.getMap(PRODUCT_PRICES);
    populate(
      productPrices,
      PRODUCT_COUNT,
      () -> random.nextInt((PRODUCT_MAX_PRICE - PRODUCT_MIN_PRICE) + 1) + PRODUCT_MIN_PRICE
    );

    IMap<Identity, Versioned<String>> salesPeople = hazelcastInstance.getMap(SALES_PEOPLE);
    populate(
      salesPeople,
      SALES_PERSON_COUNT,
      () -> SALES_PERSON_PREFIX + (salesPeople.size() + 1)
    );

    IMap<Identity, Versioned<Integer>> sales = hazelcastInstance.getMap(SALES);

    for (Identity productPriceIdentity : productPrices.keySet())
    {
      for (Identity salesPersonIdentity : salesPeople.keySet())
      {
        Identity saleIdentity = new CompositeIdentity(SALES, productPrices.size() + 1, productPriceIdentity, salesPersonIdentity);

        sales.put(
          saleIdentity,
          new Versioned<>(productPrices.size(), random.nextInt((PRODUCT_MAX_PRICE - PRODUCT_MIN_PRICE) + 1) + PRODUCT_MIN_PRICE)
        );

        saleValueIdentities.add(new CalculatedIdentity<>(SALE_VALUES, latest(multiply()), saleIdentity, productPriceIdentity));
      }
    }
  }

  @Override
  protected void test()
  {
    this.salesValues.clear();

    long start = System.nanoTime();
    Map<Identity, Integer> results = this.salesValues.getAll(this.saleValueIdentities);
//    Number resultLatest = this.calculatedValueGetter.get(IDENTITY_CALCULATED_CLOSEST).get();
    recordValue(System.nanoTime() - start);

    if (results.size() != this.saleValueIdentities.size())
    {
      throw new IllegalStateException("Calculation failed: " + results);
    }
  }

  private <T> void populate(IMap<Identity, Versioned<T>> map, int size, Supplier<T> valueSupplier)
  {
    while (map.size() < size)
    {
      map.put(
        new NumericIdentity(map.getName(), map.size() + 1),
        new Versioned<>(INITIAL_VERSION, valueSupplier.get())
      );
    }
  }
}
