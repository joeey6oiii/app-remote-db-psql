package commandsModule.commandReceivers.authenticationReceivers;

import clientModules.connection.DataTransferConnectionModule;
import clientModules.request.sender.RequestSender;
import clientModules.response.handlers.authenticationHandlers.RegistrationHandler;
import clientModules.response.handlers.ServerErrorResultHandler;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import requests.RegistrationRequest;
import response.data.AuthenticationData;
import response.responses.ErrorResponse;
import response.responses.RegistrationResponse;
import response.responses.Response;

import java.io.IOException;

public class RegistrationReceiver {
    DataTransferConnectionModule dataTransferConnectionModule;

    public RegistrationReceiver(DataTransferConnectionModule dataTransferConnectionModule) {
        this.dataTransferConnectionModule = dataTransferConnectionModule;
    }

    public boolean register(String login, char[] password) throws ServerUnavailableException, ResponseTimeoutException, IOException {
        RegistrationRequest registrationRequest = new RegistrationRequest(new AuthenticationData(login, password));
        Response response = new RequestSender(dataTransferConnectionModule).sendRequest(registrationRequest);

        if (response instanceof ErrorResponse errResponse) {
            new ServerErrorResultHandler().handleResponse(errResponse);
        } else if (!(response instanceof RegistrationResponse registrationResponse)) {
            System.out.println("Received invalid response from server");
        } else {
            return new RegistrationHandler().handleResponse(registrationResponse);
        }

        return false;
    }
}