package clientModules.response.receivers;

import clientModules.Client;
import clientModules.authentication.AuthenticationManager;
import clientModules.connection.DataTransferConnectionModule;
import clientModules.request.sender.RequestSender;
import clientModules.response.handlers.ExecutionResultHandler;
import commands.CommandDescription;
import commandsModule.handler.CommandHandler;
import defaultClasses.Person;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import objectBuilder.PersonBuilder;
import requests.SingleArgumentCommandExecutionRequest;
import response.responses.AuthorizationResponse;
import response.responses.CommandExecutionResponse;
import response.responses.Response;

import java.io.IOException;
import java.io.StreamCorruptedException;

/**
 * A class that represents the person single argument command execution result receiver.
 */

public class PersonCommandResultReceiver implements CommandReceiver {

    /**
     * A method that receives the simplified uncommon argument command, sends request to a server, gets response
     * and calls the {@link ExecutionResultHandler#handleResponse(CommandExecutionResponse)} method.
     *
     * @param command simplified command
     * @param args simplified command arguments
     * @param dataTransferConnectionModule client core
     */

    @Override
    public void receiveCommand(CommandDescription command, String[] args, DataTransferConnectionModule dataTransferConnectionModule) {
        Person builtPerson = new PersonBuilder().buildObject();

        SingleArgumentCommandExecutionRequest<Person> commandRequest = new SingleArgumentCommandExecutionRequest<>(Client.getLogin(), Client.getPassword(), command, args, builtPerson);
        Response response;
        try {
            response = new RequestSender().sendRequest(dataTransferConnectionModule, commandRequest);

            if(response instanceof CommandExecutionResponse executionResponse) {
                new ExecutionResultHandler().handleResponse(executionResponse);

                CommandHandler.getMissedCommands().remove(command, args);
            } else if(response instanceof AuthorizationResponse authorizationResponse && !authorizationResponse.isSuccess()) {
                new AuthenticationManager(dataTransferConnectionModule).authenticateFromInput();
                CommandHandler.getMissedCommands().put(command, args);
            } else {
                System.out.println("Received invalid response from server");
                CommandHandler.getMissedCommands().put(command, args);
            }

        } catch (StreamCorruptedException | ServerUnavailableException | ResponseTimeoutException e) {
            CommandHandler.getMissedCommands().put(command, args);
        } catch (IOException e) {
            System.out.println("Something went wrong during I/O operations");
        } catch (NullPointerException e) {
            System.out.println("Empty response received");
        }
    }

}
