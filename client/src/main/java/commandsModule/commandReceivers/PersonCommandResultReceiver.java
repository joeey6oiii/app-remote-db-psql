package commandsModule.commandReceivers;

import clientModules.authentication.AuthenticationManager;
import clientModules.connection.DataTransferConnectionModule;
import clientModules.request.sender.RequestSender;
import clientModules.response.handlers.ExecutionResultHandler;
import clientModules.response.handlers.ServerErrorResultHandler;
import clientModules.authentication.User;
import commands.CommandDescription;
import commandsModule.commandsManagement.CommandHandler;
import model.Person;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import objectBuilder.PersonObjectBuilder;
import requests.ObjectArgumentCommandExecutionRequest;
import response.responses.AuthorizationResponse;
import response.responses.CommandExecutionResponse;
import response.responses.ErrorResponse;
import response.responses.Response;

import java.io.IOException;
import java.io.StreamCorruptedException;

/**
 * A class that represents the person single argument command execution result receiver.
 */
public class PersonCommandResultReceiver implements CommandReceiver {
    private final DataTransferConnectionModule dataTransferConnectionModule;

    public PersonCommandResultReceiver(DataTransferConnectionModule dataTransferConnectionModule) {
        this.dataTransferConnectionModule = dataTransferConnectionModule;
    }

    /**
     * A method that receives the simplified uncommon argument command, sends request to a server, gets response
     * and calls the {@link ExecutionResultHandler#handleResponse(CommandExecutionResponse)} method.
     *
     * @param command simplified command
     * @param args simplified command arguments
     */
    @Override
    public void receiveCommand(CommandDescription command, String[] args) {
        Person builtPerson = new PersonObjectBuilder().buildObject();

        ObjectArgumentCommandExecutionRequest<Person> commandRequest =
                new ObjectArgumentCommandExecutionRequest<>(User.getInstance().getToken(), command, args, builtPerson);
        Response response;
        try {
            response = new RequestSender(dataTransferConnectionModule).sendRequest(commandRequest);
            boolean isSuccess = false;

            if (response instanceof ErrorResponse errResponse) {
                new ServerErrorResultHandler().handleResponse(errResponse);
            } else if (response instanceof CommandExecutionResponse executionResponse) {
                isSuccess = new ExecutionResultHandler().handleResponse(executionResponse);
            } else if (response instanceof AuthorizationResponse authorizationResponse && !authorizationResponse.isSuccess()) {
                new AuthenticationManager(dataTransferConnectionModule).authenticateFromInput();
            } else {
                System.out.println("Received invalid response from server");
            }

            if (isSuccess) {
                CommandHandler.getMissedCommands().remove(command, args);
            } else {
                CommandHandler.getMissedCommands().put(command, args);
            }

        } catch (StreamCorruptedException | ServerUnavailableException | ResponseTimeoutException e) {
            CommandHandler.getMissedCommands().put(command, args);
        } catch (IOException e) {
            System.out.println("Something went wrong during I/O operations");
        } catch (NullPointerException e) {
            System.out.println("Empty response received");
            CommandHandler.getMissedCommands().put(command, args);
        }
    }
}