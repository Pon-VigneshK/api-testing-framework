
package org.gcit.constants;

import org.gcit.enums.ConfigProperties;
import org.gcit.utils.PropertyUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
/**
 * <p>FrameworkConstants holds all the constant values used within the framework. If a value needs to be changed
 * or modified frequently, it should be stored in the property files.
 * </p>
 *
 * @author Pon Vignesh K
 * @date 2024-07-02
 * @version 1.0
 * @since 1.0
 * @see org.gcit.utils.PropertyUtils
 */
public final class FrameworkConstants {

    // Separator used to create file paths, making it platform-independent
    private static String separator = File.separator;

    /**
     * FrameworkConstants holds all the constant values used within the framework.
     * If some value needs to be changed or modified often, then it should be stored in the property files.
     */

    // Base directory path for the project
    private static final String USER_DIR = System.getProperty("user.dir");

    // Path to the test resources directory
    private static final String TEST_RESOURCES_PATH = USER_DIR + separator + "src" + separator + "test" + separator + "resources";

    // Path to the main resources directory
    private static final String MAIN_RESOURCES_PATH = USER_DIR + separator + "src" +separator + "main" + separator + "resources";

    // Path to the configuration files
    private static final String CONFIG_FILE_PATH = TEST_RESOURCES_PATH + separator + "config";

    // Path to the directory where Extent Reports will be generated
    private static final String EXTENT_REPORT_PATH = USER_DIR + separator + "extent-test-output";

    // Path to the directory where API responses will be saved
    private static final String RESPONSE_OUTPUT_PATH = USER_DIR + separator + "response-test-output" + separator;

    // Path to the directory where Excel reports will be generated
    private static final String EXCEL_REPORT_PATH = USER_DIR + separator + "excel-test-report";

    // Path to the payload resources directory
    private static final String PAYROLL_RESOURCES_PATH = TEST_RESOURCES_PATH + separator + "payload";

    // Current date and time, formatted for report filenames
    private static final Date CURRENT_DATE = new Date();
    private static final String TODAY_DATE_TIME = new SimpleDateFormat("MMM-dd-yyyy_HH_mm_ss_SSS").format(CURRENT_DATE);

    // Path to the Excel file containing test data
    private static final String EXCEL_PATH = TEST_RESOURCES_PATH + separator + "testdata" + separator + "excel" + separator + "testdata.xlsx";

    // Path to the JSON file containing the list of test cases to be executed
    private static final String TESTCASE_JSON_PATH = MAIN_RESOURCES_PATH + separator + "runnerlist" + separator + "runmanager.json";

    // Sheet names within the Excel file
    private static final String DATA_EXCEL_SHEET = "DATA";
    private static final String RUNMANGER_EXCEL_SHEET = "RUNMANAGER";

    // Flag to ensure certain operations are performed only once
    private static boolean flag = false;

    // Path to the JSON file containing SQL queries
    private static final String SELECTQUERY_JSON_FILEPATH = MAIN_RESOURCES_PATH + separator + "Configuration" + separator + "SqlQueries.json";

    // Constant representing the RunManager
    private static final String RUNMANAGER = "RunManager";

    // Constant representing the list of test cases
    private static final String TESTCASE_LIST = "testCaseLists";

    // Path to the JSON file containing test data (environment-specific)
    private static String TESTDATAJSONFILEPATH = TEST_RESOURCES_PATH + separator + "testdata" + separator + "json";

    // Variables to store report class name and file path
    private static String reportClassName = "";
    private static String reportFilePath = "";

    // Current environment, fetched from property files
    private static String ENVIRONMENT = PropertyUtils.getValue(ConfigProperties.ENV);

    // Path to the SQLite database file used for test data
    private static String DATABASE_PATH = TEST_RESOURCES_PATH + separator + "testdata" + separator + "database" + separator + "inputData.db";
    private static final String DBConfig_JSON_FILEPATH = MAIN_RESOURCES_PATH + File.separator + "Configuration" + File.separator + "DataBaseConfig.json";

    /**
     * @return the sheet name for the RunManager in the Excel file
     */
    public static String getRunmangerExcelSheet() {
        return RUNMANGER_EXCEL_SHEET;
    }

    /**
     * @return the file path of the Excel test data file
     */
    public static String getExcelFilePath() {
        return EXCEL_PATH;
    }

    /**
     * @return the current date and time formatted as a string
     * @author Pon Vignesh K
     * @date 2024-07-02
     * @version 1.0
     */
    public static String getTodayDateTime() {
        return TODAY_DATE_TIME;
    }

    /**
     * @return the file path of the JSON file containing the list of test cases
     */
    public static String getTestCaseJsonPath() {
        return TESTCASE_JSON_PATH;
    }

    /**
     * @return the constant representing the list of test cases
     */
    public static String getTestcaselist() {
        return TESTCASE_LIST;
    }

    /**
     * @return the constant representing the RunManager
     */
    public static String getRunmanager() {
        return RUNMANAGER;
    }

    /**
     * Creates the path for the report file, based on whether reports should be overridden.
     *
     * @return the path where the report file should be saved
     * @author Pon Vignesh K
     * @date 2024-07-02
     * @version 1.0
     */
    private static String createReportPath() {
        try {
            String reportPath = EXTENT_REPORT_PATH + separator + getTodayDateTime() + separator + reportClassName + "_Automation Report";
            if (PropertyUtils.getValue(ConfigProperties.OVERRIDEREPORTS).equalsIgnoreCase("NO")) {
                return EXTENT_REPORT_PATH + separator + getTodayDateTime() + separator + reportClassName + "_Automation Report" + ".html";
            } else {
                return EXTENT_REPORT_PATH + separator + "index.html";
            }
        } catch (Exception e) {
            // Log the exception or handle it accordingly
            e.printStackTrace();
            return "";
        }
    }

    /**
     * @return the file path of the JSON file containing SQL queries
     */
    public static String getSqlQueryjsonfilepath() {
        return SELECTQUERY_JSON_FILEPATH;
    }

    /**
     * Sets the file path for the test data JSON file based on the environment name.
     *
     * @param environmentName the name of the environment
     * @author Pon Vignesh K
     * @date 2024-07-02
     * @version 1.0
     */
    public static void setTestDataJsonFilePath(String environmentName) {
        if (!flag) {
            if (Objects.isNull(environmentName)) {
                TESTDATAJSONFILEPATH = TESTDATAJSONFILEPATH + separator + environmentName.toUpperCase() + "_TestData.json";
            } else {
                TESTDATAJSONFILEPATH = TESTDATAJSONFILEPATH + separator + getEnvironment().toUpperCase() + "_TestData.json";
            }
            flag = true;
        }
    }

    /**
     * @return the file path of the SQLite database used for test data
     */
    public static String getDatabasePath() {
        return DATABASE_PATH;
    }

    /**
     * Constructs the output path for API response files based on the endpoint name and response type.
     *
     * @param endpointName the name of the API endpoint
     * @param responseType the type of the response (e.g., JSON, XML)
     * @return the file path where the response should be saved
     * @author Pon Vignesh K
     * @date 2024-07-02
     * @version 1.0
     */
    public static String getResponseOutputPath(String endpointName, String responseType) {
        try {
            String fileExtension;
            if (responseType.equalsIgnoreCase("JSON")) {
                fileExtension = "json";
            } else if (responseType.equalsIgnoreCase("XML")) {
                fileExtension = "xml";
            } else {
                fileExtension = "txt";
            }

            return RESPONSE_OUTPUT_PATH + endpointName + "." + fileExtension;
        } catch (Exception e) {
            e.printStackTrace();
            return RESPONSE_OUTPUT_PATH + endpointName + ".txt";
        }
    }

    /**
     * Sets the class name for the report, used in generating the report file path.
     *
     * @param reportClassName the name of the report class
     */
    public static void setReportClassName(String reportClassName) {
        FrameworkConstants.reportClassName = reportClassName;
    }

    /**
     * @return the file path of the configuration file
     */
    public static String getConfigFilePath() {
        return CONFIG_FILE_PATH + separator + "config.properties";
    }

    /**
     * @return the file path where the report will be saved
     */
    public static String getReportPath() {
        if (reportFilePath.isEmpty()) {
            reportFilePath = createReportPath();
        }
        return reportFilePath;
    }
    public static String getTestDataJsonFilePath() {
        return TESTDATAJSONFILEPATH;
    }

    /**
     * @return the file path of the test data JSON file
     */
    public static String getTestDataJsonFilePath(String environmentName) {
        if (!flag) {
            if (Objects.isNull(environmentName))
                return TESTDATAJSONFILEPATH + File.separator + "QA_TestData.json";  // Assuming a default name if environmentName is null
            else
                return TESTDATAJSONFILEPATH + File.separator + environmentName.toUpperCase() + "_TestData.json";
        } else {
            return "";
        }
    }

    /**
     * @return the sheet name for the data in the Excel file
     */
    public static String getDataExcelSheet() {
        return DATA_EXCEL_SHEET;
    }

    /**
     * @return the current environment name
     */
    public static String getEnvironment() {
        return ENVIRONMENT;
    }

    /**
     * Sets the environment name.
     *
     * @param environment the name of the environment
     */
    public static void setEnvironment(String environment) {
        FrameworkConstants.ENVIRONMENT = environment;
    }
    public static String getDBConfigJSONPath() {
        return DBConfig_JSON_FILEPATH;
    }

    /**
     * Constructs the file path for document payloads based on the file name.
     *
     * @param fileName the name of the file
     * @return the file path for the document payload
     * @author Pon Vignesh K
     * @date 2024-07-02
     * @version 1.0
     */
    public static String getDocumentPayload(String fileName) {
        return PAYROLL_RESOURCES_PATH + separator + "docs" + File.separator + fileName;
    }

    /**
     * Constructs the file path for JSON payloads based on the file name.
     *
     * @param fileName the name of the file
     * @return the file path for the JSON payload
     * @author Pon Vignesh K
     * @date 2024-07-02
     * @version 1.0
     */
    public static String getJsonPayload(String fileName) {
        return PAYROLL_RESOURCES_PATH + separator + "json" + File.separator + fileName;
    }

    /**
     * Constructs the file path for image payloads based on the file name.
     *
     * @param fileName the name of the file
     * @return the file path for the image payload
     * @author Pon Vignesh K
     * @date 2024-07-02
     * @version 1.0
     */
    public static String getImagePayload(String fileName) {
        return PAYROLL_RESOURCES_PATH + separator + "image" + File.separator + fileName;
    }

    /**
     * Constructs the file path for XML payloads based on the file name.
     *
     * @param fileName the name of the file
     * @return the file path for the XML payload
     * @author Pon Vignesh K
     * @date 2024-07-02
     * @version 1.0
     */
    public static String getXmlPayload(String fileName) {
        return PAYROLL_RESOURCES_PATH + separator + "xml" + File.separator + fileName;
    }

    /**
     * Constructs the file path for HTML payloads based on the file name.
     *
     * @param fileName the name of the file
     * @return the file path for the HTML payload
     * @author Pon Vignesh K
     * @date 2024-07-02
     * @version 1.0
     */
    public static String getHtmlPayload(String fileName) {
        return PAYROLL_RESOURCES_PATH + separator + "html" + File.separator + fileName;
    }
}
