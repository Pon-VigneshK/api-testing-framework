package org.gcit.exceptions;
@SuppressWarnings("serial")
public class InvalidFilepathException extends BaseException {
    /**
     * Pass the message that needs to be appended to the stack-trace
     * @param message Details about the exception or custom message
     */
    public InvalidFilepathException(String message) {
        super(message);
    }
    /**
     * Pass the message that needs to be appended to the stack-trace
     * @param message Details about the exception or custom message
     */
    public InvalidFilepathException(String message, Throwable cause) {
        super(message, cause);
    }
}
