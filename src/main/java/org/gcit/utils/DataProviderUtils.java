package org.gcit.utils;

import org.gcit.constants.FrameworkConstants;
import org.gcit.enums.ConfigProperties;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

import static org.gcit.utils.JsonUtils.generateTestDataJson;
import static org.gcit.utils.JsonUtils.getTestDataDetails;

public final class DataProviderUtils {
    private DataProviderUtils() {
    }
    private static List<Map<String, Object>> testDataFromJson = new ArrayList();


    @DataProvider
    public static Object[] getData(Method method) {

        String testcasename = method.getName();
        List<Map<String, String>> list = ExcelUtils.getTestDetails(FrameworkConstants.getDataExcelSheet());
        List<Map<String, String>> iterationlist = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).get("testcasename").equalsIgnoreCase(testcasename)) {
                if (list.get(i).get("execute").equalsIgnoreCase("yes")) {
//                    if (list.get(i).get("enviroment").equalsIgnoreCase(PropertyUtils.getValue(ConfigProperties.ENV))) {
                        iterationlist.add(list.get(i));
                    //System.out.println((list.get(i).get("enviroment").equalsIgnoreCase(PropertyUtils.getValue(ConfigProperties.ENV))));
//                    }
                }
            }
        }
        return iterationlist.toArray();
    }
    @DataProvider(name = "getJsonTestData")
    public static Object[] getJsonData(Method method) {
        String testName = method.getName();
        List<Map<String, Object>> iterationList = new ArrayList();
        if (testDataFromJson.isEmpty()) {
            //get the data from database
            generateTestDataJson();
            //Store the test details in list
            testDataFromJson = getTestDataDetails();
        }


        for (int i = 0; i < testDataFromJson.size(); i++) {
            if (String.valueOf(testDataFromJson.get(i).get("testcasename")).equalsIgnoreCase(testName) &&
                    String.valueOf(testDataFromJson.get(i).get("execute")).equalsIgnoreCase("yes")) {
                iterationList.add(testDataFromJson.get(i));
            }

        }
        Set<Map<String, Object>> set = new HashSet(iterationList);
        iterationList.clear();
        iterationList.addAll(set);
        return iterationList.toArray();
    }
//    @DataProvider(parallel = false)
//    public static Object[] getData(Method m) {
//        String testcasename = m.getName();
//
//        if (list.isEmpty()) {
//            list = ExcelUtils.getTestDetails(FrameworkConstants.getDataExcelSheet());
//            System.out.println(list);
//        }
//
//        List<Map<String, String>> smalllist = new ArrayList<>(list);
//
//        Predicate<Map<String, String>> isTestNameNotMatching = map -> !map.get("testcasename").equalsIgnoreCase(testcasename);
//        Predicate<Map<String, String>> isExecuteColumnNo = map -> map.get("execute").equalsIgnoreCase("no");
//        Predicate<Map<String, String>> isEnvironment = map -> map.get("environment").equalsIgnoreCase(PropertyUtils.getValue(ConfigProperties.ENV));
//
//        smalllist.removeIf(isTestNameNotMatching.or(isExecuteColumnNo).or(isEnvironment));
//        return smalllist.toArray();
//    }

}
