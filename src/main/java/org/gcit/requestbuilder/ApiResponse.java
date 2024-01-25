package org.gcit.requestbuilder;

import io.restassured.response.Response;

public class ApiResponse {
    // Flag indicating whether body verification is required
    private final boolean bodyVerificationFlag;
    private final Response response;
    /**
     * Constructs an ApiResponse object.
     *
     * @param bodyVerificationFlag Flag indicating whether body verification is required.
     * @param response              The API response object.
     */
    public ApiResponse(boolean bodyVerificationFlag, Response response) {
        this.bodyVerificationFlag = bodyVerificationFlag;
        this.response = response;
    }

    public boolean isBodyVerificationFlag() {
        return bodyVerificationFlag;
    }

    public Response getResponse() {
        return response;
    }
}
