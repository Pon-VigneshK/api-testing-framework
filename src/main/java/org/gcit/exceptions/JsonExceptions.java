package org.gcit.exceptions;

@SuppressWarnings("serial")
public class JsonExceptions extends BaseException {
    /**
     * Pass the message that needs to be appended to the stack-trace
     * @param message Details about the exception or custom message
     */
    public JsonExceptions(String message) {
        super(message);
    }
    /**
     * Pass the message that needs to be appended to the stack-trace
     * @param message Details about the exception or custom message
     */

    public JsonExceptions(String message, Throwable cause) {
        super(message, cause);
    }
}
