package clientModules.response.visitor;

import clientModules.connection.DataTransferConnectionModule;
import clientModules.response.handlers.ClientCommandsHandler;
import clientModules.response.handlers.ExecutionResultHandler;
import clientModules.response.handlers.ServerErrorResultHandler;
import clientModules.response.handlers.authenticationHandlers.AuthorizationHandler;
import clientModules.response.handlers.authenticationHandlers.RegistrationHandler;
import response.responses.*;
import response.visitor.ResponseVisitor;

public class ResponseHandlerVisitor implements ResponseVisitor {

    /**
     * Visits an ErrorResponse and handles the response with a ServerErrorResultHandler.
     *
     * @param  response  the ErrorResponse to be visited
     * @return           true if the response is handled successfully, false otherwise
     */
    @Override
    public boolean visit(ErrorResponse response) {
        return new ServerErrorResultHandler().handleResponse(response);
    }

    /**
     * Visits a ClientCommandsResponse and handles the response using a ClientCommandsHandler.
     *
     * @param  response  the ClientCommandsResponse to be visited
     * @return           true if the response is handled successfully, false otherwise
     */
    @Override
    public boolean visit(ClientCommandsResponse response) {
        return new ClientCommandsHandler().handleResponse(response);
    }

    /**
     * A description of the entire Java function.
     *
     * @param  response	CommandExecutionResponse object to visit
     * @return         	true if the response is successfully handled, false otherwise
     */
    @Override
    public boolean visit(CommandExecutionResponse response) {
        return new ExecutionResultHandler().handleResponse(response);
    }

    /**
     * Visits an AuthorizationResponse and handles it using an AuthorizationHandler.
     *
     * @param  response  the AuthorizationResponse to be visited
     * @return           true if the response was successfully handled, false otherwise
     */
    @Override
    public boolean visit(AuthorizationResponse response) {
        return new AuthorizationHandler().handleResponse(response);
    }

    /**
     * Visits a RegistrationResponse and handles it using an RegistrationHandler.
     *
     * @param  response  the RegistrationResponse to be visited
     * @return           true if the response was successfully handled, false otherwise
     */
    @Override
    public boolean visit(RegistrationResponse response) {
        return new RegistrationHandler().handleResponse(response);
    }
}
