package clientModules.connection;

import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;

import java.io.IOException;

/**
 * An interface for all connection module-implementers with the ability to transfer data.
 */

public interface DataTransferConnectionModule extends ConnectionModule {

    /**
     * A method that receives data.
     *
     * @return data byte array
     * @throws IOException if failed during I/O operations
     * @throws ServerUnavailableException if the server was unavailable during sending and receiving operations
     * @throws ResponseTimeoutException if client could not get response from the server during the given time
     */

    byte[] receiveData() throws IOException, ServerUnavailableException, ResponseTimeoutException;

    /**
     * A method that sends the specified data.
     *
     * @param data data to send
     * @throws IOException if failed during I/O operations
     * @throws ServerUnavailableException if the server was unavailable during sending and receiving operations
     */

    void sendData(byte[] data) throws IOException, ServerUnavailableException;

}
