package stepdefinitions;

import base.Baseclass;
import endpoints.Endpoints;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import org.testng.Assert;
import utils.ConfigReader;
import utils.ExcelUtility;

import java.util.List;
import java.util.Map;

public class CollectionStepDefinition extends Baseclass {

    private static String savedSlug;
    private String currentSlug;
    private String updatedName;
    private String updatedSlug;

    // ─────────────────────────────────────────────
    // BACKGROUND
    // ─────────────────────────────────────────────

    @Given("Base Url is set")
    public void base_url_is_set() {
        Assert.assertNotNull(RestAssured.baseURI, "Base URI should not be null");
    }

    // ─────────────────────────────────────────────
    // TC1 - GET collection successfully
    // TC2 - GET collection unauthorized
    // ─────────────────────────────────────────────

    @When("I get collection using {string}")
    public void i_get_collection_using(String authType) {
        if (authType.equalsIgnoreCase("authentication")) {
            response = request
                    .when()
                    .get(Endpoints.GET_COLLECTIONS);
        }
    }

    // ─────────────────────────────────────────────
    // TC3 - Create collection using Excel data
    // ─────────────────────────────────────────────

    @Given("I prepare collection payload from excel {string}")
    public void i_prepare_collection_payload_from_excel(String rowNumber) {
        int rowNum = Integer.parseInt(rowNumber);

        String name       = ExcelUtility.getCellData("Sheet1", rowNum, "col_name");
        String slug       = ExcelUtility.getCellData("Sheet1", rowNum, "col_slug");
        String projectId  = ExcelUtility.getCellData("Sheet1", rowNum, "project_id");
        String visibility = ExcelUtility.getCellData("Sheet1", rowNum, "visibility");

        String body = String.format(
                "{\"name\":\"%s\",\"slug\":\"%s\",\"project_id\":%s,\"visibility\":\"%s\"}",
                name, slug, projectId, visibility
        );

        request = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .header("x-api-key", ConfigReader.getApiKey())
                .body(body);
    }

    @When("I send a POST request for collection")
    public void i_send_a_post_request_for_collection() {
        response = request
                .when()
                .post(Endpoints.CREATE_COLLECTION);
    }

    @Then("validate collection fields from excel {string}")
    public void validate_collection_fields_from_excel(String rowNumber) {
        int rowNum = Integer.parseInt(rowNumber);

        String expectedName       = ExcelUtility.getCellData("Sheet1", rowNum, "col_name");
        String expectedSlug       = ExcelUtility.getCellData("Sheet1", rowNum, "col_slug");
        String expectedVisibility = ExcelUtility.getCellData("Sheet1", rowNum, "visibility");

        String actualName       = response.jsonPath().getString("name");
        String actualSlug       = response.jsonPath().getString("slug");
        String actualVisibility = response.jsonPath().getString("visibility");

        Assert.assertEquals(actualName,       expectedName,       "Name mismatch");
        Assert.assertEquals(actualSlug,       expectedSlug,       "Slug mismatch");
        Assert.assertEquals(actualVisibility, expectedVisibility, "Visibility mismatch");
    }

    @Then("I save collection slug")
    public void i_save_collection_slug() {
        savedSlug = response.jsonPath().getString("slug");
        Assert.assertNotNull(savedSlug, "Slug should not be null after creation");
    }

    // ─────────────────────────────────────────────
    // TC4 - Duplicate collection creation
    // ─────────────────────────────────────────────

    @Given("I prepare duplicate collection payload")
    public void i_prepare_duplicate_collection_payload() {
        String body = "{\"name\":\"Orders\",\"slug\":\"orders\",\"project_id\":14543,\"visibility\":\"private\"}";

        request = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .header("x-api-key", ConfigReader.getApiKey())
                .body(body);
    }

    // ─────────────────────────────────────────────
    // TC5 - Create collection with missing fields
    // ─────────────────────────────────────────────

    @Given("I prepare collection payload with missing fields")
    public void i_prepare_collection_payload_with_missing_fields() {
        String body = "{\"name\":\"MissingFieldsCollection\"}";

        request = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .header("x-api-key", ConfigReader.getApiKey())
                .body(body);
    }

    // ─────────────────────────────────────────────
    // TC6 - Invalid datatype payload
    // ─────────────────────────────────────────────

    @Given("I prepare invalid datatype payload")
    public void i_prepare_invalid_datatype_payload() {
        String body = "{\"name\":\"Test\",\"slug\":\"test\",\"project_id\":\"INVALID\",\"visibility\":\"private\"}";

        request = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .header("x-api-key", ConfigReader.getApiKey())
                .body(body);
    }

    // ─────────────────────────────────────────────
    // TC7 - GET collection using saved slug
    // ─────────────────────────────────────────────

    @Given("I use saved collection slug")
    public void i_use_saved_collection_slug() {
        Assert.assertNotNull(savedSlug, "Saved slug must not be null. Run Create scenario first.");
        currentSlug = savedSlug;
    }

    @When("I send GET request for collection")
    public void i_send_get_request_for_collection() {
        response = request
                .when()
                .get(Endpoints.GET_COLLECTION_BY_SLUG + currentSlug);
    }

    // ─────────────────────────────────────────────
    // TC8 - GET collection with invalid slug
    // ─────────────────────────────────────────────

    @Given("I have invalid collection slug:")
    public void i_have_invalid_collection_slug(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        currentSlug = rows.get(0).get("slug");
    }

    @When("I send GET request using invalid slug")
    public void i_send_get_request_using_invalid_slug() {
        response = request
                .when()
                .get(Endpoints.GET_COLLECTION_BY_SLUG + currentSlug);
    }

    // ─────────────────────────────────────────────
    // TC9 - GET collection without authentication
    // ─────────────────────────────────────────────

    @When("I get collection without authentication using slug {string}")
    public void i_get_collection_without_authentication_using_slug(String slug) {
        response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .when()
                .get(ConfigReader.getBaseUrl() + Endpoints.GET_COLLECTION_BY_SLUG + slug);
    }

    // ─────────────────────────────────────────────
    // TC10 - UPDATE collection using Excel data
    // ─────────────────────────────────────────────

    @Given("I read collection data from Excel row {string}")
    public void i_read_collection_data_from_excel_row(String rowNumber) {
        int rowNum = Integer.parseInt(rowNumber);
        updatedName = ExcelUtility.getCellData("Sheet1", rowNum, "u_name1");
        updatedSlug = ExcelUtility.getCellData("Sheet1", rowNum, "u_slug");
    }

    @Given("I prepare updated slug payload")
    public void i_prepare_updated_slug_payload() {
        String body = String.format(
                "{\"name\":\"%s\",\"slug\":\"%s\"}",
                updatedName, updatedSlug
        );

        request = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .header("x-api-key", ConfigReader.getApiKey())
                .body(body);
    }

    @When("I send PUT request for collection")
    public void i_send_put_request_for_collection() {
        response = request
                .when()
                .put(Endpoints.UPDATE_COLLECTION + currentSlug);
    }

    // ─────────────────────────────────────────────
    // TC11 - UPDATE collection with invalid slug
    // ─────────────────────────────────────────────

    @When("I update collection using invalid slug {string}")
    public void i_update_collection_using_invalid_slug(String slug) {
        String body = "{\"name\":\"Updated\",\"slug\":\"updated-slug\"}";

        response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .header("x-api-key", ConfigReader.getApiKey())
                .body(body)
                .when()
                .put(Endpoints.UPDATE_COLLECTION + slug);
    }

    // ─────────────────────────────────────────────
    // TC12 - UPDATE collection without authentication
    // ─────────────────────────────────────────────

    @When("I update collection without authentication using slug {string}")
    public void i_update_collection_without_authentication_using_slug(String slug) {
        String body = "{\"name\":\"Updated\",\"slug\":\"updated-slug\"}";

        response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .put(ConfigReader.getBaseUrl() + Endpoints.UPDATE_COLLECTION + slug);
    }

    // ─────────────────────────────────────────────
    // COMMON VALIDATIONS
    // ─────────────────────────────────────────────

    @Then("I validate status code {int}")
    public void i_validate_status_code(int expectedStatusCode) {
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode,
                "Status code mismatch. Response: " + response.getBody().asString());
    }

    @Then("I validate response time")
    public void i_validate_response_time() {
        long responseTime = response.getTime();
        Assert.assertTrue(responseTime < 5000,
                "Response time exceeded 5000ms. Actual: " + responseTime + "ms");
    }

    @Then("I validate content type")
    public void i_validate_content_type() {
        String contentType = response.getContentType();
        Assert.assertTrue(contentType.contains("application/json"),
                "Expected application/json but got: " + contentType);
    }

    @Then("I validate success response")
    public void i_validate_success_response() {
        String body = response.getBody().asString();
        Assert.assertNotNull(body, "Response body should not be null");
        Assert.assertFalse(body.isEmpty(), "Response body should not be empty");
    }

    @Then("I validate error response")
    public void i_validate_error_response() {
        String body = response.getBody().asString();
        Assert.assertNotNull(body, "Error response body should not be null");
        Assert.assertFalse(body.isEmpty(), "Error response body should not be empty");
    }

    @Then("I validate error message Unauthorized")
    public void i_validate_error_message_unauthorized() {
        String body = response.getBody().asString();
        Assert.assertTrue(
                body.toLowerCase().contains("unauthorized") || body.toLowerCase().contains("401"),
                "Expected 'Unauthorized' in response but got: " + body
        );
    }
}