@users_create_get
Feature: User Create and Retrieve Management in ReqRes API

  Background:
    Given the user API is initialized

    @positive @excel @TC_Users_Excel
Scenario Outline: Create user using Excel data
  Given I set user endpoint for create user
  And I prepare create user request body from excel sheet "data" row <row>
  When I send a POST request for user
  Then the API should return status code 201 with status message "Created" for user
  And the response body should be valid JSON for user
  And the response should contain field "id" for user
  And the response should contain field "name" for user
  And the response should contain field "job" for user
  And the user response should match excel sheet "data" row <row>

Examples:
  | row |
  | 1   |
  | 2   |
  | 3   |

@datatable @create @TC_Users_15
  Scenario: TC_Users_15 - Create multiple users using DataTable
    Given I set user endpoint for create user
    When I send POST request to create users using below data
      | name     | job               |
      | Garishma | QA Engineer       |
      | Priya    | Automation Tester |
      | Rahul    | Developer         |
      | Anu      | DevOps Engineer   |
      | Kiran    | Data Analyst      |
    Then the API should return status code 201 with status message "Created" for user
    And the response time should be less than 2000 ms for user
    And the response body should be valid JSON for user
    And the response should contain field "id" for user
    And the response should contain field "name" for user
    And the response should contain field "job" for user
    And the response should contain field "createdAt" for user
    And validate response headers for user

  @positive 
  Scenario: TC_Users_15 - Create user with valid request body
    Given I set user endpoint for create user
    And I prepare valid user request body
    When I send a POST request for user
    Then the API should return status code 201 with status message "Created" for user
    And the response time should be less than 2000 ms for user
    And the response body should be valid JSON for user
    And the response should contain field "id" for user
    And the response should contain field "name" for user
    And the response should contain field "job" for user
    And the response should contain field "createdAt" for user
    And validate response headers for user

  @negative 
  Scenario: TC_Users_16 - Create user with empty request body
    Given I set user endpoint for create user
    And I prepare empty user request body
    When I send a POST request for user
    Then the API should return status code should be handled as per API behavior for user
    And the response time should be less than 2000 ms for user
    And the response body should be valid JSON for user
    And validate response headers for user

  @negative @create @TC_Users_17
  Scenario: TC_Users_17 - Create user with invalid API key returns 403
    Given I set user endpoint for create user
    And I prepare valid user request body
    And I set invalid API key in header
    When I send a POST request for user
    Then the API should return status code 403 with status message "Forbidden" for user
    And the response time should be less than 2000 ms for user
    And the response body should be valid JSON for user
    And validate response headers for user

  @negative 
  Scenario: TC_Users_18 - Create user with missing required field
    Given I set user endpoint for create user
    And I prepare user request body with only name "Garishma"
    When I send a POST request for user
    Then the API should return status code should be 404 for user
    And the response time should be less than 2000 ms for user
    And the response body should be valid JSON for user
    And validate response headers for user

  @positive 
  Scenario: TC_Users_19 - Get all users successfully
    Given I set users list endpoint
    When I send a GET request for user
    Then the API should return status code 200 with status message "OK" for user
    And the response time should be less than 2000 ms for user
    And the response body should be valid JSON for user
    And the response should contain field "data" for user
    And the response should contain field "page" for user
    And the response should contain field "per_page" for user
    And the response should contain field "total" for user
    And the response should contain field "total_pages" for user
    And validate response headers for user

  @negative
  Scenario: TC_Users_20 - Get users with invalid URL returns 404
    Given I set user endpoint with invalid id
    When I send a GET request for user
    Then the API should return status code 404 with status message "Not Found" for user
    And the response time should be less than 2000 ms for user
    And the response body should be valid JSON for user
    And validate response headers for user

  @negative 
  Scenario: TC_Users_21 - Get users without API key - ReqRes allows unauthenticated GET
    Given I set users list endpoint
    And I remove API key from header
    When I send a GET request for user
    Then the API should return status code should be 401 Unauthorized for user
    And the response time should be less than 2000 ms for user
    And the response body should be valid JSON for user
    And validate response headers for user

  @positive 
  Scenario Outline: TC_Users_22 - Get users list with page "<page>"
    Given I set users list endpoint with page "<page>"
    When I send a GET request for user
    Then the API should return status code 200 with status message "OK" for user
    And the response time should be less than 2000 ms for user
    And the response body should be valid JSON for user
    And the response should contain field "data" for user
    And the response should contain field "page" for user
    And the response should contain field "per_page" for user
    And the response should contain field "total" for user
    And the response should contain field "total_pages" for user
    And validate response headers for user

    Examples:
      | page |
      | 1    |
      | 2    |

  @positive
  Scenario: TC_Users_24 - Get user by valid ID
    Given I set user endpoint with valid id
    When I send a GET request for user
    Then the API should return status code 200 with status message "OK" for user
    And the response time should be less than 2000 ms for user
    And the response body should be valid JSON for user
    And the response should contain field "data" for user
    And the response should contain field "data.id" for user
    And the response should contain field "data.email" for user
    And the response should contain field "data.first_name" for user
    And the response should contain field "data.last_name" for user
    And the response should contain field "data.avatar" for user
    And validate response headers for user

  @negative 
  Scenario: TC_Users_25 - Get user by invalid ID returns 404
    Given I set user endpoint with user id "999"
    When I send a GET request for user
    Then the API should return status code 404 with status message "Not Found" for user
    And the response time should be less than 2000 ms for user
    And the response body should be valid JSON for user
    And validate response headers for user

  @negative 
  Scenario: TC_Users_26 - Get user by ID with invalid API key - ReqRes allows unauthenticated GET
    Given I set user endpoint with valid id
    And I set invalid API key in header
    When I send a GET request for user
    Then the API should return status code should be handled as per API behavior for user
    And the response time should be less than 2000 ms for user
    And the response body should be valid JSON for user
    And validate response headers for user

  @negative 
  Scenario: TC_Users_27 - Get user with string type ID returns 404
    Given I set user endpoint with user id "abc"
    When I send a GET request for user
    Then the API should return status code 404 with status message "Not Found" for user
    And the response time should be less than 2000 ms for user
    And the response body should be valid JSON for user
    And validate response headers for user

  @scenariooutline 
  Scenario Outline: TC_Users_SO - Get user by user ID "<userId>"
    Given I set user endpoint with user id "<userId>"
    When I send a GET request for user
    Then the API should return status code <statusCode> with status message "<statusMessage>" for user
    And the response time should be less than 2000 ms for user
    And the response body should be valid JSON for user
    And validate response headers for user

    Examples:
      | userId | statusCode | statusMessage |
      | 1      | 200        | OK            |
      | 2      | 200        | OK            |
      | 3      | 200        | OK            |
      | 999    | 404        | Not Found     |
      | abc    | 404        | Not Found     |
