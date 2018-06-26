package com.tn.datagrid.acceptance.tests;


import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
  plugin = { "html:target/cucumber/TreeCreateUpdateAndGet" },
  features = { "classpath:features/tree_create_update_and_get.feature" },
  glue = { "com.tn.datagrid.acceptance.steps" }
)
public class TreeCreateUpdateAndGetTest
{
}
