package org.gcit.listeners;

import org.gcit.utils.DataProviderUtils;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
/**
 * Implements {@link org.testng.IAnnotationTransformer} to leverage certain functionality like updating the annotations of test
 * methods at runtime.
 *
 * @date 2024-07-02
 * @author Pon Vignesh K
 * @version 1.0
 * @since 1.0<br>
 * @see org.gcit.utils.DataProviderUtils
 */

public class AnnotationTransformer implements IAnnotationTransformer{
    /**
     * Assists in setting the dataProvider, dataProvider class, and retry analyzer annotations for all the test methods at runtime.
     */
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        annotation.setDataProvider("getTestData");
        annotation.setDataProviderClass(DataProviderUtils.class);
        annotation.setRetryAnalyzer(RetryFailedTests.class);
    }
}
