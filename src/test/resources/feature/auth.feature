@auth_create_get
Feature: Auth Register and Login Management in ReqRes API

  Background:
    Given the Auth API is initialized

  @positive @excel @auth @TC_Auth_Excel
  Scenario Outline: Register user using Excel data
    Given I set auth endpoint for register
    And I prepare auth request body from excel sheet "data" row <row>
    When I send a POST request for auth
    Then the API should return status code 200 with status message "OK" for auth
    And the response body should be valid JSON for auth
    And the response should contain field "id" for auth
    And the response should contain field "token" for auth
    And the auth response should match excel sheet "data" row <row>

  Examples:
    | row |
    | 1   |
    | 2   |
    | 3   |

  @datatable @auth @TC_Auth_01
  Scenario: TC_Auth_01 - Register multiple users using DataTable
    Given I set auth endpoint for register
    When I send POST request for auth using below data
      | email                   | password |
      | eve.holt@reqres.in      | pistol   |
      | george.bluth@reqres.in  | pistol   |
      | janet.weaver@reqres.in  | pistol   |
      | emma.wong@reqres.in     | pistol   |
    Then the API should return status code 200 with status message "OK" for auth
    And the response time should be less than 2000 ms for auth
    And the response body should be valid JSON for auth
    And the response should contain field "id" for auth
    And the response should contain field "token" for auth
    And validate response headers for auth

  @positive @auth
  Scenario: TC_Auth_02 - Register with valid request body
    Given I set auth endpoint for register
    And I prepare valid auth request body
    When I send a POST request for auth
    Then the API should return status code 200 with status message "OK" for auth
    And the response time should be less than 2000 ms for auth
    And the response body should be valid JSON for auth
    And the response should contain field "id" for auth
    And the response should contain field "token" for auth
    And validate response headers for auth

  @negative @auth
  Scenario: TC_Auth_03 - Register without password
    Given I set auth endpoint for register
    And I prepare auth request body without password
    When I send a POST request for auth
    Then the API should return status code 400 with status message "Bad Request" for auth
    And the response time should be less than 2000 ms for auth
    And the response body should be valid JSON for auth
    And the response should contain field "error" for auth
    And validate response headers for auth

  @negative @auth
  Scenario: TC_Auth_04 - Register without email
    Given I set auth endpoint for register
    And I prepare auth request body without email
    When I send a POST request for auth
    Then the API should return status code 400 with status message "Bad Request" for auth
    And the response time should be less than 2000 ms for auth
    And the response body should be valid JSON for auth
    And the response should contain field "error" for auth
    And validate response headers for auth

  @negative @auth @create
  Scenario: TC_Auth_05 - Register with invalid API key
    Given I set auth endpoint for register
    And I prepare valid auth request body
    And I set invalid API key in header for auth
    When I send a POST request for auth
    Then the API should return status code should be handled as per API behavior for auth
    And the response time should be less than 2000 ms for auth
    And the response body should be valid JSON for auth
    And validate response headers for auth

  @positive @auth
  Scenario: TC_Auth_06 - Get list color valid credentials
    Given I set auth endpoint for list colors
    When I send a GET request for auth
    Then the API should return status code 200 with status message "OK" for auth
    And the response time should be less than 2000 ms for auth
    And the response body should be valid JSON for auth
    And validate response headers for auth

  @positive @auth
  Scenario: TC_Auth_07 - Pagination check
    Given I set auth endpoint for list colors with page "2"
    When I send a GET request for auth
    Then the API should return status code 200 with status message "OK" for auth
    And the response should contain field "page" for auth
    And the response should contain field "total_pages" for auth
    And the response time should be less than 2000 ms for auth
    And validate response headers for auth

  @positive @auth
  Scenario: TC_Auth_08 - Response fields validation
    Given I set auth endpoint for list colors with page "2"
    When I send a GET request for auth
    Then the API should return status code 200 with status message "OK" for auth
    And the response body should be valid JSON for auth
    And validate response headers for auth

  @positive @auth
  Scenario: TC_Auth_09 - Invalid page parameter handled
    Given I set auth endpoint for list colors with page "abc123"
    When I send a GET request for auth
    Then the API should return status code 200 with status message "OK" for auth
    And the response time should be less than 2000 ms for auth
    And validate response headers for auth

  @positive @auth
  Scenario: TC_Auth_10 - Valid login
    Given I set auth endpoint for login
    And I prepare valid login request body
    When I send a POST request for auth
    Then the API should return status code 200 with status message "OK" for auth
    And the response should contain field "token" for auth
    And the response time should be less than 2000 ms for auth
    And validate response headers for auth

  @negative @auth
  Scenario: TC_Auth_11 - Login with missing password
    Given I set auth endpoint for login
    And I prepare login request body without password
    When I send a POST request for auth
    Then the API should return status code 400 with status message "Bad Request" for auth
    And the response should contain field "error" for auth
    And the response time should be less than 2000 ms for auth
    And validate response headers for auth

  @negative @auth
  Scenario: TC_Auth_12 - Login with missing email
    Given I set auth endpoint for login
    And I prepare login request body without email
    When I send a POST request for auth
    Then the API should return status code 400 with status message "Bad Request" for auth
    And the response should contain field "error" for auth
    And the response time should be less than 2000 ms for auth
    And validate response headers for auth

  @negative @auth
  Scenario: TC_Auth_13 - Login with invalid email
    Given I set auth endpoint for login
    And I prepare login request body with invalid email
    When I send a POST request for auth
    Then the API should return status code 400 with status message "Bad Request" for auth
    And the response should contain field "error" for auth
    And the response time should be less than 2000 ms for auth
    And validate response headers for auth

  @negative @auth
  Scenario: TC_Auth_14 - Login with invalid API key
    Given I set auth endpoint for login
    And I prepare valid login request body
    And I set invalid API key in header for auth
    When I send a POST request for auth
    Then the API should return status code should be handled as per API behavior for auth
    And the response should contain field "error" for auth
    And the response time should be less than 2000 ms for auth
    And validate response headers for auth



  @auth @extra
  Scenario Outline: Extra Register Validation
    Given I set auth endpoint for register
    And I prepare login request body with "<email>" and "<password>"
    When I send a POST request for auth
    Then the API should return status code 200 with status message "OK" for auth
    And the response should contain field "token" for auth
    And the response time should be less than 2000 ms for auth

  Examples:
    | email              | password |
    | eve.holt@reqres.in | pistol   |

  @auth @extra
  Scenario Outline: Extra Login Validation
    Given I set auth endpoint for login
    And I prepare login request body with "<email>" and "<password>"
    When I send a POST request for auth
    Then the API should return status code 200 with status message "OK" for auth
    And the response should contain field "token" for auth

  Examples:
    | email              | password    |
    | eve.holt@reqres.in | cityslicka |

  @auth @extra
  Scenario Outline: Extra Negative Register Cases
    Given I set auth endpoint for register
    And I prepare auth request body with "<condition>"
    When I send a POST request for auth
    Then the API should return status code 400 with status message "Bad Request" for auth

  Examples:
    | condition   |
    | no_password |
    | no_email    |