package stepdefinition;

import base.Baseclass;
import endpoints.Endpoints;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.Assert;
import utils.ConfigReader;
import utils.ExcelUtility;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.lessThan;

public class CollectionStep extends Baseclass {

    private static String savedSlug;
    private String currentSlug;
    private String updateName;
    private String updateSlug;

    @Given("Base Url is set")
    public void base_url_is_set() {
        setup();
    }

    @When("I get collection using {string}")
    public void i_get_collection_using(String authType) {
        if (authType.equalsIgnoreCase("authentication")) {
            response = request.get(Endpoints.GET_COLLECTIONS);
        } else {
            response = RestAssured
                    .given()
                    .header("Content-Type", "application/json")
                    .get(ConfigReader.getBaseUrl() + Endpoints.GET_COLLECTIONS);
        }
        printResponse();
    }

    @Given("I prepare collection payload from excel {string}")
    public void i_prepare_collection_payload_from_excel(String row) {
        int rowNum = Integer.parseInt(row);

        String colName    = ExcelUtility.getCellData("data", rowNum, "col_name");
        String colSlug    = ExcelUtility.getCellData("data", rowNum, "col_slug");
        String projectId  = ExcelUtility.getCellData("data", rowNum, "project_id");
        String visibility = ExcelUtility.getCellData("data", rowNum, "visibility");

        currentSlug = colSlug;

        JSONObject body = new JSONObject();
        body.put("name",       colName);
        body.put("slug",       colSlug);
        body.put("project_id", projectId);
        body.put("visibility", visibility);

        request = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .header("x-api-key", ConfigReader.getApiKey())
                .body(body.toString());

        System.out.println("COLLECTION REQUEST BODY: " + body);
    }

    @When("I send a POST request for collection")
    public void i_send_a_post_request_for_collection() {
        response = request.post(Endpoints.CREATE_COLLECTION);
        printResponse();
    }

    @Then("I save collection slug")
    public void i_save_collection_slug() {
        savedSlug = response.jsonPath().getString("data.slug");

        if (savedSlug == null) {
            savedSlug = currentSlug;
        }

        Assert.assertNotNull(savedSlug, "Collection slug was not saved");
        System.out.println("SAVED COLLECTION SLUG: " + savedSlug);
    }

    @Given("I prepare duplicate collection payload")
    public void i_prepare_duplicate_collection_payload() {
        Assert.assertNotNull(savedSlug,
                "Create collection scenario must run before duplicate scenario");

        JSONObject body = new JSONObject();
        body.put("name",       "Duplicate Collection");
        body.put("slug",       savedSlug);
        body.put("project_id", ConfigReader.getProjectId());
        body.put("visibility", "private");

        request = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .header("x-api-key", ConfigReader.getApiKey())
                .body(body.toString());

        System.out.println("DUPLICATE REQUEST BODY: " + body);
    }

    @Given("I prepare collection payload with missing fields")
    public void i_prepare_collection_payload_with_missing_fields() {
        JSONObject body = new JSONObject();
        body.put("visibility", "private");

        request = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .header("x-api-key", ConfigReader.getApiKey())
                .body(body.toString());

        System.out.println("MISSING FIELD REQUEST BODY: " + body);
    }

    @Given("I prepare invalid datatype payload with data")
    public void i_prepare_invalid_datatype_payload_with_data(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> data = rows.get(0);

        String nameValue = data.get("name");
        String slug      = data.get("slug");
        String projectId = data.get("project_id");
        String visibility= data.get("visibility");

        String body;

        if (nameValue.equalsIgnoreCase("null")) {
            body = "{\"name\":null"
                 + ",\"slug\":\""       + slug       + "\""
                 + ",\"project_id\":"   + projectId
                 + ",\"visibility\":\"" + visibility + "\"}";
        } else {
            body = "{\"name\":\""       + nameValue  + "\""
                 + ",\"slug\":\""       + slug       + "\""
                 + ",\"project_id\":"   + projectId
                 + ",\"visibility\":\"" + visibility + "\"}";
        }

        request = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .header("x-api-key", ConfigReader.getApiKey())
                .body(body);

        System.out.println("INVALID DATATYPE REQUEST BODY: " + body);
    }


    @Given("I use saved collection slug")
    public void i_use_saved_collection_slug() {
        Assert.assertNotNull(savedSlug,
                "Saved slug is null. Create collection must run first.");
        currentSlug = savedSlug;

        request = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .header("x-api-key", ConfigReader.getApiKey());

        System.out.println("USING SAVED SLUG: " + currentSlug);
    }

    @Given("I set invalid collection slug {string}")
    public void i_set_invalid_collection_slug(String slug) {
        currentSlug = slug;

        request = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .header("x-api-key", ConfigReader.getApiKey());
    }

    @When("I send GET request for collection")
    public void i_send_get_request_for_collection() {
        response = request.get(
                ConfigReader.getBaseUrl()
                        + Endpoints.GET_COLLECTION_BY_SLUG
                        + currentSlug);
        printResponse();
    }

    @When("I get collection without authentication using slug {string}")
    public void i_get_collection_without_authentication_using_slug(String slug) {
        response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .get(ConfigReader.getBaseUrl()
                        + Endpoints.GET_COLLECTION_BY_SLUG
                        + slug);
        printResponse();
    }

    @Given("I read update collection data from excel {string}")
    public void i_read_update_collection_data_from_excel(String row) {
        int rowNum = Integer.parseInt(row);

        updateName = ExcelUtility.getCellData("data", rowNum, "u_name1");
        updateSlug = ExcelUtility.getCellData("data", rowNum, "u_slug");

        System.out.println("UPDATE NAME: " + updateName);
        System.out.println("UPDATE SLUG: " + updateSlug);
    }

    @When("I send PUT request for collection")
    public void i_send_put_request_for_collection() {
        JSONObject body = new JSONObject();
        body.put("name", updateName);
        body.put("slug", updateSlug);

        response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .header("x-api-key", ConfigReader.getApiKey())
                .body(body.toString())
                .put(ConfigReader.getBaseUrl()
                        + Endpoints.UPDATE_COLLECTION
                        + currentSlug);

        if (response.getStatusCode() == 200) {
            savedSlug   = response.jsonPath().getString("data.slug");
            currentSlug = savedSlug;
        }

        printResponse();
    }

    @When("I update collection using invalid slug {string}")
    public void i_update_collection_using_invalid_slug(String slug) {
        JSONObject body = new JSONObject();
        body.put("name", "Updated Collection");
        body.put("slug", "updated-slug");

        response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .header("x-api-key", ConfigReader.getApiKey())
                .body(body.toString())
                .put(ConfigReader.getBaseUrl()
                        + Endpoints.UPDATE_COLLECTION
                        + slug);
        printResponse();
    }

    @When("I update collection without authentication using slug {string}")
    public void i_update_collection_without_authentication_using_slug(String slug) {
        JSONObject body = new JSONObject();
        body.put("name", "Updated Collection");
        body.put("slug", "updated-slug");

        response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(body.toString())
                .put(ConfigReader.getBaseUrl()
                        + Endpoints.UPDATE_COLLECTION
                        + slug);
        printResponse();
    }

    @Then("I validate status code {int}")
    public void i_validate_status_code(int expectedStatusCode) {
        response.then()
                .statusCode(expectedStatusCode);
    }

    @Then("I validate response time")
    public void i_validate_response_time() {
        response.then()
                .time(lessThan(6000L));
    }

    @Then("I validate content type")
    public void i_validate_content_type() {
        response.then()
                .contentType(Matchers.containsString("application/json"));
    }

    private void printResponse() {
        System.out.println("STATUS CODE:   " + response.getStatusCode());
        System.out.println("RESPONSE TIME: " + response.getTime() + " ms");
        System.out.println("RESPONSE BODY:");
        System.out.println(response.asPrettyString());
    }
}