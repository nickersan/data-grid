package com.tn.datagrid.core.domain.identity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.tn.datagrid.core.domain.Location;

public class ChildIdentityTest
{
  private static final Location LOCATION_TEST = new Location("test");

  @Test
  public void testIsChild()
  {
    Identity root = new NumericIdentity(LOCATION_TEST, 1);
    ChildIdentity parent = new ChildIdentity(root, 2);
    ChildIdentity child = new ChildIdentity(parent, 3);

    assertTrue(child.isChild(parent));
    assertTrue(child.isChild(parent, false));
    assertTrue(child.isChild(parent, true));

    assertFalse(child.isChild(root));
    assertFalse(child.isChild(root, false));
    assertTrue(child.isChild(root, true));

    Identity identity = new NumericIdentity(LOCATION_TEST, 4);
    assertFalse(child.isChild(identity));
    assertFalse(child.isChild(identity, false));
    assertFalse(child.isChild(identity, true));
  }
}
