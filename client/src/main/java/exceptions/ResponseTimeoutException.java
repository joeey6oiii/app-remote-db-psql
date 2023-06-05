package exceptions;

/**
 * A class that represents an exception when the client could not receive data from the server during the given time.
 */

public class ResponseTimeoutException extends Exception {

    /**
     * Can be thrown if there was no response from server during the given time.
     *
     * @param message short explanation of the exception
     */

    public ResponseTimeoutException(String message) {
        super(message);
    }

}
