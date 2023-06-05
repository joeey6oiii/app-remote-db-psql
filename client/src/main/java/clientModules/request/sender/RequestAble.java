package clientModules.request.sender;

import clientModules.connection.DataTransferConnectionModule;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import response.responses.Response;

import java.io.IOException;

/**
 * An interface for all request sender-implementers.
 *
 * @param <T> concrete response
 */

public interface RequestAble<T extends Response, V> {

    /**
     * A method that sends a request of a T type to the server.
     *
     * @param module client core
     * @param request concrete request
     */

    T sendRequest(DataTransferConnectionModule module, V request) throws IOException, ServerUnavailableException, ResponseTimeoutException;

}
