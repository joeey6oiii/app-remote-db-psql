package commandsModule.commandReceivers;

import clientModules.connection.DataTransferConnectionModule;
import clientModules.request.sender.RequestAble;
import clientModules.request.sender.RequestSender;
import clientModules.response.handlers.ExecutionResultHandler;
import clientModules.authentication.User;
import clientModules.response.visitor.ResponseHandlerVisitor;
import commands.CommandDescription;
import commandsModule.commandsManagement.CommandHandler;
import model.Person;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import objectBuilder.PersonObjectBuilder;
import requests.ObjectArgumentCommandExecutionRequest;
import response.responses.CommandExecutionResponse;
import response.responses.Response;
import response.visitor.ResponseVisitor;

import java.io.IOException;
import java.io.StreamCorruptedException;

/**
 * A class that represents the person single argument command execution result receiver.
 */
public class PersonCommandResultReceiver implements CommandReceiver {
    private final RequestAble requestSender;
    private final ResponseVisitor responseVisitor;

    public PersonCommandResultReceiver(DataTransferConnectionModule dataTransferConnectionModule) {
        this.requestSender = new RequestSender(dataTransferConnectionModule);
        this.responseVisitor = new ResponseHandlerVisitor();
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
            response = requestSender.sendRequest(commandRequest);

            if (response.getClass().isAssignableFrom(CommandExecutionResponse.class)) {
                response.accept(responseVisitor);
                CommandHandler.getMissedCommandsMap().remove(command, args);
            } else {
                response.accept(responseVisitor);
                CommandHandler.getMissedCommandsMap().put(command, args);
            }
        } catch (StreamCorruptedException | ServerUnavailableException
                 | ResponseTimeoutException | NullPointerException e) {
            CommandHandler.getMissedCommandsMap().put(command, args);
        } catch (IOException e) {
            System.out.println("Something went wrong during I/O operations");
        }
    }
}