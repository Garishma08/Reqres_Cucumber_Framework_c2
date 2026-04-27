@record_module
Feature: Record Module

  Background:
    Given the API is initialized

  Scenario: TC_Record_01 Create Record
    Given I set create record endpoint from excel sheet "data" row <row>
    And I prepare record request body from excel sheet "data" row <row>
    When I send a POST request for record
    Then the API should return status code 201 for record
    And the response body should be valid JSON for record
    And the response should contain field "id"
    And the response should contain field "collection_id"
    Examples:
    | row |
    | 1   |
    | 2   |

  Scenario: TC_Record_02 Create Record without data
    Given I set create record endpoint
    And I prepare empty record request body
    When I send a POST request for record
    Then the API should return status code 400 for record
    And the response should contain error and message

  Scenario: TC_Record_03 Create Record without json
    Given I set create record endpoint
    And I send raw empty string body
    When I send a POST request for record
    Then the API should return status code 400 for record
    And the response should contain error and message

	Scenario: TC_Record_04 Create Record without slug
	  Given I set create record endpoint with invalid slug
	  And I prepare record request body from excel sheet "data" row <row>
	  When I send a POST request for record
	  Then the API should return status code 404 for record
	  And the response should contain error and message
	
	Examples:
	  | row |
	  | 1   |
	  | 2   |

  Scenario: TC_Record_1B Create record using datatable
    Given I set create record endpoint
    When I send POST request for record using below data
      | name    | value |
      | Steven  | 100   |
      | Aiden   | 200   |
    Then the API should return status code 201 for record

  Scenario: TC_Record_05 Get All Records
    Given I set get records endpoint
    When I send a GET request for records
    Then the API should return status code 200 for record
    And the response should contain a non-empty data array

  Scenario: TC_Record_06 Get All Records without Slug
    Given I set get records endpoint with missing slug
    When I send a GET request for records
    Then the API should return status code 404 for record
    And the response should contain error and message

  Scenario: TC_Record_07 Get All Records invalid Slug
    Given I set get records endpoint with invalid slug
    When I send a GET request for records
    Then the API should return status code 404 for record
    And the response should contain error and message

  Scenario: TC_Record_08 Get a single Record
    Given I create a record and store its id
    And I set get record endpoint with valid id
    When I send a GET request for record
    Then the API should return status code 200 for record
    And the response should contain field "id"
    And the response should contain field "collection_id"

  Scenario: TC_Record_09 Get a single Record without collection
    Given I create a record and store its id
    And I set get record endpoint with invalid slug
    When I send a GET request for record
    Then the API should return status code 404 for record
    And the response should contain error and message

  Scenario: TC_Record_10 Get a single Record without ID
    Given I set get record endpoint without id
    When I send a GET request for record
    Then the API should return status code 404 for record
    And the response should contain error and message

  Scenario: TC_Record_11 Get a single Record invalid ID
    Given I set get record endpoint with invalid id
    When I send a GET request for record
    Then the API should return status code 404 for record
    And the response should contain error and message

  Scenario: TC_Record_12 Update Record
    Given I create a record and store its id
    And I set update record endpoint with valid id
    And I prepare update record request body from excel sheet "data" row <row>
    When I send a PUT request for record
    Then the API should return status code 200 for record
    And the response should contain field "updated_at"
    Examples:
    | row |
    | 1   |
    | 2   |

  Scenario: TC_Record_13 Update Record without ID
    Given I set update record endpoint without id
    And I prepare update record request body
    When I send a PUT request for record
    Then the API should return status code 404 for record
    And the response should contain error and message

  Scenario: TC_Record_14 Update Record invalid Collection
    Given I set update record endpoint with invalid slug
    And I prepare update record request body
    When I send a PUT request for record
    Then the API should return status code 404 for record
    And the response should contain error and message

  Scenario: TC_Record_15 Delete a Record
    Given I create a record and store its id
    And I set delete record endpoint with valid id
    When I send a DELETE request for record
    Then the API should return status code 204 for record

  Scenario: TC_Record_16 Delete a Record without id
    Given I set delete record endpoint without id
    When I send a DELETE request for record
    Then the API should return status code 404 for record
    And the response should contain error and message

  Scenario: TC_Record_17 Delete a Record invalid id
    Given I set delete record endpoint with invalid id
    When I send a DELETE request for record
    Then the API should return status code 404 for record
    And the response should contain error and message

Scenario: Delete collection successfully
  Given I set delete collection endpoint from excel sheet "data" row <row>
  When I send a DELETE request for collection
  Then the API should return status code 204 for collection
  Examples:
    | row |
    | 1   |
    | 2   |
