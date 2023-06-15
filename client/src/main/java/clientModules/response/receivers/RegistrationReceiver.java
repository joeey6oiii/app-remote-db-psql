package clientModules.response.receivers;

import clientModules.connection.DataTransferConnectionModule;
import clientModules.request.sender.RequestSender;
import clientModules.response.handlers.RegistrationHandler;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import requests.RegistrationRequest;
import response.responses.RegistrationResponse;
import response.responses.Response;

import java.io.IOException;

public class RegistrationReceiver {
    DataTransferConnectionModule dataTransferConnectionModule;

    public RegistrationReceiver(DataTransferConnectionModule dataTransferConnectionModule) {
        this.dataTransferConnectionModule = dataTransferConnectionModule;
    }

    public boolean register(String login, char[] password) throws ServerUnavailableException, ResponseTimeoutException, IOException {
        RegistrationRequest registrationRequest = new RegistrationRequest(login, password);
        Response response = new RequestSender().sendRequest(dataTransferConnectionModule, registrationRequest);

        if (!(response instanceof RegistrationResponse registrationResponse)) {
            System.out.println("Received invalid response from server");
        } else {
            return new RegistrationHandler().handleResponse(registrationResponse);
        }

        return false;
    }

}
