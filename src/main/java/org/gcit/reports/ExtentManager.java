package org.gcit.reports;

import com.aventstack.extentreports.ExtentTest;

public final class ExtentManager {
    /**
     * The ExtentManager class helps achieve thread safety for the {@link com.aventstack.extentreports.ExtentTest} instance.
     */
    private ExtentManager(){

    }
    private static  ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();
    /**
     * Returns the thread-safe {@link com.aventstack.extentreports.ExtentTest} instance fetched from the ThreadLocal variable.
     * @return Thread-safe {@link com.aventstack.extentreports.ExtentTest} instance.
     */
    static ExtentTest getExtentTest() {
        return extentTestThreadLocal.get();
    }
    /**
     * Sets the {@link com.aventstack.extentreports.ExtentTest} instance to the thread-local variable.
     * @param test {@link com.aventstack.extentreports.ExtentTest} instance that needs to be saved from thread safety issues.<p>
     */
    static void setExtentTest(ExtentTest test) {
        extentTestThreadLocal.set(test);
    }
    /**
     * Calling the remove method on the ThreadLocal variable ensures setting the default value for the ThreadLocal variable.
     * It is much safer than assigning a null value to the ThreadLocal variable.
     */
    static void unloadExtentTest() {
        extentTestThreadLocal.remove();
    }
}
