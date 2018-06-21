package com.tn.datagrid.acceptance.steps;

import static java.util.stream.Collectors.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import static com.tn.datagrid.core.util.StringUtils.COMMA;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import cucumber.api.java8.En;
import org.springframework.test.context.ContextConfiguration;

import com.tn.datagrid.cao.CaoException;
import com.tn.datagrid.cao.ReadWriteCao;
import com.tn.datagrid.core.domain.ListDefinition;
import com.tn.datagrid.core.domain.Locations;
import com.tn.datagrid.core.domain.identity.Identity;
import com.tn.datagrid.core.predicate.ChildOf;

@ContextConfiguration("classpath:cucumber.xml")
public class ListSteps implements En
{
  private ReadWriteCao<Object> readWriteCao;

  private Map<String, Identity> identities;

  public ListSteps(ReadWriteCao<Object> readWriteCao)
  {
    this.readWriteCao = readWriteCao;
    this.identities = new HashMap<>();

    When("a new list called (\\S+) is created", this::listCreate);
    When("(\\S+) is added to the list (\\S+)", this::listAdd);

    Then("the list (\\S+) contains (.*)", this::assertListContains);

    After(this::deleteLists);
  }

  private void assertListContains(String listName, String itemsStr)
  {
    Identity listIdentity = this.identities.get(listName);
    if (listIdentity == null)
    {
      fail("List does not exist: " + listName);
    }

    Map<Identity, Object> children = this.readWriteCao.getAll(listIdentity.getLocation(), new ChildOf<>(listIdentity));
    Collection<String> items = Stream.of(itemsStr.split(COMMA)).map(String::trim).collect(toSet());

    assertEquals(items.size(), children.size());
    assertEquals(items, Set.copyOf(children.values()));
  }

  private void listAdd(String value, String listName)
  {
    try
    {
      Identity listIdentity = this.identities.get(listName);
      if (listIdentity == null)
      {
        fail("List does not exist: " + listName);
      }

      this.identities.put(value, readWriteCao.create(listIdentity, value));
    }
    catch (CaoException e)
    {
      fail("Failed to add: " + value + " to list: " + listName);
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
      fail("Failed to create list: " + listName);
    }
  }

  private void deleteLists()
  {
    this.identities.values().forEach((identity) -> readWriteCao.delete(identity, true));
  }
}
