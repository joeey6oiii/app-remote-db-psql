package exceptions;

/**
 * A class that represents an exception when the manager contains an invalid argument.
 */

public class IllegalManagerArgumentException extends Exception {

    /**
     * Can be thrown if a manager contains an invalid argument.
     *
     * @param message short explanation of the exception
     */

    public IllegalManagerArgumentException(String message) {
        super(message);
    }
}
