package org.gcit.exceptions;
@SuppressWarnings("serial")
public class PropertiesFileException extends BaseException {
    /**
     * Pass the message that needs to be appended to the stack-trace
     * @param message Details about the exception or custom message
     */
    public PropertiesFileException(String message) {
        super(message);
    }
    /**
     * Pass the message that needs to be appended to the stack-trace
     * @param message Details about the exception or custom message
     */
    public PropertiesFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
