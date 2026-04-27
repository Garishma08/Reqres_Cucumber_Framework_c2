@collections
Feature: Collection API Testing

  Background:
    Given Base Url is set

  @positive
  Scenario: Get all collections successfully
    When I get collection using "authentication"
    Then I validate status code 200
    And I validate response time
    And I validate content type

  @negative
  Scenario: Get all collections without authentication
    When I get collection using "without_auth"
    Then I validate status code 401
    And I validate response time

  @positive @excel
  Scenario Outline: Create collection using Excel data
    Given I prepare collection payload from excel "<row>"
    When I send a POST request for collection
    Then I validate status code 201
    And I validate response time
    And I save collection slug

    Examples:
      | row |
      | 1   |
      | 2   |

  @negative
  Scenario: Create duplicate collection
    Given I prepare duplicate collection payload
    When I send a POST request for collection
    Then I validate status code 409
    And I validate response time

  @negative
  Scenario: Create collection with missing fields
    Given I prepare collection payload with missing fields
    When I send a POST request for collection
    Then I validate status code 400
    And I validate response time

  @negative
  Scenario Outline: Create collection with invalid datatype
    Given I prepare invalid datatype payload with data
      | name   | slug               | project_id | visibility |
      | <name> | invalid-slug-dtype | 12631      | private    |
    When I send a POST request for collection
    Then I validate status code 400
    And I validate response time

    Examples:
      | name |
      | null |

  @positive
  Scenario: Get collection using saved slug
    Given I use saved collection slug
    When I send GET request for collection
    Then I validate status code 200
    And I validate response time

  @negative
  Scenario: Get collection with invalid slug
    Given I set invalid collection slug "invalid123"
    When I send GET request for collection
    Then I validate status code 404
    And I validate response time

  @negative
  Scenario: Get collection without authentication
    When I get collection without authentication using slug "orders"
    Then I validate status code 401
    And I validate response time

  @negative
  Scenario: Update collection with invalid slug
    When I update collection using invalid slug "wrongslug"
    Then I validate status code 404
    And I validate response time

  @negative
  Scenario: Update collection without authentication
    When I update collection without authentication using slug "orders"
    Then I validate status code 401
    And I validate response time