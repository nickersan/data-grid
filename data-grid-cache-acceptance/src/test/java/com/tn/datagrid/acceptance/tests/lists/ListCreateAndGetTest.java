package com.tn.datagrid.acceptance.tests.lists;


import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
  plugin = { "html:target/cucumber" },
  features = { "classpath:features/lists/list_create_and_get.feature" },
  glue = { "com.tn.datagrid.acceptance.steps" }
)
public class ListCreateAndGetTest
{
}
