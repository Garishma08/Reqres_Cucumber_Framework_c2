
package base;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.ConfigReader;

public class Baseclass {

    protected static RequestSpecification request;
    protected static Response response;
    protected String endpoint;

    public static void setup() {
        RestAssured.baseURI = ConfigReader.getBaseUrl();

        request = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .header("x-api-key", ConfigReader.getApiKey());
               
    }
}