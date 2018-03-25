package com.tn.datagrid.performance;

import java.util.Random;
import java.util.function.Supplier;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import com.tn.datagrid.core.domain.Identity;
import com.tn.datagrid.core.domain.NumericIdentity;
import com.tn.datagrid.core.domain.Versioned;

public class BigDataSetMultiplicationCalculationHistogram extends CalculationHistogram
{
  private static final int INITIAL_VERSION = 1;
  private static final int PRODUCT_COUNT = 1000;
  private static final int PRODUCT_MAX_PRICE = 99;
  private static final int PRODUCT_MIN_PRICE = 49;
  private static final String PRODUCT_PRICES = "primary.productPrices";
  private static final String SALES = "primary.sales";
  private static final String SALES_PEOPLE = "primary.salesPeople";
  private static final int SALES_PERSON_COUNT = 1000;
  private static final String SALES_PERSON_PREFIX = "SP_";

  //private List<>

  @Override
  protected void setup(HazelcastInstance hazelcastInstance)
  {
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

    for (Identity productIdentity : productPrices.keySet())
    {
      sales.put(
        new CompositeIdentity(SALES, productPrices.size() + 1),
        new Versioned<>(productPrices.size(), random.nextInt((PRODUCT_MAX_PRICE - PRODUCT_MIN_PRICE) + 1) + PRODUCT_MIN_PRICE)
      );
    }
  }

  @Override
  protected void test()
  {

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
