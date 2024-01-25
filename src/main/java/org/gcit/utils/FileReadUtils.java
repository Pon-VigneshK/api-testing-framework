package org.gcit.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class FileReadUtils {
    private FileReadUtils() {
    }

    public static String getJsonFileLocationAsString(String location) {
        try {
            System.out.println(location);
            return new String(Files.readAllBytes(Paths.get(location)));
        } catch (IOException e) {
            // Handle the exception as needed, e.g., log it
            System.err.println("An error occurred while reading the file: " + e.getMessage());
            return null;
        }
    }
}
