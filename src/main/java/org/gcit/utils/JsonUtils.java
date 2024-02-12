package org.gcit.utils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.gcit.constants.FrameworkConstants;
import org.gcit.enums.ConfigProperties;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static org.gcit.utils.DataBaseConnectionUtils.getMyConn;
@SuppressWarnings({ "unchecked", "rawtypes" })
public final class JsonUtils {
    private JsonUtils(){}
    private static List<Map<String, Object>> finaltestcaselist = new ArrayList();

    private static HashMap<String, Object> queriesList= new HashMap();
    private static FileInputStream fileInputStream;
    private static LinkedHashMap<String , LinkedHashMap<String, ArrayList<HashMap<String, Object>>>> testDataHashMap= new LinkedHashMap();
    private static String envName;
    private static List<Map<String, Object>> finalDatalist= new ArrayList<>();
    private static LinkedHashMap<String, ArrayList<HashMap<String, Object>>> finalMap = new LinkedHashMap();

    //test case runner list map
    private static LinkedHashMap<String, ArrayList<HashMap<String, Object>>> finalTestList= new LinkedHashMap();
    private static LinkedHashMap<String , LinkedHashMap<String, ArrayList<HashMap<String, Object>>>> testRunnerHashMap= new LinkedHashMap();
    public static LinkedHashMap<String , LinkedHashMap<String, ArrayList<HashMap<String, Object>>>> getTestDataHashMap() {
        return testDataHashMap;
    }

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
            HashMap<String, HashMap<String, List<Map<String, Object>>>> testCaseMap =
                    objectMapper.readValue(fileInputStream, HashMap.class);
            List<Map<String, Object>> jsonTestCaseList = testCaseMap.get(highLevelKeyName).get(nestedKeyName);
            for (Map<String, Object> testCase : jsonTestCaseList) {
                testDetailsList.add(testCase);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
    public static List<Map<String, Object>> getTestDataDetails(){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            fileInputStream= new FileInputStream(FrameworkConstants.getTestDataJsonFilePath());
            HashMap<String, HashMap> jsonTestDataMap = objectMapper.readValue(fileInputStream, HashMap.class);
            LinkedHashMap<String ,Object> jsonTestDataLinkedMap = (LinkedHashMap) jsonTestDataMap.get(FrameworkConstants.getEnvironment());
            try {
                for (Map.Entry<String, Object> finaltestcasemap : jsonTestDataLinkedMap.entrySet()) {
                    finalDatalist.addAll((ArrayList) finaltestcasemap.getValue());
                }
            }
            catch (RuntimeException e){
                e.printStackTrace();
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {

            if(Objects.nonNull(fileInputStream)){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return finalDatalist;
    }
    public static HashMap<String, Object> getQueryDetails(String queryType){

        try {
            String keyname = queryType.toLowerCase()+"queries";
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            fileInputStream= new FileInputStream(FrameworkConstants.getSqlQueryjsonfilepath());
            HashMap<String, Object> jsontestcasemap = objectMapper.readValue(fileInputStream, HashMap.class);
            queriesList = (HashMap<String, Object>) jsontestcasemap.get(keyname);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {

            if(Objects.nonNull(fileInputStream)){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return queriesList;
    }
    public static void generateTestDataJson() {
        try {

            Statement st =getMyConn().createStatement();
            HashMap<String, Object> queryDetails = getQueryDetails("select");


            for (Map.Entry<String, Object> mapdata : queryDetails.entrySet()) {

                ResultSet resultSet = st.executeQuery((String) mapdata.getValue());
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int columns = resultSetMetaData.getColumnCount();
                HashMap<String, Object> rowDatas;
                ArrayList<HashMap<String, Object>> testDataList = new ArrayList();

                while (resultSet.next()) {
                    rowDatas = new HashMap(columns);
                    for (int i = 1; i <= columns; ++i) {
                        rowDatas.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
                        envName = PropertyUtils.getValue(ConfigProperties.ENV);
                    }
                    testDataList.add(rowDatas);
                }
                System.out.println("env name is:"+ envName);
                finalMap.put(mapdata.getKey(), testDataList);
            }
            testDataHashMap.put(envName,finalMap);
            FrameworkConstants.setEnvironment(envName);

            ObjectMapper mapper = new ObjectMapper();
            FrameworkConstants.setTestDataJsonFilePath(envName);
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FrameworkConstants.getTestDataJsonFilePath()), testDataHashMap);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            if (Objects.nonNull(getMyConn())){
                try {
                    getMyConn().close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void generateRunnerListJsonData() {
        try {
            Statement st =getMyConn().createStatement();
            HashMap<String, Object> queryDetails = getQueryDetails("runnerlist");
            for (Map.Entry<String, Object> mapdata : queryDetails.entrySet()) {
                ResultSet resultSet = st.executeQuery((String) mapdata.getValue());
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int columns = resultSetMetaData.getColumnCount();
                HashMap<String, Object> rowDatas;
                ArrayList<HashMap<String, Object>> testDataList = new ArrayList();
                while (resultSet.next()) {
                    rowDatas = new HashMap(columns);
                    for (int i = 1; i <= columns; ++i) {
                        rowDatas.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
                    }
                    testDataList.add(rowDatas);
                }
                finalTestList.put(mapdata.getKey(), testDataList);
            }
            testRunnerHashMap.put(FrameworkConstants.getRunmanager() ,finalTestList);
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FrameworkConstants.getTestCaseJsonPath()), testRunnerHashMap);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();

        } catch (JsonGenerationException e) {
            e.printStackTrace();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

