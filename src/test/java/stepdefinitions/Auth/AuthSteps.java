package stepdefinitions.Auth;

import base.Baseclass;
import endpoints.Endpoints;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import io.restassured.http.Method;
import org.json.JSONObject;
import utils.ExcelUtility;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class AuthSteps extends Baseclass {

    String requestBody;
    String expectedEmail;
    String expectedPassword;


    @Given("the user API is initialized")
    public void initAPI() {
        setup();
    }


    @Given("I set auth endpoint for register")
    public void setRegister() {
        endpoint = Endpoints.REGISTER;
    }

    @Given("I set auth endpoint for login")
    public void setLogin() {
        endpoint = Endpoints.LOGIN;
    }

    @Given("I set auth endpoint for list colors")
    public void setColors() {
        endpoint = Endpoints.LIST_COLORS;
    }

    @Given("I set auth endpoint for list colors with page {string}")
    public void setColorsPage(String page) {
        endpoint = Endpoints.LIST_COLORS + "?page=" + page;
    }


    @Given("I prepare valid auth request body")
    public void validRegisterBody() {
        requestBody = new JSONObject()
                .put("email", "eve.holt@reqres.in")
                .put("password", "pistol")
                .toString();
    }

    @Given("I prepare valid login request body")
    public void validLogin() {
        validRegisterBody();
    }

    @Given("I prepare auth request body without password")
    public void noPassword() {
        requestBody = new JSONObject()
                .put("email", "eve.holt@reqres.in")
                .toString();
    }

    @Given("I prepare auth request body without email")
    public void noEmail() {
        requestBody = new JSONObject()
                .put("password", "pistol")
                .toString();
    }

    @Given("I prepare login request body without password")
    public void loginNoPass() {
        noPassword();
    }

    @Given("I prepare login request body without email")
    public void loginNoEmail() {
        noEmail();
    }

    @Given("I prepare login request body with invalid email")
    public void invalidEmail() {
        requestBody = new JSONObject()
                .put("email", "invalid@reqres.in")
                .put("password", "pistol")
                .toString();
    }

    @Given("I prepare login request body with {string} and {string}")
    public void dynamicLogin(String email, String password) {
        requestBody = new JSONObject()
                .put("email", email)
                .put("password", password)
                .toString();
    }

    @Given("I prepare auth request body with {string}")
    public void conditionalBody(String type) {
        JSONObject obj = new JSONObject();

        if (type.equals("no_password")) {
            obj.put("email", "eve.holt@reqres.in");
        } else if (type.equals("no_email")) {
            obj.put("password", "pistol");
        }

        requestBody = obj.toString();
    }


    @Given("I prepare auth request body from excel sheet {string} row {int}")
    public void excelData(String sheet, int row) {

        ExcelUtility excel = new ExcelUtility();

        expectedEmail = excel.getCellData(sheet, row, "email");
        expectedPassword = excel.getCellData(sheet, row, "password");

        requestBody = new JSONObject()
                .put("email", expectedEmail)
                .put("password", expectedPassword)
                .toString();
    }


    @When("I send POST request for auth using below data")
    public void datatable(DataTable table) {

        List<Map<String, String>> data = table.asMaps(String.class, String.class);

        for (Map<String, String> row : data) {
            requestBody = new JSONObject()
                    .put("email", row.get("email"))
                    .put("password", row.get("password"))
                    .toString();

            response = request.body(requestBody).request(Method.POST, endpoint);
        }
    }


    @When("I send a POST request for auth")
    public void postRequest() {
        response = request.body(requestBody).request(Method.POST, endpoint);
    }

    @When("I send a GET request for auth")
    public void getRequest() {
        response = request.request(Method.GET, endpoint);
    }


    @Then("the API should return status code {int} with status message {string} for auth")
    public void validateStatus(Integer code, String msg) {

        assertEquals(response.getStatusCode(), code);
        assertTrue(response.getStatusLine().contains(msg));


        if (code == 200) {
            assertTrue(response.asString().contains("token") || response.asString().contains("data"));
        }

        if (code == 400) {
            assertTrue(response.asString().contains("error"));
        }
    }


    @Then("the response time should be less than {int} ms for auth")
    public void timeCheck(int time) {
        assertTrue(response.getTime() < time);
    }

    @Then("the response body should be valid JSON for auth")
    public void jsonCheck() {
        assertNotNull(response.jsonPath());
    }

    @Then("the response should contain field {string} for auth")
    public void fieldCheck(String key) {
        assertNotNull(response.jsonPath().get(key));
    }

    @Then("validate response headers for auth")
    public void headerCheck() {
        assertTrue(response.getHeader("Content-Type").contains("application/json"));
    }


    @Then("the auth response should match excel sheet {string} row {int}")
    public void excelValidation(String sheet, int row) {

        assertTrue(requestBody.contains(expectedEmail));
        assertTrue(requestBody.contains(expectedPassword));

        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().get("id"));
        assertNotNull(response.jsonPath().get("token"));
    }


    @Given("I set invalid API key in header")
    public void invalidKey() {
        request.header("x-api-key", "invalid_key");
    }

    @Then("the API should return status code should be handled as per API behavior for auth")
    public void invalidKeyValidation() {
        int status = response.getStatusCode();
        assertTrue(status == 401 || status == 403);
    }
}