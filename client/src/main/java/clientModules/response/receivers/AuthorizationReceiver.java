package clientModules.response.receivers;

import clientModules.connection.DataTransferConnectionModule;
import clientModules.request.sender.RequestSender;
import clientModules.response.handlers.AuthorizationHandler;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import requests.AuthorizationRequest;
import response.responses.AuthorizationResponse;
import response.responses.Response;

import java.io.IOException;

public class AuthorizationReceiver {
    private final DataTransferConnectionModule dataTransferConnectionModule;

    public AuthorizationReceiver(DataTransferConnectionModule dataTransferConnectionModule) {
        this.dataTransferConnectionModule = dataTransferConnectionModule;
    }

    public boolean authorize(String login, char[] password) throws ServerUnavailableException, ResponseTimeoutException, IOException {
        AuthorizationRequest authorizationRequest = new AuthorizationRequest(login, password);
        Response response = new RequestSender().sendRequest(dataTransferConnectionModule, authorizationRequest);

        if (!(response instanceof AuthorizationResponse authorizationResponse)) {
            System.out.println("Received invalid response from server");
        } else {
            return new AuthorizationHandler().handleResponse(authorizationResponse);
        }

        return false;
    }

}
