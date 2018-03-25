package com.tn.datagrid.performance;

import java.util.Random;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import com.tn.datagrid.core.domain.Identity;
import com.tn.datagrid.core.domain.NumericIdentity;
import com.tn.datagrid.core.domain.Versioned;

public class BigDataSetMultiplicationCalculationHistogram extends CalculationHistogram
{
  private static final int PRODUCT_COUNT = 1000;
  private static final int SALE_COUNT = 1000;
  private static final int PRODUCT_MAX_PRICE = 99;
  private static final int PRODUCT_MIN_PRICE = 49;
  private static final String PRODUCT_PRICES = "primary.productPrices";
  private static final String SALES = "primary.sales";

  //private List<>

  @Override
  protected void setup(HazelcastInstance hazelcastInstance)
  {
    Random random = new Random();

    IMap<Identity, Versioned<Integer>> productPrices = hazelcastInstance.getMap(PRODUCT_PRICES);

    while (productPrices.size() < PRODUCT_COUNT)
    {
      productPrices.put(
        new NumericIdentity(PRODUCT_PRICES, productPrices.size() + 1),
        new Versioned<>(productPrices.size(), random.nextInt((PRODUCT_MAX_PRICE - PRODUCT_MIN_PRICE) + 1) + PRODUCT_MIN_PRICE)
      );
    }

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
}
