package clientModules.request.sender;

import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import requests.Request;
import response.responses.Response;

import java.io.IOException;

/**
 * An interface for all request sender-implementers.
 *
 * @param <T> response
 */
public interface RequestAble<T extends Response, V extends Request> {

    /**
     * A method that sends a request of a T type to the server.
     *
     * @param request request
     */
    T sendRequest(V request) throws IOException, ServerUnavailableException, ResponseTimeoutException;
}
