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

import static org.hamcrest.Matchers.*;

public class Users_createget_stepdefinition extends Baseclass {

    private Map<String, Object> requestBody;

    @Given("the user API is initialized")
    public void initAPI() {
        request = RestAssured
                .given()
                .baseUri(ConfigReader.getBaseUrl())
                .header("x-api-key", ConfigReader.getApiKey())
                .header("Content-Type", "application/json");

        Assert.assertNotNull(request, "Request not initialized");
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
        endpoint = Endpoints.GET_USER_BY_ID + ConfigReader.getUserId();
    }

    @Given("I set user endpoint with invalid id")
    public void setInvalidUserId() {
        endpoint = Endpoints.GET_USER_BY_ID + "9999";
    }

    @Given("I set user endpoint with user id {string}")
    public void setUserId(String id) {
        endpoint = Endpoints.GET_USER_BY_ID + id;
    }

    @Given("I prepare valid user request body")
    public void prepareValidUserBody() {
        requestBody = new HashMap<>();
        requestBody.put("name", "morpheus");
        requestBody.put("job", "leader");
    }

    @Given("I prepare create user request body from excel sheet {string} row {int}")
    public void prepareBodyFromExcel(String sheetName, Integer rowIndex) {
        String name = ExcelUtility.getCellData(sheetName, rowIndex, "name");
        String job = ExcelUtility.getCellData(sheetName, rowIndex, "job");

        requestBody = new HashMap<>();
        requestBody.put("name", name);
        requestBody.put("job", job);

    }

    @Given("I prepare empty user request body")
    public void prepareEmptyBody() {
        requestBody = null;
    }

    @Given("I prepare user request body with only name {string}")
    public void prepareOnlyName(String name) {
        requestBody = new HashMap<>();
        requestBody.put("name", name);
    }

    @Given("I set invalid API key in header for user")
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

        if (requestBody != null) {
            response = request.body(requestBody).post(endpoint);
        } else {
            response = request.post(endpoint);
        }

    }

    @When("I send a GET request for user")
    public void sendGetRequest() {
        response = request.get(endpoint);

    }

    @When("I send a GET request for user by id")
    public void sendGetRequestById() {
        response = request.get(endpoint);

    }

    @When("I send POST request to create users using below data")
    public void sendPostRequestWithDataTable(DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> user : data) {
            requestBody = new HashMap<>();
            requestBody.put("name", user.get("name"));
            requestBody.put("job", user.get("job"));

            response = request.body(requestBody).post(endpoint);

            Assert.assertEquals(response.getStatusCode(), 201);
            Assert.assertEquals(getStatusMessage(), "Created");
        }
    }

    @Then("the API should return status code {int} with status message {string} for user")
    public void validateStatus(int code, String message) {
        Assert.assertEquals(response.getStatusCode(), code);
        Assert.assertEquals(getStatusMessage(), message);
    }

    @Then("the API should return status code should be {int} for user")
    public void validateOnlyStatusCode(int code) {
        Assert.assertEquals(response.getStatusCode(), code);
    }

    @Then("the API should return status code should be {int} Unauthorized for user")
    public void validateUnauthorized(int code) {
        Assert.assertEquals(response.getStatusCode(), code);
        Assert.assertEquals(getStatusMessage(), "Unauthorized");
    }

    @Then("the response should contain field {string} for user")
    public void validateFieldPresent(String field) {
        Assert.assertNotNull(response.jsonPath().get(field));
    }

    @Then("the user response should match excel sheet {string} row {int}")
    public void validateResponseWithExcel(String sheetName, Integer rowIndex) {
        String expectedName = ExcelUtility.getCellData("data", rowIndex, "name");
        String expectedJob = ExcelUtility.getCellData("data", rowIndex, "job");

        String actualName = response.jsonPath().getString("name");
        String actualJob = response.jsonPath().getString("job");

        Assert.assertEquals(actualName, expectedName);
        Assert.assertEquals(actualJob, expectedJob);
    }

    @Then("the response body should be valid JSON for user")
    public void validateJson() {
        Assert.assertEquals(response.getContentType(), "application/json; charset=utf-8");
    }

    @Then("validate response headers for user")
    public void validateHeaders() {
        Assert.assertNotNull(response.getHeader("Content-Type"));
    }

    @Then("the response time should be less than {int} ms for user")
    public void validateTime(long time) {
        response.then().time(lessThan(5000L));
    }

    private String getStatusMessage() {
        return response.getStatusLine().replace("HTTP/1.1 " + response.getStatusCode(), "").trim();
    }

}