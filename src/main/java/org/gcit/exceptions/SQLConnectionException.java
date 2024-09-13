package org.gcit.exceptions;

@SuppressWarnings("serial")
public class SQLConnectionException extends BaseException {
    /**
     * Pass the message that needs to be appended to the stack-trace
     * @param message Details about the exception or custom message
     */
    public SQLConnectionException(String message) {
        super(message);
    }
    /**
     * Pass the message that needs to be appended to the stack-trace
     * @param message Details about the exception or custom message
     */
    public SQLConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
