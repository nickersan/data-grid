package com.tn.datagrid.acceptance.steps;

import static java.util.stream.Collectors.toSet;

import static com.tn.datagrid.core.util.StringUtils.COMMA;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cucumber.api.DataTable;
import cucumber.api.java8.En;

import com.tn.datagrid.cao.CaoException;
import com.tn.datagrid.cao.ReadWriteCao;
import com.tn.datagrid.core.domain.LineItemDefinition;
import com.tn.datagrid.core.domain.Locations;
import com.tn.datagrid.core.domain.identity.Identity;

public class LineItemSteps extends AbstractSteps implements En
{
  private ReadWriteCao<Object> readWriteCao;

  public LineItemSteps(ReadWriteCao<Object> readWriteCao)
  {
    this.readWriteCao = readWriteCao;

    When("a line-item called (\\S+) is created against (.*)", this::lineItemCreate);
    When("the following 1-dimensional integer values are added to the line-item (\\S+):", this::lineItemAddInteger);

    After(this::deleteLineItems);
  }

  private void lineItemAddInteger(String lineItemName, DataTable dataTable)
  {
    try
    {
      Identity lineItemIdentity = getIdentity(lineItemName);
      List<Identity> columnIdentities = dataTable.topCells().stream().map(LineItemSteps::getIdentity).collect(Collectors.toList());
      List<Integer> values = dataTable.cells(1).get(0).stream().map(Integer::parseInt).collect(Collectors.toList());

      for (int i = 0; i < columnIdentities.size(); i++)
      {
        Integer value = values.get(i);
        setIdentity(value, this.readWriteCao.create(lineItemIdentity.getLocation(), Set.of(lineItemIdentity, columnIdentities.get(i)), values));
      }
    }
    catch (CaoException e)
    {
      fail("Failed to create line item values: " + lineItemName, e);
    }
  }

  private void lineItemCreate(String lineItemName, String dimensionNames)
  {
    try
    {
      Collection<Identity> dimensionIdentities = Stream.of(dimensionNames.split(COMMA)).filter(String::isEmpty).map(ListSteps::getIdentity).collect(toSet());
      setIdentity(lineItemName, readWriteCao.create(Locations.lineItemsLocation(), new LineItemDefinition(lineItemName, dimensionIdentities)));
    }
    catch (CaoException e)
    {
      fail("Failed to create line item: " + lineItemName, e);
    }
  }

  private void deleteLineItems()
  {
    getIdentities().forEach((identity) -> readWriteCao.delete(identity, true));
    clearIdentities();
  }
}
