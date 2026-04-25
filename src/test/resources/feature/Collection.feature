@collections
Feature: Collection API Testing

  Background:
    Given Base Url is set

  # =========================================================
  @positive
  Scenario: Get collection successfully
    When I get collection using "authentication"
    Then I validate status code 200
    And I validate response time
    And I validate content type
    And I validate success response

  # =========================================================
  @negative
  Scenario: Get collection unauthorized
    When I get collection using "without_auth"
    Then I validate status code 401
    And I validate response time
    And I validate error response

  # =========================================================
  @positive @excel @dependency
  Scenario Outline: Create collection using Excel data
    Given I prepare collection payload from excel "<rowNumber>"
    When I send a POST request for collection
    Then I validate status code 201
    And validate collection fields from excel "<rowNumber>"
    And I save collection slug
    And I validate response time

    Examples:
      | rowNumber |
      | 1         |
      | 2         |

  # =========================================================
  @negative
  Scenario: Duplicate collection creation
    Given I prepare duplicate collection payload
    When I send a POST request for collection
    Then I validate status code 409
    And I validate response time
    And I validate error response

  # =========================================================
  @negative
  Scenario: Create collection Missing fields
    Given I prepare collection payload with missing fields
    When I send a POST request for collection
    Then I validate status code 400
    And I validate response time
    And I validate error response

  # =========================================================
  @negative
  Scenario: Invalid datatype
    Given I prepare invalid datatype payload
    When I send a POST request for collection
    Then I validate status code 400
    And I validate response time
    And I validate error response

  # =========================================================
  @positive @dependency
  Scenario: Get collection using saved slug
    Given I use saved collection slug
    When I send GET request for collection
    Then I validate status code 200
    And I validate response time
    And I validate success response

  # =========================================================
  @negative
  Scenario: Get collection with invalid slug
    Given I have invalid collection slug:
      | slug       |
      | invalid123 |
    Then I validate status code 404
    And I validate response time
    And I validate error response

  # =========================================================
  @negative
  Scenario: Get collection without authentication
    When I get collection without authentication using slug "orders"
    Then I validate status code 401
    And I validate response time
    And I validate error response

  # =========================================================
  @positive @dependency @excel
  Scenario Outline: Update collection name using Excel data
    Given I use saved collection slug
    And I read collection data from Excel row "<rowNumber>"
    When I send PUT request for collection
    Then I validate status code 200
    And I validate response time
    And I validate success response

    Examples:
      | rowNumber |
      | 1         |
      | 2         |

  # =========================================================
  @negative
  Scenario: Update collection with invalid slug
    When I update collection using invalid slug "wrongslug"
    Then I validate status code 404
    And I validate response time
    And I validate error response

  # =========================================================
  @negative
  Scenario: Update collection without authentication
    When I update collection without authentication using slug "orders"
    Then I validate status code 401
    And I validate response time
    And I validate error response