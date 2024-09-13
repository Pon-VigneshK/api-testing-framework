
package org.gcit.requestbuilder;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.gcit.constants.FrameworkConstants;
import org.gcit.enums.ConfigProperties;
import org.gcit.reports.ExtentLogger;
import org.gcit.utils.PropertyUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.baseURI;
import static org.gcit.enums.ConfigProperties.*;
import static org.gcit.enums.LogType.ERROR;
import static org.gcit.enums.LogType.INFO;
import static org.gcit.logger.LogService.log;
import static org.gcit.utils.PropertyUtils.getValue;

public class RequestBuilder {


    private static final String HEADER_CONTENT_TYPE_NAME = "Content-Type";
    private static final String HEADER_ACCEPT_NAME = "Accept";

    /**
     * Creates and returns headers required for the HTTP request.
     *
     * @return Headers object containing organizationIdHeader and contentTypeHeader
     */
    private static Headers createHeaders() {
        Header contentTypeHeader = new Header(HEADER_CONTENT_TYPE_NAME, getValue(CONTENTTYPEHEADER));
        Header acceptHeader = new Header(HEADER_ACCEPT_NAME, getValue(ACCEPTHEADER));
        return new Headers(contentTypeHeader, acceptHeader);
    }
    /**
     * Writes the response content to a file.
     *
     * @param responseContent The response of the HTTP response.
     * @param filePath        The path to the file where the response should be written.
     */
    private static void writeToFile(String responseContent, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(responseContent);
            log(INFO, "Response written to file successfully at the following file path : " + filePath);
            //ExtentLogger.log("Response written to file successfully at the following file path: " + filePath);
        } catch (IOException e) {
            log(ERROR, "Error writing response to file");
            ExtentLogger.fail("Error writing response to file");
            throw new RuntimeException("Error writing response to file", e);
        }
    }
    /**
     * Retrieves the base URL from the configuration.
     *
     * @return The base URL for the HTTP request.
     */
    /**
     * Retrieves the base URL from the configuration.
     *
     * @return The base URL for the HTTP request.
     */
    private static String getBaseUrl() {
        return getValue(ConfigProperties.BASEURL);
    }

    /**
     * Handles the content type of the HTTP response and logs the information accordingly.
     *
     * @param response The HTTP response.
     * @param name     The name of the write the file with the name.
     */
    private static void handleContentType(Response response, String name) {
        String contentType = response.contentType().toLowerCase();
        if (contentType.contains("json")) {
            String fileName = FrameworkConstants.getResponseOutputPath(name, "json");
            writeToFile(response.asPrettyString(), fileName);
            ExtentLogger.log("Response is in Json format");
            ExtentLogger.logPrettyJsonResponseToReport(response);
            if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
                log(INFO, "Response :" + response.asPrettyString());
            }
        } else if (contentType.contains("xml")) {
            String fileName = FrameworkConstants.getResponseOutputPath(name, "xml");
            writeToFile(response.asPrettyString(), fileName);
            ExtentLogger.log("Response is in XML format");
            ExtentLogger.logPrettyXmlResponseToReport(response);
            if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
                log(INFO, "Response :" + response.asPrettyString());
            }
        } else {
            String fileName = FrameworkConstants.getResponseOutputPath(name, "txt");
            writeToFile("Unsupported content type or null response.", fileName);
            ExtentLogger.logResponseToReport(response);
            log(INFO, "Unsupported content type or null response.");
        }
    }

    /**
     * Validates the HTTP response and logs the information accordingly.
     *
     * @param response     The HTTP response.
     * @param endpointName The name of the API endpoint.
     * @return true if response validation passes, false otherwise.
     */
    private boolean validateResponse(Response response, String endpointName, Object expectedResponseCode, Map<String, Object> responseBodyVerification, String responseBodyType) {
        String endpointNameLowerCase = endpointName.toLowerCase();
        boolean bodyVerificationFlag = false;
        String actualResponseCode = String.valueOf(response.getStatusCode());
        RequestValidation statusValidation = new RequestValidation();
        if (endpointNameLowerCase.contains("negative") || endpointNameLowerCase.contains("invalid")) {
            if (actualResponseCode==expectedResponseCode) {
                log(INFO, "Current API endpoint status code is: " + actualResponseCode +
                        " and response time taken by the endpoint is: " + response.getTime() + " " +
                        statusValidation.responseTimeValidation(response.getTime()));
                ExtentLogger.log("Current API endpoint status code is: " + actualResponseCode +
                        " and response time taken by the endpoint is: " + response.getTime() + " " +
                        statusValidation.responseTimeValidation(response.getTime()));
                bodyVerificationFlag = true;
            } else {
                log(ERROR, "Expected response code ["+expectedResponseCode+" ] does not match the current endpoint response code [" + actualResponseCode + "]");
                ExtentLogger.fail("Expected response code [" + expectedResponseCode + "] does not match the current endpoint response code [" + actualResponseCode + "]");
            }
        } else {
            if (actualResponseCode==expectedResponseCode) {
                log(INFO, "Current API endpoint status code is: " + actualResponseCode +
                        " and response time taken by the endpoint is: " + response.getTime() + " " +
                        statusValidation.responseTimeValidation(response.getTime()));
                ExtentLogger.log("Current API endpoint status code is: " + actualResponseCode +
                        " and response time taken by the endpoint is: " + response.getTime() + " " +
                        statusValidation.responseTimeValidation(response.getTime()));
                if (!"plain".equalsIgnoreCase(responseBodyType)) {
                    if ("json".equalsIgnoreCase(responseBodyType)) {
                        // Validate JSON response body
                        bodyVerificationFlag = AssertionUtils.jsonResponseBodyVerification(response, responseBodyVerification);
                    }
                } else {
                    bodyVerificationFlag = true;
                }
            } else {
                log(ERROR, "Expected response code ["+expectedResponseCode+" ] does not match the current endpoint response code [" + actualResponseCode + "]");
                ExtentLogger.fail("Expected response code [" + expectedResponseCode + "] does not match the current endpoint response code [" + actualResponseCode + "]");
            }
        }
        return bodyVerificationFlag;
    }

    /**
     * Executes the HTTP request based on the provided parameters.
     *
     * @param authType    The authentication type for the request.
     * @param requestType The HTTP request type (GET, POST, PUT, etc.).
     * @param bodyPayLoad The payload for the request body.
     * @return Response object containing the HTTP response.
     */
    private Response executeHttpRequest(String authType, String requestType, String bodyPayLoad, String endPoint) {
        // Create request specification
        RequestSpecification request;
        try {
            switch (authType.toLowerCase()) {
                case "oauth2":
                    request = RestAssured.given().auth().oauth2(AuthorizationManager.getCurrentSessionToken()).when();
                    break;
                case "basic":
                    request = RestAssured.given().auth().basic(getValue(USERNAME), getValue(PASSWORD));
                    break;
                case "apikeys":
                    request = RestAssured.given().queryParam("api_key", getValue(APIKEY));
                    break;
                default:
                    // Default to no authentication
                    request = RestAssured.given().auth().none();
                    break;
            }
            // Set headers for the request
            Headers allHeaders = createHeaders();
            request.headers(allHeaders);
            // Set request body if applicable
            if (bodyPayLoad != null) {
                request.body(bodyPayLoad);
            }
            // Perform HTTP request
            switch (requestType.toUpperCase()) {
                case "PUT":
                    return request.put(endPoint);
                case "GET":
                    return request.get(endPoint);
                case "POST":
                    return request.post(endPoint);
                case "PATCH":
                    return request.patch(endPoint);
                case "HEAD":
                    return request.head(endPoint);
                case "DELETE":
                    return request.delete(endPoint);
                default:
                    throw new IllegalArgumentException("Unsupported request type: " + requestType);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error executing HTTP request: " + e.getMessage());
        }

    }

    /**
     * Builds and executes the HTTP request based on the provided parameters.
     *
     * @param authType                 The authentication type for the request.
     * @param requestType              The HTTP request type (GET, POST, PUT, etc.).
     * @param endPoint                 The endpoint of the API.
     * @param endpointName             The name of the API endpoint.
     * @param responseBodyVerification Map containing expected response body values for verification.
     * @param responseBodyType         The expected response body type (JSON, XML, plain, etc.).
     * @param bodyPayLoad              The payload for the request body.
     * @param needResponseBody         Flag indicating whether the response body is needed.
     * @return ApiResponse object containing the verification status and response.
     */
    public ApiResponse requestCall(String authType, String requestType, String endPoint, String endpointName,Object expectedResponseCode,
                                   Map<String, Object> responseBodyVerification, String responseBodyType, String bodyPayLoad,
                                   Boolean needResponseBody) {
        // Set base URI from the configured base URL
        baseURI = getValue(BASEURL);
        ExtentLogger.log("The requested call is a " + requestType.toUpperCase() + " method, and the endpoint is " + baseURI + endPoint);
        log(INFO, "The requested call is a " + requestType.toUpperCase() + " method, and the endpoint is " + baseURI + endPoint);
        // Execute HTTP request
        Response response = executeHttpRequest(authType, requestType, bodyPayLoad, endPoint);
        String fileName = requestType.toUpperCase() + "_Call_" + endpointName + "_Response";
        handleContentType(response, fileName);
        // Validate response
        boolean bodyVerificationFlag = validateResponse(response, endpointName, expectedResponseCode, responseBodyVerification, responseBodyType);
        // Check if the response body is needed and return ApiResponse accordingly
        if ((needResponseBody != null && needResponseBody)) {
            return new ApiResponse(bodyVerificationFlag, response);
        } else {
            return new ApiResponse(bodyVerificationFlag, null);
        }
    }
}
