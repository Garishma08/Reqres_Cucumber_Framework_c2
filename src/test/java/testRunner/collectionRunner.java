package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources/feature/collectionsfile.feature", glue = { "stepdefinition", "hooks" }, plugin = {
        "pretty",
        "html:target/cucumber-report.html",
        "json:target/cucumber.json",
       
}, monochrome = true)
public class collectionRunner extends AbstractTestNGCucumberTests {
}