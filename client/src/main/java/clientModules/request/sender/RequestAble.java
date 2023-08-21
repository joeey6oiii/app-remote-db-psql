package clientModules.request.sender;

import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import requests.Request;
import response.responses.Response;

import java.io.IOException;

/**
 * An interface for all request sender-implementers.
 */
public interface RequestAble {

    /**
     * A method that sends a request of a T type to the server.
     *
     * @param request request
     */
    Response sendRequest(Request request) throws IOException, ServerUnavailableException, ResponseTimeoutException;
}
