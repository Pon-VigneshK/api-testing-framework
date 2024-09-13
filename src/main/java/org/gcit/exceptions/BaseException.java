package org.gcit.exceptions;
/**
 * BaseException for the framework. Extends Runtime Exception and can be thrown anywhere to terminate a program
 *
 *
 * @author Pon Vignesh K
 * @date 2024-07-02
 * @version 1.0
 * @since 1.0
 * */
@SuppressWarnings("serial")
public class BaseException extends RuntimeException {
    /**
     * Pass the message that needs to be appended to the stack-trace
     * @param message Details about the exception or custom message
     */
    public BaseException(String message) {
        super(message);
    }

    /**
     * Pass the message that needs to be appended to the stack-trace
     * @param message Details about the exception or custom message
     */
    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
