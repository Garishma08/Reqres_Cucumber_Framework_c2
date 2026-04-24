package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources/feature/Users_create-get.feature", glue = { "stepdefinition",
        "hooks" }, plugin = {
                "pretty",
                "html:target/reports/users-createget-report.html",
                "json:target/reports/users-createget-report.json"
        }, monochrome = true, tags = "@users_create_get")
public class UsersCreateGetTestRunner extends AbstractTestNGCucumberTests {
}