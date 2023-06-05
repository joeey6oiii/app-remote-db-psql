package exceptions;

/**
 * A class that represents an exception when the client could not send data to the server or receive data from the server.
 */

public class ServerUnavailableException extends Exception {

    /**
     * Can be thrown if there is a suspicion about server unavailability.
     *
     * @param message short explanation of the exception
     */

    public ServerUnavailableException(String message) {
        super(message);
    }

}
