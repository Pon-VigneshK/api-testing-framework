package org.gcit.listeners;

import org.gcit.constants.FrameworkConstants;
import org.gcit.enums.ConfigProperties;
import org.gcit.utils.ExcelUtils;
import org.gcit.utils.JsonUtils;
import org.gcit.utils.PropertyUtils;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Implements {@link org.testng.IMethodInterceptor} to leverage the abstract methods
 * Mostly used to read the data from json and decides on which tests needs to run.
 *
 * <pre>Please make sure to add the listener details in the testng.xml file</pre>
 *
 * @date 2024-07-02
 * @author Pon Vignesh K
 * @version 1.0
 * @since 1.0<br>
 * @see org.gcit.utils.JsonUtils
 */

public class MethodInterceptor  implements IMethodInterceptor {
    private static final Map<String, Integer> invocationCounts = new HashMap<>();
    List<Map<String, Object>> list = new ArrayList<>();

    public static int getInvocation(String methodName) {
        return invocationCounts.getOrDefault(methodName, 1);
    }

    /**
     * Intercepts the existing test methods and changes the annotation values at runtime.
     * Values are fetched from the JSON.
     * The user has to choose yes/no in the Excel executed column.
     * For example, if there are 3 tests named a, b, c that need to be run, it reads the runner list JSON and understands the user wants to
     * run only tests a and c. It then returns the updated list after performing the comparisons.
     */
    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext iTestContext) {
        List<IMethodInstance> results = new ArrayList<>();
        // generate runner list json
        String dataSource = PropertyUtils.getValue(ConfigProperties.DATASOURCE);
        switch (dataSource.toLowerCase()) {
            case "db":
                JsonUtils.generateRunnerListJsonData();
                list = JsonUtils.getTestDetails(FrameworkConstants.getRunmanager());
                break;
            case "excel":
                JsonUtils.generateRunnerListJsonDataFromExcel(FrameworkConstants.getRunmangerExcelSheet());
                list = JsonUtils.getTestDetails(FrameworkConstants.getRunmanager());
                break;
            case "json":
                list = JsonUtils.getTestDetails(FrameworkConstants.getRunmanager());
                break;
            default:
                throw new IllegalArgumentException("Unknown data source: " + dataSource);
        }

        for (IMethodInstance methodInstance : methods) {
            for (Map<String, Object> testData : list) {
                if (methodInstance.getMethod().getMethodName().equalsIgnoreCase(String.valueOf(testData.get("testcasename"))) &&
                        String.valueOf(testData.get("execute")).equalsIgnoreCase("yes")) {
                    methodInstance.getMethod().setDescription(String.valueOf(testData.get("testdescription")));
                    methodInstance.getMethod().setPriority(Integer.parseInt(String.valueOf(testData.get("priority"))));
                    int count = Integer.parseInt(String.valueOf(testData.get("count")));
                    invocationCounts.put(methodInstance.getMethod().getMethodName(), count);
                    results.add(methodInstance);
                    break;
                }
            }
        }
        return results;
    }
}
