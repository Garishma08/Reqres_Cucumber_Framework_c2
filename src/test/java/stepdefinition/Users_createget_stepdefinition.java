package stepdefinition;

import base.Baseclass;
import endpoints.Endpoints;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import org.testng.Assert;
import utils.ConfigReader;
import utils.ExcelUtility;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Users_createget_stepdefinition extends Baseclass {

    private Map<String, Object> requestBody;
    private String currentSheet;
    private int currentRow;

    @Given("the user API is initialized")
    public void initAPI() {
        Assert.assertNotNull(request, "Request not initialized. Check Hooks.");
    }

    @Given("I set user endpoint for create user")
    public void setCreateUserEndpoint() {
        endpoint = Endpoints.CREATE_USER;
    }

    @Given("I set users list endpoint")
    public void setUsersListEndpoint() {
        endpoint = Endpoints.GET_USERS;
    }

    @Given("I set users list endpoint with page {string}")
    public void setUsersListWithPage(String page) {
        endpoint = Endpoints.GET_USERS_BY_PAGE + page;
    }

    @Given("I set user endpoint with valid id")
    public void setValidUserId() {
        endpoint = Endpoints.GET_USER_BY_ID + "2";
    }

    @Given("I set user endpoint with invalid id")
    public void setInvalidUserId() {
        endpoint = Endpoints.INVALID_USERS;
    }

    @Given("I set user endpoint with user id {string}")
    public void setUserId(String id) {
        endpoint = Endpoints.GET_USER_BY_ID + id;
    }

    @Given("I prepare create user request body from excel sheet {string} row {int}")
    public void prepareBodyFromExcel(String sheetName, Integer rowIndex) {

        this.currentSheet = sheetName;
        this.currentRow = rowIndex;

        String name = ExcelUtility.getCellData("data", rowIndex, "name");
        String job = ExcelUtility.getCellData("data", rowIndex, "job");

        requestBody = new HashMap<>();
        requestBody.put("name", name);
        requestBody.put("job", job);
    }

    @Given("I prepare empty user request body")
    public void prepareEmptyBody() {
        requestBody = new HashMap<>();
    }

    @Given("I prepare user request body with only name {string}")
    public void prepareOnlyName(String name) {
        requestBody = new HashMap<>();
        requestBody.put("name", name);
    }

    @Given("I set invalid API key in header")
    public void setInvalidApiKey() {
        request = RestAssured
                .given()
                .baseUri(ConfigReader.getBaseUrl())
                .header("x-api-key", "invalid_key")
                .header("Content-Type", "application/json");
    }

    @Given("I remove API key from header")
    public void removeApiKey() {
        request = RestAssured
                .given()
                .baseUri(ConfigReader.getBaseUrl())
                .header("Content-Type", "application/json");
    }

    @When("I send a POST request for user")
    public void sendPostRequest() {
        response = request
                .body(requestBody)
                .post(endpoint);

        System.out.println("RESPONSE:\n" + response.asPrettyString());
    }

    @When("I send a GET request for user")
    public void sendGetRequest() {
        response = request.get(endpoint);
    }

    @When("I send POST request to create users using below data")
    public void createMultipleUsers(DataTable table) {

        List<Map<String, String>> users = table.asMaps(String.class, String.class);

        for (Map<String, String> user : users) {

            requestBody = new HashMap<>();
            requestBody.put("name", user.get("name"));
            requestBody.put("job", user.get("job"));

            response = request.body(requestBody).post(endpoint);

            Assert.assertEquals(response.getStatusCode(), 201);
        }
    }

    @Then("the API should return status code {int} with status message {string} for user")
    public void validateStatus(int code, String message) {

        Assert.assertEquals(response.getStatusCode(), code);
        Assert.assertTrue(response.getStatusLine().contains(message));
    }

    @Then("the API should return status code should be handled as per API behavior for user")
    public void validateFlexibleStatus() {
        int code = response.getStatusCode();
        Assert.assertTrue(code >= 200 && code < 500);
    }

    @Then("the user response should match excel sheet {string} row {int}")
    public void validateResponseWithExcel(String sheet, Integer row) {

        String expectedName = ExcelUtility.getCellData("data", row, "name");
        String expectedJob = ExcelUtility.getCellData("data", row, "job");

        String actualName = response.jsonPath().getString("name");
        String actualJob = response.jsonPath().getString("job");

        Assert.assertEquals(actualName, expectedName, "Name mismatch");
        Assert.assertEquals(actualJob, expectedJob, "Job mismatch");
    }

    @Then("the response body should be valid JSON for user")
    public void validateJson() {
        Assert.assertTrue(response.getContentType().contains("application/json"));
    }

    @Then("the response should contain field {string} for user")
    public void validateField(String field) {
        Assert.assertNotNull(response.jsonPath().get(field));
    }

    @Then("validate response headers for user")
    public void validateHeaders() {
        Assert.assertNotNull(response.getHeader("Content-Type"));
    }

    @Then("the response time should be less than {int} ms for user")
    public void validateTime(int ms) {
        Assert.assertTrue(response.getTime() < ms);
    }
}