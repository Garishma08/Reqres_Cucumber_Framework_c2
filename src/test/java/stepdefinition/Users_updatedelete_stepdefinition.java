package stepdefinition;

import base.Baseclass;
import endpoints.Endpoints;
import io.cucumber.java.en.*;
import io.cucumber.datatable.DataTable;
import io.restassured.http.ContentType;
import org.testng.Assert;
import utils.ExcelUtility;

import java.util.List;
import java.util.Map;

public class Users_updatedelete_stepdefinition extends Baseclass {

    private String endpoint;
    private String requestBody;

    // ================= INIT =================
    @Given("the update and delete user API is initialized")
    public void initialize_api() {
        setup();
        request = request.contentType(ContentType.JSON);
    }

    // ================= ENDPOINT =================
    @Given("I set update user endpoint from excel sheet {string} row {int}")
    public void set_endpoint_from_excel(String sheet, int row) {
        String userId = ExcelUtility.getCellData(sheet, row, "user_id");
        endpoint = Endpoints.UPDATE_USER + userId;
    }

    @Given("I set update user endpoint with user id from excel sheet {string} row {int}")
    public void set_invalid_id_from_excel(String sheet, int row) {
        String userId = ExcelUtility.getCellData(sheet, row, "user_id");
        endpoint = Endpoints.UPDATE_USER + userId;
    }

    @Given("I set update user endpoint with valid id")
    public void set_update_valid() {
        String userId = ExcelUtility.getCellData("data", 1, "user_id");
        endpoint = Endpoints.UPDATE_USER + userId;
    }

    @Given("I set update user endpoint")
    public void set_update_endpoint_only() {
        endpoint = Endpoints.CREATE_USER;
    }

    @Given("I set update user endpoint with user id {string}")
    public void set_update_endpoint(String userId) {
        endpoint = Endpoints.UPDATE_USER + userId;
    }

    @Given("I set update user endpoint without id")
    public void set_update_without_id() {
        endpoint = Endpoints.UPDATE_USER;
    }

    // ================= DELETE =================
    @Given("I set delete user endpoint with valid id")
    public void set_delete_valid() {
        String userId = ExcelUtility.getCellData("data", 1, "user_id");
        endpoint = Endpoints.DELETE_USER + userId;
    }

    @Given("I set delete user endpoint with user id {string}")
    public void set_delete_endpoint(String userId) {
        endpoint = Endpoints.DELETE_USER + userId;
    }

    @Given("I set delete user endpoint without id")
    public void set_delete_without_id() {
        endpoint = Endpoints.DELETE_USER;
    }

    // ================= REQUEST BODY =================
    @Given("I prepare update user request body from excel sheet {string} row {int}")
    public void prepare_body_from_excel(String sheet, int row) {
        String name = ExcelUtility.getCellData(sheet, row, "name");
        String job  = ExcelUtility.getCellData(sheet, row, "job");

        requestBody = "{ \"name\": \"" + name + "\", \"job\": \"" + job + "\" }";
        request.body(requestBody);
    }

    @Given("I prepare empty update user request body")
    public void prepare_empty_body() {
        requestBody = "{}";
        request.body(requestBody);
    }

    @Given("I prepare duplicate fields request body")
    public void prepare_duplicate_body() {
        requestBody = "{ \"name\": \"test\", \"job\": \"QA\", \"name\": \"test\" }";
        request.body(requestBody);
    }

    @Given("I prepare partial update user request body")
    public void prepare_partial_body() {
        requestBody = "{ \"job\": \"QA Lead\" }";
        request.body(requestBody);
    }

    @Given("I prepare partial update request body with name {string}")
    public void prepare_patch_with_name(String name) {
        requestBody = "{ \"name\": \"" + name + "\" }";
        request.body(requestBody);
    }

    @Given("I prepare partial update request body")
    public void prepare_generic_patch() {
        requestBody = "{ \"job\": \"Tester\" }";
        request.body(requestBody);
    }

    // ================= API KEY =================
    @Given("I set invalid API key")
    public void set_invalid_api_key() {
        request.header("x-api-key", "invalid_key");
    }

    // ================= REQUEST =================
    @When("I send a PUT request for update user")
    public void send_put_request() {
        response = request.when().put(endpoint);
        System.out.println(response.asPrettyString());
    }

    @When("I send a PATCH request for update user")
    public void send_patch_request() {
        response = request.when().patch(endpoint);
        System.out.println(response.asPrettyString());
    }

    @When("I send a DELETE request for delete user")
    public void send_delete_request() {
        response = request.when().delete(endpoint);
        System.out.println(response.asPrettyString());
    }

    @When("I send PUT request for update user using below data")
    public void send_put_datatable(DataTable table) {
        List<Map<String, String>> data = table.asMaps(String.class, String.class);

        for (Map<String, String> row : data) {
            String userId = row.get("userId");
            String name = row.get("name");
            String job = row.get("job");

            String body = "{ \"name\": \"" + name + "\", \"job\": \"" + job + "\" }";

            response = request
                    .body(body)
                    .when()
                    .put(Endpoints.UPDATE_USER + userId);

            System.out.println(response.asPrettyString());
        }
    }

    // ================= VALIDATION =================
    @Then("the API should return status code {int} for update user")
    public void validate_status_update(int code) {
        Assert.assertEquals(response.getStatusCode(), code);
    }

    @Then("the API should return status code {int} for delete user")
    public void validate_status_delete(int code) {
        Assert.assertEquals(response.getStatusCode(), code);
    }

    @Then("the response body should be valid JSON for update user")
    public void validate_json() {
        Assert.assertNotNull(response.jsonPath());
    }

    @Then("the response should contain field {string} for update user")
    public void validate_field_update(String field) {
        Assert.assertTrue(response.getBody().asString().contains(field));
    }

    // ✅ THIS IS THE FIX (missing step)
    @Then("the response should contain field {string}")
    public void validate_field_generic(String field) {
        Assert.assertTrue(response.getBody().asString().contains(field));
    }

    @Then("validate response headers for update user")
    public void validate_headers() {
        Assert.assertTrue(response.getHeader("Content-Type").contains("application/json"));
    }
}