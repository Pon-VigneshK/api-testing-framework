package org.gcit.requestbuilder;

import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.gcit.enums.ConfigProperties;
import org.gcit.reports.ExtentLogger;
import org.gcit.utils.PropertyUtils;
import java.util.HashMap;
import java.util.Map;

import java.util.HashMap;

import static org.gcit.enums.ConfigProperties.*;
import static org.gcit.enums.LogType.ERROR;
import static org.gcit.enums.LogType.INFO;
import static org.gcit.logger.LogService.log;
import static org.gcit.utils.PropertyUtils.getValue;

/**
 * This class manages authorization for the application using various authentication methods.
 * It provides methods to retrieve authentication-related properties and obtain access tokens.
 */
public final class AuthorizationManager {

    private AuthorizationManager() {
    }
    public static String getCurrentSessionToken() {
        try {
            // Disable SSL verification (use it cautiously)
            RestAssured.useRelaxedHTTPSValidation();
            Map<String,String> body = new HashMap<>();
            body.put("grant_type", "client_credentials");
            body.put("audience", getValue(AUDIENCE));
            // Make a POST request to obtain the access token
            String accessToken = RestAssured.given()
                    .auth().preemptive().basic(getValue(CLIENTID), getValue(CLIENTSECRET))
                    .contentType(ContentType.JSON)
                    .body(body)
                    .post(getValue(ACCESSTOKENURL)).then().statusCode(200).extract().jsonPath().get("access_token");
            String apiCallHeaders = accessToken;
            ExtentLogger.log("User authenticated successfully, with the client id of: " + getValue(CLIENTID));
            ExtentLogger.log("Auth Token: " + apiCallHeaders);
            log(INFO,"User authenticated successfully, with the client id of: " + getValue(CLIENTID));
            log(INFO,"Auth Token: " + apiCallHeaders);
            System.setProperty("auth_token", apiCallHeaders);
            return apiCallHeaders;
        } catch (RuntimeException e) {
            ExtentLogger.fail("Authentication failed: " + e.getMessage());
            log(ERROR,"Authentication failed: " + e.getMessage());
            return null;
        }
    }
    }




