package org.gcit.utils;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;

public final class RestUtils {
    private RestUtils(){}

    public static String extractPatientID(String newURL) {
        // Use regular expression to extract the desired substring
        String regex = "/Patient/\\d+";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(newURL);
        if (matcher.find()) {
            // Extract the matched substring
            String endpoint = matcher.group();
            return endpoint;
        } else {
            return null;
        }
    }
    public static String findHeaderValue(String headerName, Response response) {
        Headers headers = response.getHeaders();
        for (Header hd : headers) {
            if (hd.getName().equalsIgnoreCase(headerName)) {
                return hd.getValue();
            }
        }
        System.out.println("Header not found in response");
        return null;
    }

}
