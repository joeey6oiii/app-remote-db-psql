package commandsModule.commandReceivers.authenticationReceivers;

import clientModules.connection.DataTransferConnectionModule;
import clientModules.request.sender.RequestSender;
import clientModules.response.handlers.authenticationHandlers.AuthorizationHandler;
import clientModules.response.handlers.ServerErrorResultHandler;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import requests.AuthorizationRequest;
import response.data.AuthenticationData;
import response.responses.AuthorizationResponse;
import response.responses.ErrorResponse;
import response.responses.Response;

import java.io.IOException;

public class AuthorizationReceiver {
    private final DataTransferConnectionModule dataTransferConnectionModule;

    public AuthorizationReceiver(DataTransferConnectionModule dataTransferConnectionModule) {
        this.dataTransferConnectionModule = dataTransferConnectionModule;
    }

    public boolean authorize(String login, char[] password) throws ServerUnavailableException, ResponseTimeoutException, IOException {
        AuthorizationRequest authorizationRequest = new AuthorizationRequest(new AuthenticationData(login, password));
        Response response = new RequestSender(dataTransferConnectionModule).sendRequest(authorizationRequest);

        if (response instanceof ErrorResponse errResponse) {
            new ServerErrorResultHandler().handleResponse(errResponse);
        } else if (!(response instanceof AuthorizationResponse authorizationResponse)) {
            System.out.println("Received invalid response from server");
        } else {
            return new AuthorizationHandler().handleResponse(authorizationResponse);
        }

        return false;
    }
}