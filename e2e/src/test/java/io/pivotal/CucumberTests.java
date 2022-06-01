package io.pivotal;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:build/reports/tests/cucumber/cucumber-report.html"},
        features = {"src/test/resources/features"}
)
public class CucumberTests {
}
