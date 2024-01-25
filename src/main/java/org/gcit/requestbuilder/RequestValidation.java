package org.gcit.requestbuilder;

import io.restassured.response.Response;
import org.apache.commons.lang3.ArrayUtils;
import org.gcit.reports.ExtentLogger;
import static org.assertj.core.api.Assertions.assertThat;

public final class RequestValidation {
    public boolean statusCodeValidation(Response response) {
        int responseStatusCode = response.getStatusCode();
        int[] passStatusCodes = {200, 201, 204};

        if (ArrayUtils.contains(passStatusCodes, responseStatusCode)) {
            return true;
        } else {
            ExtentLogger.fail("Unexpected response status code: " + response.getStatusCode());
            return false;
        }
    }


    public String responseTimeValidation(long responseTime) {
        try {
            if (responseTime < 1000) {
                return "Low response time";
            } else {
                ExtentLogger.warn("This endpoint took more than 1 sec for response !");
                return "High response time";
            }
        } catch (RuntimeException e) {
            ExtentLogger.fail("Exception during response time validation: " + e.getMessage());
            return "Exception occurred";
        }
    }
}
