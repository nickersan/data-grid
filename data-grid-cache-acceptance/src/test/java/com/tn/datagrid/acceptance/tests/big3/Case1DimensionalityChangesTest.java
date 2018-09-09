package com.tn.datagrid.acceptance.tests.big3;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

@RunWith(Cucumber.class)
@CucumberOptions(
  plugin = { "html:target/cucumber/Case1DimensionalityChanges" },
  features = { "classpath:features/big-3/case-1-dimensionality-changes.feature" },
  glue = { "com.tn.datagrid.acceptance.steps" }
)
@ContextConfiguration("classpath:cucumber.xml")
public class Case1DimensionalityChangesTest
{
}
