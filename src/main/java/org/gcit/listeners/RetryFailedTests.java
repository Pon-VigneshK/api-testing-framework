package org.gcit.listeners;

import org.gcit.enums.ConfigProperties;
import org.gcit.utils.PropertyUtils;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryFailedTests implements IRetryAnalyzer {
    /**
     * Implements {@link IRetryAnalyzer}.<p>
     * Assists in rerunning the failed tests.<p>
     * @date 2024-07-02
     * @author Pon Vignesh K
     * @version 1.0
     * @since 1.0<br>
     */
    private int count = 0;
    private int retries = Integer.parseInt(PropertyUtils.getValue(ConfigProperties.RETRYATTEMPTS));;
    /**
     * Returns true when a retry is needed and false otherwise.
     * Maximum retries allowed is one time.
     * Retry will occur if the user desires and has set the value in the property file.
     */

    @Override
    public boolean retry(ITestResult result) {
        boolean state = false;
        if (PropertyUtils.getValue(ConfigProperties.RETRY).equalsIgnoreCase("yes")) {
            state = count < retries;
            count++;
            return state;
        } else {
            return state;
        }
    }
}
