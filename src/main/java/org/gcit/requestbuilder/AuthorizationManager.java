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

/**
 * This class manages authorization for the application using various authentication methods.
 * It provides methods to retrieve authentication-related properties and obtain access tokens.
 */
public final class AuthorizationManager {

    private AuthorizationManager() {
    }

    private static String getRequiredProperty(ConfigProperties key, String errorMessage) {
        String prop = PropertyUtils.getValue(key);
        if (prop != null) {
            return prop;
        } else {
            throw new RuntimeException(errorMessage);
        }
    }

    public static String getClientId() {
        return getRequiredProperty(ConfigProperties.CLIENTID, "Property client_id is not specified in the properties file");
    }

    public static String getClientSecret() {
        return getRequiredProperty(ConfigProperties.CLIENTSECRET, "Property client_secret is not specified in the properties file");
    }

    public static String getAuthType() {
        return getRequiredProperty(ConfigProperties.AUTHTYPE, "Property auth_type is not specified in the properties file");
    }

    public static String getAccessTokenURL() {
        return getRequiredProperty(ConfigProperties.ACCESSTOKENURL, "Property access_token_url is not specified in the properties file");
    }

    public static String getUserName() {
        return getRequiredProperty(ConfigProperties.USERNAME, "Property username is not specified in the properties file");
    }

    public static String getPassword() {
        return getRequiredProperty(ConfigProperties.PASSWORD, "Property password is not specified in the properties file");
    }

    public static String getApiKey() {
        return getRequiredProperty(ConfigProperties.APIKEY, "Property api_key is not specified in the properties file");
    }
    public static String getScope() {
        return getRequiredProperty(ConfigProperties.SCOPE, "Property scope is not specified in the properties file");
    }

    public static String getAuthPrefix() {
        return getRequiredProperty(ConfigProperties.AUTHPREFIX, "Property auth_prefix is not specified in the properties file");
    }

    public static String getAudience() {
        return getRequiredProperty(ConfigProperties.AUDIENCE, "Property audience is not specified in the properties file");
    }
    public static String getCurrentSessionToken() {
        try {
            // Disable SSL verification (use it cautiously)
            RestAssured.useRelaxedHTTPSValidation();
            Map<String,String> body = new HashMap<>();
            body.put("grant_type", "client_credentials");
            body.put("audience", getAudience());
            // Make a POST request to obtain the access token
            String accessToken = RestAssured.given()
                    .auth().preemptive().basic(getClientId(), getClientSecret())
                    .contentType(ContentType.JSON)
                    .body(body)
                    .post(getAccessTokenURL()).then().statusCode(200).extract().jsonPath().get("access_token");
            String apiCallHeaders = accessToken;
            ExtentLogger.log("User authenticated successfully, with the client id of: " + getClientId());
            //ExtentLogger.log("Auth Token: " + apiCallHeaders);
            System.setProperty("auth_token", apiCallHeaders);
            return apiCallHeaders;
        } catch (RuntimeException e) {
            ExtentLogger.fail("Authentication failed: " + e.getMessage());
            return null;
        }
    }
    }




