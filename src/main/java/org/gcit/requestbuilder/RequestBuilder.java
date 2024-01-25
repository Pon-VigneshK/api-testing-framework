
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

public class RequestBuilder {


    private static final String HEADER_CONTENT_TYPE_NAME = "Content-Type";
    private static final String HEADER_ACCEPT_NAME = "Accept";

    /**
     * Creates and returns headers required for the HTTP request.
     *
     * @return Headers object containing organizationIdHeader and contentTypeHeader
     */
    private static Headers createHeaders() {
        Header contentTypeHeader = new Header(HEADER_CONTENT_TYPE_NAME, "application/json");
        return new Headers(contentTypeHeader);
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
            //ExtentLogger.log("Response written to file successfully at the following file path: " + filePath);
        } catch (IOException e) {
            ExtentLogger.fail("Error writing response to file");
            throw new RuntimeException("Error writing response to file", e);
        }
    }
    /**
     * Retrieves the base URL from the configuration.
     *
     * @return The base URL for the HTTP request.
     */
    private static String getBaseUrl(){
        return PropertyUtils.getValue(ConfigProperties.BASEURL);
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
        } else if (contentType.contains("xml")) {
            String fileName = FrameworkConstants.getResponseOutputPath(name, "xml");
            writeToFile(response.asPrettyString(), fileName);
            ExtentLogger.log("Response is in XML format");
            ExtentLogger.logPrettyXmlResponseToReport(response);
        } else {
            String fileName = FrameworkConstants.getResponseOutputPath(name, "txt");
            writeToFile("Unsupported content type or null response.", fileName);
            ExtentLogger.logResponseToReport(response);
        }
    }

    /**
     * Builds and executes the HTTP request based on the provided parameters.
     *
     * @param authType                The authentication type for the request.
     * @param requestType             The HTTP request type (GET, POST, PUT, etc.).
     * @param endPoint               The endpoint of the API.
     * @param endpointName           The name of the API endpoint.
     * @param responseBodyVerification Map containing expected response body values for verification.
     * @param responseBodyType        The expected response body type (JSON, XML, plain, etc.).
     * @param bodyPayLoad             The payload for the request body.
     * @param needResponseBody        Flag indicating whether the response body is needed.
     * @return ApiResponse object containing the verification status and response.
     */
    public ApiResponse requestCall(String authType, String requestType, String endPoint, String endpointName,
                                Map<String, Object> responseBodyVerification, String responseBodyType, String bodyPayLoad,
                                Boolean needResponseBody) {
        // Flag to track response body verification status
        boolean bodyVerificationFlag = false;
        // Set base URI from the configured base URL
        baseURI = getBaseUrl();
        // Set base path from the provided endpoint
        basePath = endPoint;
        ExtentLogger.log("The requested call is a "+requestType.toUpperCase()+" method, and the endpoint is "+baseURI+basePath);
        // Create headers for the request
        Headers allHeaders = createHeaders();
        Header acceptHeader = new Header(HEADER_ACCEPT_NAME, "application/json");
        // Response and Request objects
        Response response;
        RequestSpecification request;
        try {
            // Choose authentication method based on authType
            switch (authType.toLowerCase()) {
                case "oauth2":
                    request = RestAssured.given().auth().oauth2(AuthorizationManager.getCurrentSessionToken());
                    break;
                case "basic":
                    request = RestAssured.given().auth().basic(AuthorizationManager.getUserName(), AuthorizationManager.getPassword());
                    break;
                case "apikeys":
                    request = RestAssured.given().queryParam("api_key", AuthorizationManager.getApiKey());
                    break;
                case "none":
                    request = RestAssured.given().auth().none();
                    break;
                default:
                    // Default to no authentication
                    request = RestAssured.given().auth().none();
                    break;
            }
            // Set headers for the request
            //request.headers(allHeaders).header(acceptHeader);
            // Choose request type and perform the corresponding action
            switch (requestType.toUpperCase()) {
                case "PUT":
                    request.body(bodyPayLoad);
                    response = request.put();
                    break;
                case "GET":
                    response = request.get();
                    break;
                case "POST":
                    request.body(bodyPayLoad);
                    response = request.post();
                    break;
                case "PATCH":
                    request.body(bodyPayLoad);
                    response = request.patch();
                    break;
                case "HEAD":
                    response = request.head();
                    break;
                case "DELETE":
                    response = request.delete();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported request type: " + requestType);
            }
            // Generate a filename for handling content type
            String fileName = requestType.toUpperCase()+"_Call_"+endpointName+"_Response";
            handleContentType(response, fileName);
            // Perform response validations
            RequestValidation statusValidation = new RequestValidation();
            boolean codeVerification = statusValidation.statusCodeValidation(response);
            if (codeVerification){
                ExtentLogger.log("Current API endpoint status code is: " + response.getStatusCode() +
                        " and response time took by the endpoint is: " + response.getTime() + " " +
                        statusValidation.responseTimeValidation(response.getTime()));
                if (!"plain".equalsIgnoreCase(responseBodyType)) {
                    if ("json".equalsIgnoreCase(responseBodyType)) {
                        // Validate JSON response body
                        if ( AssertionUtils.jsonResponseBodyVerification(response, responseBodyVerification)) {
                            bodyVerificationFlag = true;
                        } else {
                            bodyVerificationFlag = false;
                        }
                    }
                }else {
                    ExtentLogger.log("No response body for this endpoint");
                    bodyVerificationFlag = true;
                }
            } else {
                bodyVerificationFlag = false;
                ExtentLogger.fail("Expected response code  not matched with the current end " +
                        "point response code [" + response.getStatusCode() + "]");
            }
            // Check if the response body is needed and return ApiResponse accordingly
            if (needResponseBody != null && needResponseBody) {
                return new ApiResponse(bodyVerificationFlag, response);
            } else {
                return new ApiResponse(bodyVerificationFlag, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(false, null);
        }

    }
}
