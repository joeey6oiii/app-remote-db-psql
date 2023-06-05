package serverModules.request.reader;

import java.io.IOException;

/**
 * An interface for all request reader-implementers.
 *
 * @param <T> concrete request
 */

public interface RequestReadAble<T> {

    /**
     * A method that reads request of the T type from the received data byte array.
     *
     * @param data request to read
     * @throws IOException if failed during I/O operations
     * @throws ClassNotFoundException when could not create request object after deserialization
     * @return T type of the request
     */

    T readRequest(byte[] data) throws IOException, ClassNotFoundException;

}
