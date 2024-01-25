package org.gcit.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.gcit.constants.FrameworkConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public final class JsonUtils {
    private JsonUtils(){}
    // HashMap to store test runner data.
    private static LinkedHashMap<String, LinkedHashMap<String, ArrayList<HashMap<String, Object>>>> testRunnerHashMap =
            new LinkedHashMap<>();
    // HashMap to store final test list data.
    private static LinkedHashMap<String, ArrayList<HashMap<String, Object>>> finalTestList = new LinkedHashMap<>();
    /**
     * Read RunnerList data from Excel and write it to a JSON file.
     *
     * @param sheetName Name of the Excel sheet containing data.
     */
    public static void generateRunnerListJsonDataFromExcel(String sheetName) {
        try (FileInputStream fs = new FileInputStream(FrameworkConstants.getExcelFilePath())) {
            XSSFWorkbook workbook = new XSSFWorkbook(fs);
            XSSFSheet sheet = workbook.getSheet(sheetName);
            int lastrownum = sheet.getLastRowNum();
            int lastcolnum = sheet.getRow(0).getLastCellNum();
            HashMap<String, Object> map;
            ArrayList<HashMap<String, Object>> testDataList = new ArrayList<>();
            String testCaseListName = "testCaseLists";
            for (int i = 1; i <= lastrownum; i++) {
                map = new HashMap<>();
                for (int j = 0; j < lastcolnum; j++) {
                    String key = sheet.getRow(0).getCell(j).getStringCellValue();
                    String value = convertCellValueToString(sheet.getRow(i).getCell(j));
                    map.put(key, value);
                }
                testDataList.add(map);
                finalTestList.put(testCaseListName, testDataList); // Use a unique key for each test data entry
            }
            testRunnerHashMap.put(FrameworkConstants.getRunmanager(), finalTestList);
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FrameworkConstants.getTestCaseJsonPath()), testRunnerHashMap);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Convert cell value to a string based on its type.
     *
     * @param cell The Excel cell to convert.
     * @return String representation of the cell value.
     */
    private static String convertCellValueToString(Cell cell) {
        if (cell == null) {
            return "";
        }
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                double numericValue = cell.getNumericCellValue();
                if (numericValue % 1 == 0) {
                    return String.valueOf((int) numericValue);
                } else {
                    return String.valueOf(numericValue);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }}
    /**
     * Get test details from the JSON file based on high-level and nested key names.
     *
     * @param highLevelKeyName The high-level key name in the JSON structure.
     * @param nestedKeyName    The nested key name in the JSON structure.
     * @return List of test details as maps.
     */
    public static List<Map<String, Object>> getTestDetails(String highLevelKeyName, String nestedKeyName) {
        List<Map<String, Object>> testDetailsList = new ArrayList<>();
        FileInputStream fileInputStream = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            fileInputStream = new FileInputStream(FrameworkConstants.getTestCaseJsonPath());

            // Change the types for better readability and maintainability
            HashMap<String, HashMap<String, List<Map<String, Object>>>> testCaseMap =
                    objectMapper.readValue(fileInputStream, HashMap.class);

            // Retrieve the nested list using highLevelKeyName
            List<Map<String, Object>> jsonTestCaseList = testCaseMap.get(highLevelKeyName).get(nestedKeyName);

            // Flatten the nested list and add to the result list
            for (Map<String, Object> testCase : jsonTestCaseList) {
                testDetailsList.add(testCase);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the FileInputStream in the finally block
            if (Objects.nonNull(fileInputStream)) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return testDetailsList;
    }

}

