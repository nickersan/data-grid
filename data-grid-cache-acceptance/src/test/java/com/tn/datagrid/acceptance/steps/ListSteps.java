package com.tn.datagrid.acceptance.steps;

import static java.util.stream.Collectors.toSet;

import static org.junit.Assert.assertEquals;

import static com.tn.datagrid.core.util.StringUtils.COMMA;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;

import com.tn.datagrid.cao.CaoException;
import com.tn.datagrid.cao.ReadWriteCao;
import com.tn.datagrid.core.domain.ListDefinition;
import com.tn.datagrid.core.domain.Locations;
import com.tn.datagrid.core.domain.identity.Identity;
import com.tn.datagrid.core.predicate.ChildOf;

@ContextConfiguration("classpath:cucumber.xml")
public class ListSteps extends AbstractSteps implements En
{
  private static Logger logger = LoggerFactory.getLogger(ListSteps.class);

  private ReadWriteCao<Object> readWriteCao;

  private Map<String, Identity> identities;

  public ListSteps(ReadWriteCao<Object> readWriteCao)
  {
    this.readWriteCao = readWriteCao;
    this.identities = new HashMap<>();

    When("a new list called (\\S+) is created", this::listCreate);
    When("(\\S+) is added to the list (\\S+)", this::listAdd);

    Then("the list (\\S+) contains (.*)", this::assertListContains);
    Then("the list (\\S+) at version (\\d+) contains (.*)", this::assertListContainsAt);
    After(this::deleteLists);
  }

  private void assertListContains(String listName, String itemsStr)
  {
    assertListContainsAt(listName, Integer.MAX_VALUE, itemsStr);
  }

  private void assertListContainsAt(String listName, int version, String itemsStr)
  {
    try
    {
      Identity listIdentity = this.identities.get(listName);

      if (listIdentity != null)
      {
        Map<Identity, Object> children = this.readWriteCao.getAllAt(listIdentity.getLocation(), new ChildOf<>(listIdentity), version);
        Collection<String> items = Stream.of(itemsStr.split(COMMA)).map(String::trim).collect(toSet());

        assertEquals(items.size(), children.size());
        assertEquals(items, Set.copyOf(children.values()));
      }
      else
      {
        fail("List does not exist: " + listName);
      }
    }
    catch (CaoException e)
    {
      fail("Failed to get list content: " + listName, e);
    }
  }

  private void listAdd(String value, String listName)
  {
    try
    {
      Identity listIdentity = this.identities.get(listName);

      if (listIdentity != null)
      {
        this.identities.put(value, readWriteCao.create(listIdentity, value));
      }
      else
      {
        fail("List does not exist: " + listName);
      }
    }
    catch (CaoException e)
    {
      fail("Failed to add: " + value + " to list: " + listName, e);
    }
  }

  private void listCreate(String listName)
  {
    try
    {
      this.identities.put(listName, readWriteCao.create(Locations.listsLocation(), new ListDefinition(listName)));
    }
    catch (CaoException e)
    {
      fail("Failed to create list: " + listName, e);
    }
  }

  private void deleteLists()
  {
    this.identities.values().forEach((identity) -> readWriteCao.delete(identity, true));
  }
}
