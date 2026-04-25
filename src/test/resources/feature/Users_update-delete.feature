@users_update_delete
Feature: User Update and Delete Management in ReqRes API

  Background:
    Given the update and delete user API is initialized


  # ================= UPDATE (PUT - EXCEL) =================

  @positive @excel @update @TC_Users_31
  Scenario Outline: TC_Users_31 - Update user using Excel data
    Given I set update user endpoint from excel sheet "data" row <row>
    And I prepare update user request body from excel sheet "data" row <row>
    When I send a PUT request for update user
    Then the API should return status code 200 for update user
    And the response body should be valid JSON for update user
    And the response should contain field "name" for update user
    And the response should contain field "job" for update user
    And the response should contain field "updatedAt" for update user
    And validate response headers for update user

    Examples:
      | row |
      | 1   |
      | 2   |
      | 3   |


  # ================= UPDATE INVALID ID =================

  @negative @update @TC_Users_32
  Scenario: TC_Users_32 - Update user with invalid ID using Excel

    Given I set update user endpoint with user id from excel sheet "data" row 1
    And I prepare update user request body from excel sheet "data" row 1
    When I send a PUT request for update user
    Then the API should return status code 404 for update user


  # ================= EMPTY BODY =================

  @negative @update @TC_Users_33
  Scenario: TC_Users_33 - Update user with empty body
    Given I set update user endpoint with valid id
    And I prepare empty update user request body
    When I send a PUT request for update user
    Then the API should return status code 200 for update user


  # ================= DATATABLE (PUT) =================

  @datatable @update @TC_Users_34
  Scenario: TC_Users_34 - Update users with invalid API key
    Given I set update user endpoint
    And I set invalid API key
    When I send PUT request for update user using below data
      | userId | name     | job                |
      | 2      | Kavya    | QA Engineer        |
      | 2      | Anu      | Developer          |
    Then the API should return status code 403 for update user


  # ================= DUPLICATE FIELDS =================

  @negative @update @TC_Users_35
  Scenario: TC_Users_35 - Update user with duplicate fields
    Given I set update user endpoint with valid id
    And I prepare duplicate fields request body
    When I send a PUT request for update user
    Then the API should return status code 400 for update user


  # ================= PATCH =================

  @positive @patch @TC_Users_36
  Scenario: TC_Users_36 - Partial update user
    Given I set update user endpoint with valid id
    And I prepare partial update user request body
    When I send a PATCH request for update user
    Then the API should return status code 200 for update user
    And the response should contain field "updatedAt"


  # ================= PATCH SCENARIO OUTLINE =================

  @scenariooutline @patch @TC_Users_37
  Scenario Outline: TC_Users_37 - Partial update with missing fields
    Given I set update user endpoint with user id "<userId>"
    And I prepare partial update request body with name "<name>"
    When I send a PATCH request for update user
    Then the API should return status code 200 for update user

    Examples:
      | userId | name      |
      | 2      | Garishma  |
      | 2      | Kavya     |


  # ================= PATCH WITHOUT ID =================

  @negative @patch @TC_Users_38
  Scenario: TC_Users_38 - Partial update without user ID
    Given I set update user endpoint without id
    And I prepare partial update request body
    When I send a PATCH request for update user
    Then the API should return status code 400 for update user


  # ================= DELETE =================

  @positive @delete @TC_Users_39
  Scenario: TC_Users_39 - Delete user with valid ID
    Given I set delete user endpoint with valid id
    When I send a DELETE request for delete user
    Then the API should return status code 204 for delete user


  # ================= DELETE SCENARIO OUTLINE =================

  @negative @delete @TC_Users_40
  Scenario: TC_Users_40 - Delete user with invalid ID

    Given I set delete user endpoint with user id "999"
    When I send a DELETE request for delete user
    Then the API should return status code 404 for delete user


  # ================= DELETE WITHOUT ID =================

  @negative @delete @TC_Users_41
  Scenario: TC_Users_41 - Delete user without ID
    Given I set delete user endpoint without id
    When I send a DELETE request for delete user
    Then the API should return status code 204 for delete user


  # ================= DELETE INVALID API KEY =================

  @negative @delete @TC_Users_42
  Scenario: TC_Users_42 - Delete user with invalid API key
    Given I set delete user endpoint with valid id
    And I set invalid API key
    When I send a DELETE request for delete user
    Then the API should return status code 403 for delete user