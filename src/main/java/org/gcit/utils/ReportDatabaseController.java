package org.gcit.utils;

import org.gcit.constants.FrameworkConstants;
import org.gcit.enums.ConfigProperties;
import org.gcit.enums.LogType;
import org.gcit.logger.LogService;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.gcit.enums.LogType.*;
import static org.gcit.logger.LogService.log;

public class ReportDatabaseController {

    private ReportDatabaseController() {
    }

    public static void historyLog(String testCaseName, String status) {
        if (PropertyUtils.getValue(ConfigProperties.RUNMODE).equalsIgnoreCase("local")) {
            String insertQuery = "INSERT INTO `service_automation`.`result` " +
                    "(`environment`, `testCaseName`, `status`, `executionTime`) " +
                    "VALUES (?, ?, ?, ?)";

            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = DataBaseConnectionUtils.getMyConn();

                // Check if connection is null or closed, and reconnect if necessary
                if (connection == null || connection.isClosed()) {
                    log(WARN, "Database connection was closed. Attempting to reconnect...");
                    connection = DataBaseConnectionUtils.getMyConn();
                }

                if (connection != null) {
                    statement = connection.prepareStatement(insertQuery);
                    statement.setString(1, FrameworkConstants.getEnvironment().toUpperCase());
                    statement.setString(2, testCaseName);
                    statement.setString(3, status);
                    statement.setTimestamp(4, java.sql.Timestamp.valueOf(LocalDateTime.now()));

                    statement.executeUpdate();
                    log(INFO, "History log for test case [{}] inserted successfully."+ testCaseName);
                } else {
                    log(ERROR, "Failed to obtain a valid database connection.");
                }

            } catch (SQLException e) {
                log(ERROR, "SQLException occurred while logging history for test case [{}]"+testCaseName);
            } finally {
                // Close statement and connection
                try {
                    if (statement != null) {
                        statement.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    log(ERROR, "Error while closing database resources.");
                }
            }
        } else {
            log(ERROR, "Run mode is not local. Skipping database logging.");

        }
    }
}
