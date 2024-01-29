**Rest Assured + TestNG + Extent Report for API Automation**

**Preferred Java Version : JDK-11**

**Libraries Used**:
1. [Rest Assured for Api automation](https://github.com/rest-assured/rest-assured)
2. [Maven as build tool](https://maven.apache.org/guides/)
3. [TestNG as testing framework](https://testng.org/doc/)
4. [Extent for reporting](https://www.extentreports.com/docs/versions/4/java/index.html)
5. [Java Faker for random data](https://github.com/DiUS/java-faker)
6. [AssertJ Core for assert the test](https://assertj.github.io/doc/#assertj-core)
7. [Apache POI for reading Excel for test data](https://poi.apache.org/)


# Framework Structure and Rules

## Package Structure
- **src/test/java:** Write regression suite and automation test cases.
- **src/main/java:** Keep non-testing content (e.g., utility classes, reporting, listening).
- Utilize a `testng.xml` file for running a suite of tests.

## Code Conventions
- Follow access level order: `private > protected > default > public`.
- Perform null checks on variables before operations.
- Prefer `ENUM` for predefined sets of values.

## API Testing Framework Pattern (AAA Pattern)
The AAA pattern (Arrange, Act, Assert) is a method for organizing tests to improve readability, maintainability, and clarity.

1. **Arrange:** Establish necessary preconditions and inputs for the test, such as base URL, endpoints, headers, and payload.
2. **Act:** Represent the action or operation being tested, such as POST, GET, PUT, PATCH, and DELETE calls.
3. **Assert:** Focus on verifying expected outcomes or assertions. Validate results, including request body and status code.

## Listeners Notes - Most Frequently Used Listeners
- Avoid tight coupling between test report setup/teardown activities and test methods.
- Use listeners for report setup/teardown to keep test classes focused on testing.
- Customize test case execution at runtime using `IMethodInterceptor` and `AnnotationTransformer` listeners.

### IMethodInterceptor
- Customize test case execution at RUNTIME.
- Provide methods to customize the list of methods to be executed.
- Enables dynamic control over test case execution.

### AnnotationTransformer
- Controls annotations for referred test methods.
- `transform()` method is called for each test method.
- Useful for modifying annotations at runtime.

### RetryAnalyzer
- Controls annotations for referred test methods.
- `retry()` method is called when a test fails.
- If `retry()` returns TRUE, the failed test is re-run.

## Request Call Notes
### Authorization Manager
This class manages authorization for the application using various authentication methods. It provides methods to retrieve authentication-related properties and obtain access tokens.

### RequestValidation
This class is for status code validation and captures the response time. If the status code matches with predefined status code, it returns true; otherwise, it returns false.

### RequestBuilder
Builds and executes the HTTP request based on provided parameters.
- `authType`: The authentication type for the request.
- `requestType`: The HTTP request type (GET, POST, PUT, etc.).
- `endPoint`: The endpoint of the API.
- `endpointName`: The name of the API endpoint.
- `responseBodyVerification`: Map containing expected response body values for verification.
- `responseBodyType`: The expected response body type (JSON, XML, plain, etc.).
- `bodyPayload`: The payload for the request body.
- `needResponseBody`: Flag indicating whether the response body is needed.

This method returns the `ApiResponse` method containing the verification status and response.

### Assertion Utils
- Performs JSON response body verification for a given REST API response.
- Uses Rest Assured library.
- Logs results and returns a boolean indicating assertion success.

## Utils Notes
### Data Provider Utils
- Holds data provider for all test cases.
- Acts as a data provider for each test case.
- Provides information about test cases at runtime.

### Data and Random Utils
- Generates test data using the Java Faker library.
- Generates random strings.

### Excel Utils
- Reads from or writes to Excel.
- Encapsulates values from a specified Excel sheet.
- Returns a List containing Hashmap.

### File Read Utils
- Reads contents of a payload file based on the provided location.
- Designed for various file types (JSON, XML, HTML, Image, Docs).
- Includes error handling for file reading.

### Rest Utils
- Extracts values from the response using Rest Assured.

### Json Utils
- Reads Excel data and converts it into a JSON file.
- Uses Apache POI for Excel handling and Jackson library for JSON processing.
- Returns data as a list of maps.

### Property Utils
- Reads the property file and stores it in a HashMap.
- Implements Eager Initialization using a static block.

### Decode Utils
- Assists in decoding base64-encoded strings.
- Accepts a base64-encoded string and returns the decoded result.

## Miscellaneous Notes
### Try with Resources Concept
- Utilize Try With Resources to automatically close streams (no need for a finally block).
- Applicable only for classes implementing the AutoCloseable interface (e.g., FileInputStream).
- Allows multiple streams within the Try With Resources block.

### FrameworkConstants
- `FrameworkConstants` hold all the constant values used within the framework.

### Enums
- Enums restrict the values used in Property files. Null pointer exceptions can be avoided because of typos.
- Whenever a new value is added to the property file, a corresponding enum should be created here.

### Reports
- Implement a class using ThreadLocal to address thread safety concerns.
- Utilize these methods in your test to display reports after running the test.
- Restrict access to specific methods (`pass()`, `fail()`, `skip()`, and others) to prevent unauthorized usage by team members.

## Test Case Creation
**Class Definition:**
```java
public class TestClassName extends RequestBuilder {}
```
    This line defines a class named `TestClassName` that extends another class named `RequestBuilder`, indicating that `TestClassName` inherits functionality from `RequestBuilder`.

**Test Method:**
```java
@Test
public void testCaseName(Map<String, String> data) {}
```
    This is a TestNG test method annotated with `@Test`, indicating it's a test case. The method is named `testCaseName` and takes a `Map<String, String>` parameter named `data`. The test name must match the column name "testname" in the Excel sheet for the test to run; otherwise, it will be ignored. The match has to be there in both the RUNMANAGER and TESTDATA sheets. `data` is a HashMap containing all the values of test data needed to run the tests.

**Reading JSON Payload:**
```java
String body = FileReadUtils.getJsonFileLocationAsString(FrameworkConstants.getJsonPayload("createPatient.json"));
```
    This line reads the content of a JSON file and stores it in the `body` variable.

**Making API Request:**
```java
ApiResponse apiResponse = requestCall(PropertyUtils.getValue(ConfigProperties.AUTHTYPE), "POST",
        data.get("endpoints"), data.get("endpointname"), null,
        "plain", body, false);
```
This line makes an API request using the `requestCall` method (presumably inherited from `RequestBuilder`). It specifies the request method as POST and provides various parameters such as authentication type, endpoints, endpoint name, headers, content type, request body (`body`), and a flag for body verification.

**Assertion:**
```java
Assertions.assertThat(apiResponse.isBodyVerificationFlag()).isEqualTo(true);
```
    This line uses AssertJ to check if the `isBodyVerificationFlag` property of the `apiResponse` object is equal to true. If false, the test will fail.
