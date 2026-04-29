# ReqRes API Testing Framework

A comprehensive Behavior-Driven Development (BDD) framework for testing the ReqRes API using Cucumber, REST Assured, and TestNG.

## Overview

This project provides automated API testing for the [ReqRes](https://reqres.in/) API, a online REST API for testing and prototyping. The framework uses Cucumber for BDD scenarios, REST Assured for API interactions, TestNG for test execution, and ExtentReports for detailed reporting.

## Features

- **BDD Testing**: Cucumber-based scenarios for clear, readable test cases
- **API Testing**: REST Assured for comprehensive HTTP request/response handling
- **Data-Driven Testing**: Support for Excel data sources and DataTables
- **Reporting**: ExtentReports integration for detailed HTML reports
- **Modular Architecture**: Organized structure with base classes, utilities, and step definitions
- **Configuration Management**: Externalized configuration via properties files

## Project Structure

```
src/
├── main/java/
│   ├── base/
│   │   └── Baseclass.java          # Base class with REST Assured setup
│   ├── endpoints/
│   │   └── Endpoints.java          # API endpoint constants
│   └── utils/
│       ├── ConfigReader.java       # Configuration management
│       └── ExcelUtility.java       # Excel data reading utility
└── test/
    ├── java/
    │   ├── hooks/
    │   │   └── Hooks.java          # Cucumber hooks for setup/teardown
    │   ├── stepdefinition/
    │   │   ├── AuthSteps.java
    │   │   ├── CollectionStep.java
    │   │   ├── RecordStepDefinition.java
    │   │   ├── Users_createget_stepdefinition.java
    │   │   └── Users_updatedelete_stepdefinition.java
    │   └── testRunner/
    │       └── TestRunner.java      # TestNG Cucumber runner
    └── resources/
        ├── config.properties        # Configuration properties
        ├── extent-config.xml        # ExtentReports configuration
        ├── extent.properties        # ExtentReports properties
        ├── feature/                 # Cucumber feature files
        │   ├── auth.feature
        │   ├── collectionsfile.feature
        │   ├── record_feature.feature
        │   ├── Users_create-get.feature
        │   └── Users_update-delete.feature
        └── testdata/
            └── TestData.xlsx        # Test data Excel file
```

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- IDE (Eclipse, IntelliJ IDEA, or VS Code) with Java support

## Installation

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd Reqres_Cucumber_Framework_c2
   ```

2. Install dependencies:
   ```bash
   mvn clean install
   ```

## Configuration

Update the configuration in `src/test/resources/config.properties`:

```properties
base_url=https://reqres.in
api_key=your_api_key_here
invalid_api_key=invalid_key_for_testing
user_id=2
project_id=12878
excel_path=src/test/resources/testdata/TestData.xlsx
```

## Test Data

Test data is stored in `src/test/resources/testdata/TestData.xlsx`. The Excel file should have:
- Sheet named "data" with columns for test data
- Header row with column names
- Data rows referenced in feature files

## Running Tests

### Run All Tests
```bash
mvn test -Dtest=TestRunner
```

### Run Specific Feature
```bash
mvn test -Dcucumber.filter.tags="@users_create_get"
```

### Run with Specific Tags
```bash
mvn test -Dcucumber.filter.tags="@positive"
```

## Test Scenarios

The framework covers the following API operations:

### User Management
- Create users (with Excel data and DataTables)
- Retrieve users (single user, user list, paginated)
- Update users (PUT and PATCH)
- Delete users

### Authentication
- User registration
- User login

### Collections & Records
- Create collections
- Get collections
- Update collections
- Delete collections
- CRUD operations on records within collections

### Other Features
- List colors/resources
- Error handling scenarios

## Reporting

After test execution, reports are generated in:

- **Cucumber HTML Report**: `target/cucumber-report.html`
- **Cucumber JSON Report**: `target/cucumber.json`
- **ExtentReports**: `target/ExtentReports/extent-report.html`
- **TestNG Reports**: `target/surefire-reports/` and `test-output/`

## Key Components

### Baseclass
- Sets up REST Assured configuration
- Provides common request/response objects
- Handles base URI and headers

### Endpoints
- Centralized endpoint definitions
- Supports parameterized URLs

### Utilities
- **ConfigReader**: Loads configuration from properties file
- **ExcelUtility**: Reads test data from Excel files

### Step Definitions
- Implements Cucumber steps
- Uses REST Assured for API calls
- Includes assertions and validations

### Hooks
- Initializes configuration before each scenario
- Can be extended for additional setup/teardown logic

## Writing New Tests

1. **Add Feature File**: Create `.feature` file in `src/test/resources/feature/`
2. **Implement Steps**: Add step definitions in appropriate class under `stepdefinition/`
3. **Update Endpoints**: Add new endpoints in `Endpoints.java` if needed
4. **Add Test Data**: Update Excel file or use DataTables in features

## Example Feature

```gherkin
@users_create_get
Feature: User Create and Retrieve Management in ReqRes API

  Background:
    Given the user API is initialized

  @positive @excel
  Scenario Outline: Create user using Excel data
    Given I set user endpoint for create user
    And I prepare create user request body from excel sheet "data" row <row>
    When I send a POST request for user
    Then the API should return status code 201 with status message "Created"
    And the response should contain field "id"
    And the user response should match excel sheet "data" row <row>

  Examples:
    | row |
    | 1   |
    | 2   |
```

## Dependencies

- **Cucumber**: BDD framework
- **REST Assured**: API testing library
- **TestNG**: Test framework
- **Jackson**: JSON processing
- **Apache POI**: Excel file handling
- **ExtentReports**: Test reporting

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request
