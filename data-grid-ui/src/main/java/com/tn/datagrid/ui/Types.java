package com.tn.datagrid.ui;

import com.tn.datagrid.core.domain.StringValue;
import com.tn.datagrid.core.domain.Type;

public class Types
{
  public static final Type<String, StringValue> TYPE_MONTH = new Type<>(StringValue.class, "MONTH");
  public static final Type<String, StringValue> TYPE_PERSON = new Type<>(StringValue.class, "PERSON");
  public static final Type<String, StringValue> TYPE_REGION = new Type<>(StringValue.class, "REGION");
  public static final Type<String, StringValue> TYPE_REGIONS = new Type<>(StringValue.class, "REGIONS");
  public static final Type<String, StringValue> TYPE_YEAR = new Type<>(StringValue.class, "YEAR");
  public static final Type<String, StringValue> TYPE_YEARS = new Type<>(StringValue.class, "YEARS");
}
