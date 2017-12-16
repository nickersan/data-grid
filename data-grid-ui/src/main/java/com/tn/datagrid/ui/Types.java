package com.tn.datagrid.ui;

import com.tn.datagrid.core.domain.StringValue;
import com.tn.datagrid.core.domain.Type;

public class Types
{
  public static final Type<String, StringValue> TYPE_PERSON = new Type<>(StringValue.class, "PERSON");
  public static final Type<String, StringValue> TYPE_REGION = new Type<>(StringValue.class, "REGION");
  public static final Type<String, StringValue> TYPE_REGIONS = new Type<>(StringValue.class, "REGIONS");
}
