package com.tn.datagrid.acceptance.tests;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

@RunWith(Cucumber.class)
@CucumberOptions(
  plugin = { "html:target/cucumber/ListCreateUpdateAndGet" },
  features = { "classpath:features/list_create_update_and_get.feature" },
  glue = { "com.tn.datagrid.acceptance.steps" }
)
@ContextConfiguration("classpath:cucumber.xml")
public class ListCreateUpdateAndGetTest
{
}
