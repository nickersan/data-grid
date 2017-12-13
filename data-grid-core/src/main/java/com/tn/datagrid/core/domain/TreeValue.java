package com.tn.datagrid.core.domain;

import static java.util.Arrays.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class TreeValue<T, V extends Value<T, V>, T1, V1 extends Value<T1, V1>> extends Value<T, TreeValue<T, V, T1, V1>>
{
  private Value<T, V> root;
  private Collection<V1> children;

  @SafeVarargs
  protected TreeValue(Value<T, V> root, V1... children)
  {
    this(root, asList(children));
  }

  protected TreeValue(Value<T, V> root, List<V1> children)
  {
    //noinspection unchecked
    super((Identity<T, TreeValue<T, V, T1, V1>>)root.getIdentity());
    this.root = root;
    this.children = Collections.unmodifiableList(children);
  }

  @Override
  public T get()
  {
    return this.root.get();
  }

  public Collection<V1> getChildren()
  {
    return this.children;
  }

  @Override
  public boolean equals(Object other)
  {
    return this == other || (
      other != null &&
        getClass().equals(other.getClass()) &&
        getIdentity().equals(((TreeValue)other).getIdentity()) &&
        get().equals(((TreeValue)other).get()) &&
        getChildren().equals(((TreeValue)other).getChildren())
    );
  }

  @Override
  public int hashCode()
  {
    return this.root.hashCode();
  }

  @Override
  public String toString()
  {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("identity", getIdentity())
      .append("value", get())
      .append("children", getChildren())
      .toString();
  }

  public static <T1, V1 extends Value<T1, V1>> Builder<T1, V1> builder(Type<T1, V1> childType)
  {
    return new Builder<>(childType);
  }

  public static class Builder<T1, V1 extends Value<T1, V1>>
  {
    private Type<T1, V1> childType;
    private Builder<?, ? extends Value> childBuilder;

    private Builder(Type<T1, V1> childType)
    {
      this.childType = childType;
    }

    public <T, V extends Value<T, V>> TreeValue<T, V, T1, V1> build(V root, Value<?, ?>... children)
    {
      return this.build(root, asList(children));
    }

    public <T, V extends Value<T, V>> TreeValue<T, V, T1, V1> build(V root, Collection<Value<?, ?>> children)
    {
      //noinspection unchecked
      List<V1> childValues = children.stream()
        .filter(isChild(root))
        .map((value) -> this.childBuilder != null ? (V1)childBuilder.build((V1)value, children) : (V1)value)
        .collect(Collectors.toList());

      return new TreeValue<>(root, childValues);
    }

    private Predicate<Value<?, ?>> isChild(Value<?, ?> root)
    {
      return (value) -> childType.equals(value.getType()) &&
        value.getIdentity() instanceof ChildIdentity &&
        ((ChildIdentity)value.getIdentity()).getParentIdentity().equals(root.getIdentity());
    }

    public <T2, V2 extends Value<T2, V2>> Builder<T1, TreeValue<T1, V1, T2, V2>> withChildren(Builder<T2, V2> childBuilder)
    {
      this.childBuilder = childBuilder;
      //noinspection unchecked
      return (Builder<T1, TreeValue<T1, V1, T2, V2>>)this;
    }
  }
}
