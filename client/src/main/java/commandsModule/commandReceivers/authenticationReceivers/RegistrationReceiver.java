package commandsModule.commandReceivers.authenticationReceivers;

import clientModules.connection.DataTransferConnectionModule;
import clientModules.request.sender.RequestAble;
import clientModules.request.sender.RequestSender;
import clientModules.response.visitor.ResponseHandlerVisitor;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import requests.RegistrationRequest;
import response.data.AuthenticationData;
import response.responses.RegistrationResponse;
import response.responses.Response;
import response.visitor.ResponseVisitor;

import java.io.IOException;

public class RegistrationReceiver {
    private final RequestAble requestSender;
    private final ResponseVisitor responseVisitor;

    public RegistrationReceiver(DataTransferConnectionModule dataTransferConnectionModule) {
        this.requestSender = new RequestSender(dataTransferConnectionModule);
        this.responseVisitor = new ResponseHandlerVisitor();
    }

    public boolean register(String login, char[] password) throws ServerUnavailableException, ResponseTimeoutException, IOException {
        RegistrationRequest registrationRequest = new RegistrationRequest(new AuthenticationData(login, password));
        Response response = requestSender.sendRequest(registrationRequest);

        if (response.getClass().isAssignableFrom(RegistrationResponse.class)) {
            return response.accept(responseVisitor);
        } else {
            response.accept(responseVisitor);
        }

        return false;
    }
}