package org.gcit.exceptions;
@SuppressWarnings("serial")
public class ExcelFileNotFoundException extends InvalidFilepathException {
    /**
     * Pass the message that needs to be appended to the stack-trace
     * @param message Details about the exception or custom message
     */
    public ExcelFileNotFoundException(String message) {
        super(message);
    }
    /**
     * Pass the message that needs to be appended to the stack-trace
     * @param message Details about the exception or custom message
     */
    public ExcelFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
