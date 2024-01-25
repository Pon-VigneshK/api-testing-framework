package org.gcit.listeners;

import org.gcit.annotations.FrameworkAnnotation;
import org.gcit.reports.ExtentLogger;
import org.gcit.reports.ExtentReport;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.util.Arrays;

/**
 * Implements {@link org.testng.ITestListener} and {@link org.testng.ISuiteListener} to leverage the abstract methods.
 * Mostly used to assist in extent report generation.
 * <pre>Please make sure to add the listener details in the testng.xml file.</pre>
 */
public class ListenerClass implements ITestListener, ISuiteListener {

    /**
     * Initializes the reports with the specified file name.
     * @see org.gcit.reports.ExtentReport
     */
    @Override
    public void onStart(ISuite suite) {
        ExtentReport.initReports(suite.getXmlSuite().getName());
    }

    /**
     * Terminates the reports.
     * @see org.gcit.reports.ExtentReport
     */
    @Override
    public void onFinish(ISuite suite) {
        try {
            ExtentReport.flushReports();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Starts a test node for each TestNG test.
     * @see org.gcit.reports.ExtentReport
     * @see org.gcit.annotations.FrameworkAnnotation
     */
    @Override
    public void onTestStart(ITestResult result) {
        FrameworkAnnotation annotation = result.getMethod().getConstructorOrMethod()
                .getMethod().getAnnotation(FrameworkAnnotation.class);
        ExtentReport.createTest("<span style='color: green;'>TestCase Name:</span>" + result.getMethod().getMethodName() + "<br><span style='color: green;'>Test Description:</span> " + result.getMethod().getDescription());
//        ExtentReport.addAuthors(annotation.author());
//        ExtentReport.addCategories(annotation.category());
    }
    /**
     * Marks the test as pass and logs it in the report.
     * @see org.gcit.reports.ExtentLogger
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentLogger.pass(result.getMethod().getMethodName() + " is passed !!");
    }
    /**
     * Marks the test as fail and logs it in the report.
     * @see org.gcit.reports.ExtentLogger
     */
    @Override
    public void onTestFailure(ITestResult result) {
//        ExtentLogger.fail(result.getMethod().getMethodName() + " is failed !!");
        ExtentLogger.logStackTraceInfoInExtentReport(Arrays.toString(result.getThrowable().getStackTrace()));
        ExtentLogger.fail(result.getThrowable().getMessage());
        ExtentLogger.fail(result.getThrowable().toString());
    }
    /**
     * Marks the test as skip and logs it in the report.
     * @see org.gcit.reports.ExtentLogger
     */

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentLogger.skip(result.getMethod().getMethodName() + " is skipped !!");
    }
}
