package clientModules.response.reader;

import exceptions.ServerUnavailableException;

import java.io.IOException;

/**
 * An interface for all response reader-implementers.
 *
 * @param <T> concrete response
 */

public interface ResponseReadAble<T> {

    /**
     * A method that reads response of the T type from the received data byte array.
     *
     * @param data response to read
     * @throws IOException if failed during I/O operations
     * @throws ClassNotFoundException when could not create response object after deserialization
     * @throws ServerUnavailableException if server is unavailable
     * @return T type of the response
     */

    T readResponse(byte[] data) throws IOException, ClassNotFoundException, ServerUnavailableException;

}
