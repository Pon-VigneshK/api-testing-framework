package org.gcit.listeners;

import org.gcit.utils.DataProviderUtils;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
/**
 * Implements {@link org.testng.IAnnotationTransformer} to leverage specific functionality, such as updating the annotations of test
 * methods at runtime.
 * @see org.gcit.utils.DataProviderUtils
 */

public class AnnotationTransformer implements IAnnotationTransformer{
    /**
     * Assists in setting the dataProvider, dataProvider class, and retry analyzer annotations for all the test methods at runtime.
     */
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
//        annotation.setDataProvider("getData");
//        annotation.setDataProviderClass(DataProviderUtils.class);
//        annotation.setRetryAnalyzer(RetryFailedTests.class);
        annotation.setDataProvider("getJsonTestData");
        annotation.setDataProviderClass(DataProviderUtils.class);
        annotation.setRetryAnalyzer(RetryFailedTests.class);
    }
}
