package commandsModule.commandReceivers.authenticationReceivers;

import clientModules.connection.DataTransferConnectionModule;
import clientModules.request.sender.RequestAble;
import clientModules.request.sender.RequestSender;
import clientModules.response.visitor.ResponseHandlerVisitor;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import requests.AuthorizationRequest;
import response.data.AuthenticationData;
import response.responses.AuthorizationResponse;
import response.responses.Response;
import response.visitor.ResponseVisitor;

import java.io.IOException;

public class AuthorizationReceiver {
    private final RequestAble requestSender;
    private final ResponseVisitor responseVisitor;

    public AuthorizationReceiver(DataTransferConnectionModule dataTransferConnectionModule) {
        this.requestSender = new RequestSender(dataTransferConnectionModule);
        this.responseVisitor = new ResponseHandlerVisitor();
    }

    public boolean authorize(String login, char[] password) throws ServerUnavailableException, ResponseTimeoutException, IOException {
        AuthorizationRequest authorizationRequest = new AuthorizationRequest(new AuthenticationData(login, password));
        Response response = requestSender.sendRequest(authorizationRequest);

        if (response.getClass().isAssignableFrom(AuthorizationResponse.class)) {
            return response.accept(responseVisitor);
        } else {
            response.accept(responseVisitor);
        }

        return false;
    }
}