package org.gcit.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.gcit.constants.FrameworkConstants;
import org.gcit.enums.CategoryType;

import java.awt.*;
import java.io.*;
import java.util.Objects;

public final class ExtentReport {
    private static ExtentReports extentReports;

    private ExtentReport() {

    }
    /**
     * Sets the initial configuration for the Extent Reports and determines the report generation path.
     * @param classname Parameterized from the listener class.
     */
    public static void initReports(String classname) {
        if (Objects.isNull(extentReports)) {
            extentReports = new ExtentReports();
            FrameworkConstants.setReportClassName(classname);
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(FrameworkConstants.getReportPath());
            extentReports.attachReporter(sparkReporter);
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setEncoding("utf-8");
            sparkReporter.config().setDocumentTitle(classname + "_Automation Report");
            sparkReporter.config().setReportName("API Automation Testing Report");
        }
    }
    /**
     * Flushing the reports ensures that extent logs are reflected properly.
     * Opens the report in the default desktop browser.
     * Sets the ThreadLocal variable to the default value.
     */
    public static void flushReports() throws IOException {
        if (Objects.nonNull(extentReports)) {
            extentReports.flush();
        }
        ExtentManager.unloadExtentTest();
        Desktop.getDesktop().browse(new File(FrameworkConstants.getReportPath()).toURI());
    }
    /**
     * Creates a test node in the Extent report. Delegates to {@link ExtentManager} for providing thread safety.
     * @param testcasename Test Name that needs to be reflected in the report.
     */
    public static void createTest(String testcasename) {
        ExtentManager.setExtentTest(extentReports.createTest(testcasename));
    }

    public static void addAuthors(String[] authors) {
        for (String temp : authors) {
            ExtentManager.getExtentTest().assignAuthor(temp);
        }
    }

    public static void addCategories(CategoryType[] categories) {

        for (CategoryType temp : categories) {
            ExtentManager.getExtentTest().assignAuthor(temp.toString());
        }

    }

}
