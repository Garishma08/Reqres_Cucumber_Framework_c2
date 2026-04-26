package stepdefinition;

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

    @Given("the Auth API is initialized")
    public void initAPI() {
        setup();
    }

    @Given("I set auth endpoint for register")
    public void setRegister() {
        endpoint = Endpoints.REGISTER;
        System.out.println("AUTH ENDPOINT: " + endpoint);
    }

    @Given("I set auth endpoint for login")
    public void setLogin() {
        endpoint = Endpoints.LOGIN;
        System.out.println("AUTH ENDPOINT: " + endpoint);
    }

    @Given("I set auth endpoint for list colors")
    public void setColors() {
        endpoint = Endpoints.LIST_COLORS;
        System.out.println("AUTH ENDPOINT: " + endpoint);
    }

    @Given("I set auth endpoint for list colors with page {string}")
    public void setColorsPage(String page) {
        endpoint = Endpoints.LIST_COLORS + "?page=" + page;
        System.out.println("AUTH ENDPOINT: " + endpoint);
    }

    @Given("I prepare valid auth request body")
    public void validRegisterBody() {
        expectedEmail = ExcelUtility.getCellData("data", 1, "Email");
        expectedPassword = ExcelUtility.getCellData("data", 1, "Password");

        requestBody = new JSONObject()
                .put("email", expectedEmail)
                .put("password", expectedPassword)
                .toString();

        System.out.println("EMAIL FROM EXCEL: " + expectedEmail);
        System.out.println("PASSWORD FROM EXCEL: " + expectedPassword);
        System.out.println("AUTH REQUEST BODY: " + requestBody);
    }

    @Given("I prepare valid login request body")
    public void validLogin() {
        validRegisterBody();
    }

    @Given("I prepare auth request body without password")
    public void noPassword() {
        expectedEmail = ExcelUtility.getCellData("data", 1, "Email");

        requestBody = new JSONObject()
                .put("email", expectedEmail)
                .toString();

        System.out.println("EMAIL FROM EXCEL: " + expectedEmail);
        System.out.println("AUTH REQUEST BODY WITHOUT PASSWORD: " + requestBody);
    }

    @Given("I prepare auth request body without email")
    public void noEmail() {
        expectedPassword = ExcelUtility.getCellData("data", 1, "Password");

        requestBody = new JSONObject()
                .put("password", expectedPassword)
                .toString();

        System.out.println("PASSWORD FROM EXCEL: " + expectedPassword);
        System.out.println("AUTH REQUEST BODY WITHOUT EMAIL: " + requestBody);
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
        expectedEmail = "invalid@reqres.in";
        expectedPassword = ExcelUtility.getCellData("data", 1, "Password");

        requestBody = new JSONObject()
                .put("email", expectedEmail)
                .put("password", expectedPassword)
                .toString();

        System.out.println("INVALID EMAIL: " + expectedEmail);
        System.out.println("PASSWORD FROM EXCEL: " + expectedPassword);
        System.out.println("AUTH REQUEST BODY: " + requestBody);
    }

    @Given("I prepare login request body with {string} and {string}")
    public void dynamicLogin(String email, String password) {
        expectedEmail = email;
        expectedPassword = password;

        requestBody = new JSONObject()
                .put("email", expectedEmail)
                .put("password", expectedPassword)
                .toString();

        System.out.println("EMAIL FROM FEATURE: " + expectedEmail);
        System.out.println("PASSWORD FROM FEATURE: " + expectedPassword);
        System.out.println("AUTH REQUEST BODY: " + requestBody);
    }

    @Given("I prepare auth request body with {string}")
    public void conditionalBody(String type) {
        JSONObject obj = new JSONObject();

        if (type.equals("no_password")) {
            expectedEmail = ExcelUtility.getCellData("data", 1, "Email");
            obj.put("email", expectedEmail);
            System.out.println("EMAIL FROM EXCEL: " + expectedEmail);
        } else if (type.equals("no_email")) {
            expectedPassword = ExcelUtility.getCellData("data", 1, "Password");
            obj.put("password", expectedPassword);
            System.out.println("PASSWORD FROM EXCEL: " + expectedPassword);
        }

        requestBody = obj.toString();
        System.out.println("AUTH REQUEST BODY: " + requestBody);
    }

    @Given("I prepare auth request body from excel sheet {string} row {int}")
    public void excelData(String sheet, int row) {
        expectedEmail = ExcelUtility.getCellData(sheet, row, "Email");
        expectedPassword = ExcelUtility.getCellData(sheet, row, "Password");

        requestBody = new JSONObject()
                .put("email", expectedEmail)
                .put("password", expectedPassword)
                .toString();

        System.out.println("========== EXCEL AUTH DATA ==========");
        System.out.println("SHEET: " + sheet);
        System.out.println("ROW: " + row);
        System.out.println("EMAIL FROM EXCEL: " + expectedEmail);
        System.out.println("PASSWORD FROM EXCEL: " + expectedPassword);
        System.out.println("AUTH REQUEST BODY: " + requestBody);
        System.out.println("=====================================");
    }

    @When("I send POST request for auth using below data")
    public void datatable(DataTable table) {
        List<Map<String, String>> data = table.asMaps(String.class, String.class);

        for (Map<String, String> row : data) {
            expectedEmail = row.get("email");
            expectedPassword = row.get("password");

            requestBody = new JSONObject()
                    .put("email", expectedEmail)
                    .put("password", expectedPassword)
                    .toString();

            System.out.println("EMAIL FROM DATATABLE: " + expectedEmail);
            System.out.println("PASSWORD FROM DATATABLE: " + expectedPassword);
            System.out.println("AUTH REQUEST BODY: " + requestBody);

            response = request.body(requestBody).request(Method.POST, endpoint);

            System.out.println("STATUS CODE: " + response.getStatusCode());
            System.out.println("RESPONSE BODY:");
            System.out.println(response.asPrettyString());
        }
    }

    @When("I send a POST request for auth")
    public void postRequest() {
        System.out.println("FINAL AUTH REQUEST BODY SENT: " + requestBody);

        response = request.body(requestBody).request(Method.POST, endpoint);

        System.out.println("STATUS CODE: " + response.getStatusCode());
        System.out.println("STATUS LINE: " + response.getStatusLine());
        System.out.println("RESPONSE BODY:");
        System.out.println(response.asPrettyString());
    }

    @When("I send a GET request for auth")
    public void getRequest() {
        response = request.request(Method.GET, endpoint);

        System.out.println("STATUS CODE: " + response.getStatusCode());
        System.out.println("STATUS LINE: " + response.getStatusLine());
        System.out.println("RESPONSE BODY:");
        System.out.println(response.asPrettyString());
    }

    @Then("the API should return status code {int} with status message {string} for auth")
    public void validateStatus(Integer code, String msg) {
        assertEquals(response.getStatusCode(), code);
        assertTrue(response.getStatusLine().contains(msg));
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

    @Given("I set invalid API key in header for auth")
    public void invalidKey() {
        request.header("x-api-key", "invalid_key");
        System.out.println("INVALID API KEY SET FOR AUTH");
    }

    @Then("the API should return status code should be handled as per API behavior for auth")
    public void invalidKeyValidation() {
        int status = response.getStatusCode();
        assertTrue(status == 400 || status == 401 || status == 403);
    }
}