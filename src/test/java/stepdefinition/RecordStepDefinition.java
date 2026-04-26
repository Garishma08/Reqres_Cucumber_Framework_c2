package stepdefinition;

import base.Baseclass;
import endpoints.Endpoints;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import utils.ConfigReader;
import utils.ExcelUtility;

import java.util.List;
import java.util.Map;

public class RecordStepDefinition extends Baseclass {

    private String currentSlug;
    private String currentRecordId;
    private String endpoint;
    private String emptySlug=" ";
    private String invalidSlug="@#$%";
    private String invalidRecordId="@#$%";
    private String emptyRecordId=" ";
    
    private void ensureRecordId() {
        if (currentRecordId == null) {
            create_and_store_id();
        }
    }


    @Given("the API is initialized")
    public void init_api() {

        RestAssured.baseURI = ConfigReader.getBaseUrl();

        request = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .header("x-api-key", ConfigReader.getApiKey());
    }


    private void loadSlug() {
        currentSlug = ExcelUtility.getCellData("data", 1, "col_slug");
    }


    @Given("I set create record endpoint from excel sheet {string} row {int}")
    public void set_create_endpoint_excel(String sheet, Integer row) {

        currentSlug = ExcelUtility.getCellData(sheet, row, "col_slug");
        endpoint = Endpoints.CREATE_RECORD.replace("{slug}", currentSlug);
    }

    @Given("I prepare record request body from excel sheet {string} row {int}")
    public void prepare_body_excel(String sheet, Integer row) {

        String value = ExcelUtility.getCellData(sheet, row, "TEST");

        JSONObject body = new JSONObject();
        body.put("data", new JSONObject().put("test", value));

        request.body(body.toString());
    }

    @Given("I set create record endpoint")
    public void set_create_endpoint() {
        loadSlug();
        endpoint = Endpoints.CREATE_RECORD.replace("{slug}", currentSlug);
    }

    @Given("I set create record endpoint with invalid slug")
    public void invalid_slug_create() {
        endpoint = Endpoints.CREATE_RECORD.replace("{slug}", invalidSlug);
    }

    @Given("I prepare empty record request body")
    public void empty_body() {
        request.body("{}");
    }

    @Given("I send raw empty string body")
    public void raw_body() {
        request.body("");
    }

    @When("I send a POST request for record")
    public void send_post() {

        response = request.post(endpoint);

        if (response.getStatusCode() == 201) {
            currentRecordId = response.jsonPath().getString("id");
        }

        printResponse();
    }


    @Given("I set get records endpoint")
    public void set_get_all() {
        loadSlug();
        endpoint = Endpoints.GET_RECORDS.replace("{slug}", currentSlug);
    }

    @Given("I set get records endpoint with invalid slug")
    public void invalid_slug_get() {
        endpoint = Endpoints.GET_RECORDS.replace("{slug}", invalidSlug);
    }

    @Given("I set get records endpoint with missing slug")
    public void missing_slug_get() {
        endpoint = Endpoints.GET_RECORDS.replace("{slug}", emptySlug);
    }

    @When("I send a GET request for records")
    public void send_get_all() {
        response = request.get(endpoint);
        printResponse();
    }


    @Given("I create a record and store its id")
    public void create_and_store_id() {

        currentSlug = ExcelUtility.getCellData("data", 1, "col_slug");

        JSONObject body = new JSONObject();
        body.put("data", new JSONObject().put("test", "temp"));

        response = request.body(body.toString())
                .post(Endpoints.CREATE_RECORD.replace("{slug}", currentSlug));

        currentRecordId = response.jsonPath().getString("data.id");
    }

    @Given("I set get record endpoint with valid id")
    public void get_valid_id() {

        loadSlug();

        endpoint = Endpoints.GET_RECORD_BY_ID
                .replace("{slug}", currentSlug)
                .replace("{id}", currentRecordId);
    }

    @Given("I set get record endpoint with invalid id")
    public void get_invalid_id() {

        loadSlug();

        endpoint = Endpoints.GET_RECORD_BY_ID
                .replace("{slug}", currentSlug)
                .replace("{id}", invalidSlug);
    }

    @Given("I set get record endpoint without id")
    public void get_without_id() {

        loadSlug();

        endpoint = Endpoints.GET_RECORD_BY_ID
                .replace("{slug}", currentSlug)
                .replace("/{id}", emptyRecordId);
    }

    @Given("I set get record endpoint with invalid slug")
    public void get_invalid_slug() {

        endpoint = Endpoints.GET_RECORD_BY_ID
                .replace("{slug}", invalidSlug)
                .replace("{id}", currentRecordId);
    }

    @When("I send a GET request for record")
    public void send_get_single() {
        response = request.get(endpoint);
        printResponse();
    }


    @Given("I set update record endpoint from excel sheet {string} row {int}")
    public void set_update_excel(String sheet, Integer row) {

        currentSlug = ExcelUtility.getCellData(sheet, row, "col_slug");
        currentRecordId = ExcelUtility.getCellData(sheet, row, "project_id");

        endpoint = Endpoints.UPDATE_RECORD
                .replace("{slug}", currentSlug)
                .replace("{id}", currentRecordId);
    }

    @Given("I prepare update record request body from excel sheet {string} row {int}")
    public void prepare_update_excel(String sheet, Integer row) {

        String value = ExcelUtility.getCellData(sheet, row, "u_test");

        JSONObject body = new JSONObject();
        body.put("data", new JSONObject().put("test", value));

        request.body(body.toString());
    }

    @Given("I set update record endpoint with valid id")
    public void update_valid() {

        loadSlug();

        endpoint = Endpoints.UPDATE_RECORD
                .replace("{slug}", currentSlug)
                .replace("{id}", currentRecordId);
    }

    @Given("I set update record endpoint with invalid id")
    public void update_invalid_id() {

        loadSlug();

        endpoint = Endpoints.UPDATE_RECORD
                .replace("{slug}", currentSlug)
                .replace("{id}", invalidRecordId);
    }

    @Given("I set update record endpoint without id")
    public void update_without_id() {

        loadSlug();

        endpoint = Endpoints.UPDATE_RECORD
                .replace("{slug}", currentSlug)
                .replace("/{id}", emptyRecordId);
    }

    @Given("I set update record endpoint with invalid slug")
    public void update_invalid_slug() {

        ensureRecordId();   

        endpoint = Endpoints.UPDATE_RECORD
                .replace("{slug}", invalidSlug)
                .replace("{id}", currentRecordId);
    }

    @Given("I prepare update record request body")
    public void prepare_update_body() {

        JSONObject body = new JSONObject();
        body.put("data", new JSONObject().put("test", "temp"));

        request.body(body.toString());
    }

    @When("I send a PUT request for record")
    public void send_put() {
        response = request.put(endpoint);
        printResponse();
    }

    @Given("I prepare partial record request body")
    public void partial_body() {

        JSONObject body = new JSONObject();
        body.put("data", new JSONObject().put("test", "partial"));

        request.body(body.toString());
    }

    @When("I send a PATCH request for record")
    public void send_patch() {
        response = request.patch(endpoint);
        printResponse();
    }

    @Given("I set delete record endpoint with valid id")
    public void delete_valid() {

        loadSlug();

        endpoint = Endpoints.DELETE_RECORD
                .replace("{slug}", currentSlug)
                .replace("{id}", currentRecordId);
    }

    @Given("I set delete record endpoint with invalid id")
    public void delete_invalid() {

        loadSlug();

        endpoint = Endpoints.DELETE_RECORD
                .replace("{slug}", currentSlug)
                .replace("{id}", invalidRecordId);
    }

    @Given("I set delete record endpoint without id")
    public void delete_without_id() {

        loadSlug();

        endpoint = Endpoints.DELETE_RECORD
                .replace("{slug}", currentSlug)
                .replace("/{id}", emptyRecordId);
    }

    @When("I send a DELETE request for record")
    public void send_delete() {
        response = request.delete(endpoint);
        printResponse();
    }
    
    @Given("I set delete collection endpoint from excel sheet {string} row {int}")
    public void set_delete_collection(String sheet, Integer row) {

        String slug = ExcelUtility.getCellData(sheet, row, "col_slug");

        endpoint = Endpoints.DELETE_COLLECTION+ slug;
    }
    
    @When("I send a DELETE request for collection")
    public void send_delete_collection() {
        response = request.delete(endpoint);
        printResponse();
    }

    @When("I send POST request for record using below data")
    public void datatable_post(DataTable table) {

        loadSlug();

        List<Map<String, String>> rows = table.asMaps(String.class, String.class);

        for (Map<String, String> row : rows) {

            JSONObject body = new JSONObject();
            body.put("data", new JSONObject()
                    .put("name", row.get("name"))
                    .put("value", row.get("value")));

            response = request.body(body.toString())
                    .post(Endpoints.CREATE_RECORD.replace("{slug}", currentSlug));

            printResponse();
        }
    }


    @Then("the API should return status code {int} for record")
    public void validate_status(int code) {
        response.then().statusCode(code);
    }

    @Then("the response body should be valid JSON for record")
    public void validate_json() {
        response.then().contentType("application/json");
    }

    @Then("the response should contain field {string}")
    public void validate_field(String field) {
        response.then().body(Matchers.containsString(field));
    }

    @Then("the response should contain a non-empty data array")
    public void validate_array() {
        response.then().body("data", Matchers.not(Matchers.empty()));
    }

    @Then("the response should contain error and message")
    public void validate_error_message() {

        response.then().body("error", Matchers.notNullValue());
        response.then().body("message", Matchers.notNullValue());
    }
    
    @Then("the API should return status code {int} for collection")
    public void validate_collection_status(int code) {
        response.then().statusCode(code);
    }

    private void printResponse() {
        System.out.println("STATUS CODE: " + response.getStatusCode());
        System.out.println(response.asPrettyString());
    }
}