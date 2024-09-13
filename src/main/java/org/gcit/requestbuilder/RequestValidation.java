package org.gcit.requestbuilder;
import org.gcit.reports.ExtentLogger;

public final class RequestValidation {
    public String responseTimeValidation(long responseTime) {
        try {
            if (responseTime < 1000) {
                return "Low response time";
            } else {
                ExtentLogger.fail("This endpoint took more than 1 sec for response !");
                return "High response time";
            }
        } catch (RuntimeException e) {
            ExtentLogger.fail("Exception during response time validation: " + e.getMessage());
            return "Exception occurred";
        }
    }
}
