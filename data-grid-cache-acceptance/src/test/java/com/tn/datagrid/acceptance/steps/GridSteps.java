package com.tn.datagrid.acceptance.steps;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import static com.google.common.base.MoreObjects.toStringHelper;

import static com.tn.datagrid.core.predicate.Predicates.childOf;
import static com.tn.datagrid.core.util.LambdaUtils.WrappedException;
import static com.tn.datagrid.core.util.LambdaUtils.wrap;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.hazelcast.query.Predicate;
import cucumber.api.DataTable;
import cucumber.api.java8.En;

import com.tn.datagrid.cao.ReadWriteCao;
import com.tn.datagrid.core.domain.Location;
import com.tn.datagrid.core.domain.Locations;
import com.tn.datagrid.core.domain.Operators;
import com.tn.datagrid.core.domain.identity.AggregateIdentity;
import com.tn.datagrid.core.domain.identity.ChildIdentity;
import com.tn.datagrid.core.domain.identity.Identity;
import com.tn.datagrid.core.predicate.Predicates;
import com.tn.datagrid.core.util.CollectorUtils;

public class GridSteps extends AbstractSteps implements En
{
  private ReadWriteCao<Object> readWriteCao;

  public GridSteps(ReadWriteCao<Object> readWriteCao)
  {
    this.readWriteCao = readWriteCao;

    Then("the a grid created with (.*) on the x-axis and (.*) on the y-axis contains:", this::assertGrid);

  }

  private void assertGrid(String xAxis, String yAxis, DataTable expectedGrid)
  {
    Collection<Identity> xAxisRootIdentities = splitList(xAxis).stream().map(GridSteps::getIdentity).collect(toSet());
    Collection<Identity> xAxisChildIdentities = expandIdentities(xAxisRootIdentities);
    Collection<IdentityNode> xAxisHierarchy = toHierarchy(xAxisRootIdentities, xAxisChildIdentities);
    xAxisHierarchy.forEach((xAxisNode) -> System.out.println(xAxisNode.getLeafNodes()));


    Collection<Identity> aggregateIdentities = xAxisHierarchy.stream().flatMap(this::sumOver).collect(toList());
    System.out.println(aggregateIdentities);

    Collection<Identity> yAxisRootIdentities = splitList(yAxis).stream().map(GridSteps::getIdentity).collect(toSet());
    Collection<Identity> yAxisChildIdentities = expandIdentities(yAxisRootIdentities);
    Collection<IdentityNode> yAxisHierarchy = toHierarchy(yAxisRootIdentities, yAxisChildIdentities);




   // Collection<Identity> identities = xAxisHierarchy.stream().flatMap(sumOver(yAxisHierarchy)).collect(toSet());

    System.out.println(xAxisHierarchy);
    System.out.println(yAxisHierarchy);
//    System.out.println(identities);
  }

  private Stream<Identity> sumOver(IdentityNode identityNode)
  {
    if (identityNode.getChildren().isEmpty())
    {
      return Stream.of(identityNode.getIdentity());
    }
    else
    {
      Collection<Identity> childIdentities = identityNode.getChildren().stream().flatMap(this::sumOver).collect(toList());

      return Stream.concat(
        Stream.of(new AggregateIdentity<>(Locations.calculatedValues(), Operators.sum(), identityNode.getIdentity(), childIdentities)),
        childIdentities.stream()
      );
    }
  }

//  private Function<IdentityNode, Stream<Identity>> sumOver(Collection<IdentityNode> yAxisHierarchy)
//  {
//    return (xAxisNode) -> xAxisNode.flatMap((xAxisChildNode) -> yAxisHierarchy.stream().flatMap(sum(xAxisNode, xAxisChildNode)));
//  }
//
//  private Function<IdentityNode, Stream<Identity>> sum(IdentityNode xAxisNode, IdentityNode xAxisChildNode)
//  {
//    return (yAxisNode) -> Stream.concat(
//      Stream.of(new AggregateIdentity<>(Locations.calculatedValues(), Operators.sum(), xyAxisNode.getIdentity(), xA);)
//    );
//      yAxisNode.flatMap();
//
//    //
//  }

  private Collection<Identity> expandIdentities(Collection<Identity> identities)
  {
    try
    {
      Map<Location, Predicate<Identity, Object>> predicates = identities.stream()
        .collect(groupingBy(Identity::getLocation, mapping((identity) -> childOf(identity, true), CollectorUtils.collectAndMap(Predicates::or))));

      return predicates.entrySet().stream()
        .flatMap(wrap((entry) -> this.readWriteCao.getAll(entry.getKey(), entry.getValue()).entrySet().stream()))
        .map(Map.Entry::getKey)
        .collect(toSet());
    }
    catch (WrappedException e)
    {
      fail("Failed to load grid", e.getCause());
      return emptySet();
    }
  }

  private Collection<IdentityNode> toHierarchy(Collection<Identity> rootIdentities, Collection<Identity> childIdentities)
  {
    return toHierarchy(
      null,
      rootIdentities,
      childIdentities.stream()
        .filter(ChildIdentity.class::isInstance)
        .map(ChildIdentity.class::cast)
        .collect(groupingBy(ChildIdentity::getParentIdentity, toCollection(HashSet::new)))
    );
  }

  private Collection<IdentityNode> toHierarchy(IdentityNode parentNode, Collection<Identity> rootIdentities, Map<Identity, Collection<Identity>> childIdentities)
  {
    Collection<IdentityNode> hierarchy = new HashSet<>();

    for (Identity rootIdentity : rootIdentities)
    {
      IdentityNode identityNode = new IdentityNode(rootIdentity, parentNode);
      hierarchy.add(identityNode.withChildren(toHierarchy(identityNode, childIdentities.getOrDefault(rootIdentity, Collections.emptySet()), childIdentities)));
    }

    return hierarchy;
  }

  private class IdentityNode
  {
    private Identity identity;
    private IdentityNode parentNode;
    private Collection<IdentityNode> children;

    public IdentityNode(Identity identity, IdentityNode parentNode)
    {
      this.identity = identity;
      this.parentNode = parentNode;
      this.children = Collections.emptySet();
    }

    public Collection<IdentityNode> getChildren()
    {
      return this.children;
    }

    public Identity getIdentity()
    {
      return this.identity;
    }

    public Collection<IdentityNode> getLeafNodes() {

      if (this.children.isEmpty())
      {
        return Collections.singleton(this);
      }
      else
      {
        return this.children.stream().flatMap((child) -> child.getLeafNodes().stream()).collect(toSet());
      }
    }

    public Optional<IdentityNode> getParentNode()
    {
      return Optional.ofNullable(this.parentNode);
    }

    public <R> Stream<R> flatMap(Function<IdentityNode, Stream<R>> mapper)
    {
      return this.children.stream().flatMap(mapper);
    }

    public IdentityNode withChildren(Collection<IdentityNode> children) {

      this.children = children;
      return this;
    }

    @Override
    public boolean equals(Object other)
    {
      return this == other || (
        other != null &&
          this.getClass().equals(other.getClass()) &&
          this.identity.equals(((IdentityNode)other).identity) &&
          this.children.equals(((IdentityNode)other).children)
      );
    }

    @Override
    public int hashCode()
    {
      return this.identity.hashCode();
    }

    @Override
    public String toString()
    {
      return toStringHelper(this)
        .add("identity", this.identity)
        .add("children", this.children)
        .toString();
    }
  }
}
