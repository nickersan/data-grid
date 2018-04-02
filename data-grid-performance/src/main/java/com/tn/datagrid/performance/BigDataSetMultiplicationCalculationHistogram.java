package com.tn.datagrid.performance;

import static com.tn.datagrid.core.domain.Operators.latest;
import static com.tn.datagrid.core.domain.Operators.multiply;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tn.datagrid.cao.CalculatedValueCao;
import com.tn.datagrid.cao.CaoException;
import com.tn.datagrid.cao.ValueGetter;
import com.tn.datagrid.core.domain.CalculatedIdentity;
import com.tn.datagrid.core.domain.CompositeIdentity;
import com.tn.datagrid.core.domain.Identity;
import com.tn.datagrid.core.domain.NumericIdentity;
import com.tn.datagrid.core.domain.Versioned;

public class BigDataSetMultiplicationCalculationHistogram extends CalculationHistogram
{
  private static final int BATCH_SIZE = 1000;
  private static final int INITIAL_VERSION = 1;
  private static final int PRODUCT_COUNT = 1000;
  private static final int PRODUCT_MAX_PRICE = 99;
  private static final int PRODUCT_MIN_PRICE = 49;
  private static final String PRODUCT_PRICES = "primary.productPrices";
  private static final String SALE_VALUES = "calculated.saleValueIdentities";
  private static final String SALES = "primary.sales";
  private static final String SALES_PEOPLE = "primary.salesPeople";
  private static final int SALES_PERSON_COUNT = 10;
  private static final String SALES_PERSON_PREFIX = "SP_";

  private static Logger logger = LoggerFactory.getLogger(BigDataSetMultiplicationCalculationHistogram.class);

  private Set<Identity> saleValueIdentities;
  private IMap<Identity, Versioned<String>> salesPeople;
  private ValueGetter<Integer> salesValueGetter;

  public static void main(String[] args)
  {
    new BigDataSetMultiplicationCalculationHistogram().run(System.out);
  }

  @Override
  protected void setup(HazelcastInstance hazelcastInstance)
  {
    this.saleValueIdentities = new HashSet<>();
    this.salesValueGetter = new CalculatedValueCao<>(hazelcastInstance);

    Random random = new Random();

    IMap<Identity, Versioned<Integer>> productPrices = hazelcastInstance.getMap(PRODUCT_PRICES);
    populate(
      productPrices,
      PRODUCT_COUNT,
      () -> random.nextInt((PRODUCT_MAX_PRICE - PRODUCT_MIN_PRICE) + 1) + PRODUCT_MIN_PRICE
    );

    logger.info("Product prices setup: {}", productPrices.size());

    this.salesPeople = hazelcastInstance.getMap(SALES_PEOPLE);
    populate(
      this.salesPeople,
      SALES_PERSON_COUNT,
      () -> SALES_PERSON_PREFIX + (this.salesPeople.size() + 1)
    );

    logger.info("Sales people setup: {}", this.salesPeople.size());

    Map<Identity, Versioned<Integer>> salesBatch = new HashMap<>();
    IMap<Identity, Versioned<Integer>> sales = hazelcastInstance.getMap(SALES);

    for (Identity productPriceIdentity : productPrices.keySet())
    {
      for (Identity salesPersonIdentity : this.salesPeople.keySet())
      {
        Identity saleIdentity = new CompositeIdentity(SALES, productPrices.size() + 1, productPriceIdentity, salesPersonIdentity);

        salesBatch.put(
          saleIdentity,
          new Versioned<>(productPrices.size(), random.nextInt((PRODUCT_MAX_PRICE - PRODUCT_MIN_PRICE) + 1) + PRODUCT_MIN_PRICE)
        );

        if (salesBatch.size() >= BATCH_SIZE)
        {
          sales.putAll(salesBatch);
          salesBatch = new HashMap<>();
        }

        this.saleValueIdentities.add(new CalculatedIdentity<>(SALE_VALUES, latest(multiply()), saleIdentity, productPriceIdentity));
      }
    }

    sales.putAll(salesBatch);

    logger.info("Sales setup: {}", sales.size());
  }

  @Override
  protected void test() throws CaoException
  {
    //this.salesValues.clear();

    long start = System.nanoTime();
    Map<Identity, Integer> results = this.salesValueGetter.getAll(this.saleValueIdentities);
    recordValue(System.nanoTime() - start);

    if (results.size() != this.saleValueIdentities.size())
    {
      throw new IllegalStateException("Query failed: " + results);
    }
  }

  private <T> void populate(IMap<Identity, Versioned<T>> map, int size, Supplier<T> valueSupplier)
  {
    Map<Identity, Versioned<T>> batch = new HashMap<>();

    while (map.size() + batch.size() < size)
    {
      batch.put(
        new NumericIdentity(map.getName(), map.size() + batch.size() + 1),
        new Versioned<>(INITIAL_VERSION, valueSupplier.get())
      );

      if (batch.size() >= BATCH_SIZE)
      {
        map.putAll(batch);
        batch = new HashMap<>();
      }
    }

    map.putAll(batch);
  }
}
