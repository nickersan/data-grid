package com.tn.datagrid.core.domain;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.io.Serializable;
import java.util.Objects;

public class Pair<L, R> implements Serializable
{
  private L left;
  private R right;

  public Pair(L left, R right)
  {
    this.left = left;
    this.right = right;
  }

  public L getLeft()
  {
    return left;
  }

  public R getRight()
  {
    return right;
  }

  @Override
  public boolean equals(Object other)
  {
    return this == other || (
      other != null &&
      this.getClass().equals(other.getClass()) &&
      this.left.equals(((Pair)other).left) &&
      this.right == ((Pair)other).right
    );
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(this.left, this.right);
  }

  @Override
  public String toString()
  {
    return toStringHelper(this)
      .add("left", this.left)
      .add("right", this.right)
      .toString();
  }
}
