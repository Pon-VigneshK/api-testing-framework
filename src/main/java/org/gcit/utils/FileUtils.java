package org.gcit.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Utility class for working with files.
 */
public final class FileUtils {

    // Private constructor to prevent instantiation, as this is a utility class.
    private FileUtils() {

    }

    /**
     * Reads the content of a JSON file and returns it as a string.
     *
     * @param location The path to the JSON file.
     * @return The content of the JSON file as a string.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public static String getJsonFileAsString(String location) throws IOException {
        // Read all bytes from the specified file location and convert to a string.
        return new String(Files.readAllBytes(Paths.get(location)));
    }
}
