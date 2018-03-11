package com.tn.datagrid.core.domain;

import static java.util.Arrays.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TreeValueTest
{
  @Test
  public void testConstructionFromCollection()
  {
    Type<String, StringValue> root = StringValue.newType("root").build();
    Type<String, StringValue> level1 = StringValue.newType("L1").build();
    Type<String, StringValue> level2 = StringValue.newType("L2").build();
    Type<String, StringValue> level3 = StringValue.newType("L3").build();

    StringValue s1 = new StringValue(new NumericIdentity<>(root, 1), "Root");

    StringValue s2 = new StringValue(new ChildIdentity<>(level1, 2, s1), "L1.1");
    StringValue s3 = new StringValue(new ChildIdentity<>(level1, 3, s1), "L1.2");
    StringValue s4 = new StringValue(new ChildIdentity<>(level1, 4, s1), "L1.3");

    StringValue s5 = new StringValue(new ChildIdentity<>(level2, 5, s2), "L2.1");
    StringValue s6 = new StringValue(new ChildIdentity<>(level2, 6, s2), "L2.2");
    StringValue s7 = new StringValue(new ChildIdentity<>(level2, 7, s2), "L2.3");
    StringValue s8 = new StringValue(new ChildIdentity<>(level2, 8, s3), "L2.4");
    StringValue s9 = new StringValue(new ChildIdentity<>(level2, 9, s3), "L2.4");
    StringValue s10 = new StringValue(new ChildIdentity<>(level2, 10, s4), "L2.4");
    StringValue s11 = new StringValue(new ChildIdentity<>(level2, 11, s4), "L2.4");

    StringValue s12 = new StringValue(new ChildIdentity<>(level3, 12, s11), "L3.1");
    StringValue s13 = new StringValue(new ChildIdentity<>(level3, 12, s11), "L3.2");
    StringValue s14 = new StringValue(new ChildIdentity<>(level3, 12, s11), "L3.3" );

    TreeValue<
      String,
      StringValue,
      String,
      TreeValue<
        String,
        StringValue,
        String,
        TreeValue<
          String,
          StringValue,
          String,
          StringValue
        >
      >
    > treeValue = TreeValue.builder(level1)
      .withChildren(TreeValue.builder(level2).withChildren(TreeValue.builder(level3)))
      .build(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14);

    //noinspection unchecked
    assertEquals(
      new TreeValue(
        s1,
        asList(
          new TreeValue(s2, asList(new TreeValue(s5), new TreeValue(s6), new TreeValue(s7))),
          new TreeValue(s3, asList(new TreeValue(s8), new TreeValue(s9))),
          new TreeValue(s4, asList(new TreeValue(s10), new TreeValue(s11, asList(s12, s13, s14)))))
      ),
      treeValue
    );
  }
}
