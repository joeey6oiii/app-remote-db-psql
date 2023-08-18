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

    public boolean visit(ErrorResponse response) {
        return new ServerErrorResultHandler().handleResponse(response);
    }

    public boolean visit(ClientCommandsResponse response) {
        return new ClientCommandsHandler().handleResponse(response);
    }

    public boolean visit(CommandExecutionResponse response) {
        return new ExecutionResultHandler().handleResponse(response);
    }

    public boolean visit(AuthorizationResponse response) {
        return new AuthorizationHandler().handleResponse(response);
    }

    public boolean visit(RegistrationResponse response) {
        return new RegistrationHandler().handleResponse(response);
    }
}
