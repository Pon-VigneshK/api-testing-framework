package org.gcit.requestbuilder;

import io.restassured.response.Response;
import org.gcit.reports.ExtentLogger;

import java.util.*;

public final class AssertionUtils {


    public static boolean jsonResponseBodyVerification(Response response, Map<String, Object> expectedValuesMap) {
        // List to store details of actual verifications
        List<ResponseBodyVerification> actualValuesMap = new ArrayList<>();
        actualValuesMap.add(new ResponseBodyVerification("JSON_PATH", "EXPECTED_VALUE", "ACTUAL_VALUE", "RESULT"));
        // Flag to track if all assertions are matched
        boolean allMatched = true;
        // Set of JSON paths from expected values map
        Set<String> jsonPaths = expectedValuesMap.keySet();
        // Iterate through each value in json
        for (String jsonPath : jsonPaths) {
            // Extract verifications value from the response using RestAssured's jsonPath()
            Optional<Object> actualValue = Optional.ofNullable(response.jsonPath().get(jsonPath));
            // Check if verification value is present
            if (actualValue.isPresent()) {
                Object value = actualValue.get();
                // Compare verification value and expected values, set result
                String result = value.equals(expectedValuesMap.get(jsonPath)) ? "MATCHED" : "NOT_MATCHED";
                allMatched &= result.equals("MATCHED");
                // Store verification details in actualValuesMap
                actualValuesMap.add(new ResponseBodyVerification(jsonPath, expectedValuesMap.get(jsonPath), value, result));
            } else {
                // If verification value not found, update allMatched flag and store details
                allMatched = false;
                actualValuesMap.add(new ResponseBodyVerification(jsonPath, expectedValuesMap.get(jsonPath), "VALUE_NOT_FOUND", "NOT_MATCHED"));
            }
        }
        String[][] finalAssertionsMap = actualValuesMap.stream().map(assertions -> new String[] {assertions.getJsonPath(),
                        String.valueOf(assertions.getExpectedValue()), String.valueOf(assertions.getActualValue()), assertions.getResult()})
                .toArray(String[][] :: new);
        ExtentLogger.logInfoTable(finalAssertionsMap);
        if (allMatched)
            ExtentLogger.log("Response body validated successfully with the values of:  " + expectedValuesMap);
        else
            ExtentLogger.fail("Response Body Validation Failed !!! with the values of:  " + expectedValuesMap);
        return allMatched;
    }
}
