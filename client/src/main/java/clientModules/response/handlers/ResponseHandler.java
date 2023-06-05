package clientModules.response.handlers;

import response.responses.Response;

/**
 * An interface for all response handler-implementers.
 */

public interface ResponseHandler<T extends Response> {

    /**
     * A method that handles the received responses.
     *
     * @param response the received response
     */

    void handleResponse(T response);

}
