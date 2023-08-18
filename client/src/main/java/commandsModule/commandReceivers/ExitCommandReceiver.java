package commandsModule.commandReceivers;

import clientModules.connection.DataTransferConnectionModule;
import clientModules.request.sender.RequestAble;
import clientModules.request.sender.RequestSender;
import clientModules.response.handlers.ExitCommandHandler;
import clientModules.authentication.User;
import clientModules.response.visitor.ResponseHandlerVisitor;
import commands.CommandDescription;
import commandsModule.commandsManagement.CommandHandler;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import requests.CommandExecutionRequest;
import requests.Request;
import response.responses.CommandExecutionResponse;
import response.responses.Response;
import response.visitor.ResponseVisitor;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.Scanner;

/**
 * A class that represents the exit command receiver.
 */
public class ExitCommandReceiver implements CommandReceiver {
    private final RequestAble<Response, Request> requestSender;
    private final ResponseVisitor responseVisitor;

    public ExitCommandReceiver(DataTransferConnectionModule dataTransferConnectionModule) {
        this.requestSender = new RequestSender(dataTransferConnectionModule);
        this.responseVisitor = new ResponseHandlerVisitor();
    }

    /**
     * A method that receives the simplified "exit" command, sends request to a server,
     * gets response and calls the {@link ExitCommandHandler#handleResponse(CommandExecutionResponse)})} method.
     *
     * @param command simplified command
     * @param args simplified command arguments
     */
    @Override
    public void receiveCommand(CommandDescription command, String[] args) {
        if (!this.confirmExit()) {
            CommandHandler.getMissedCommands().remove(command, args);
            return;
        }

        CommandExecutionRequest commandRequest = new CommandExecutionRequest(User.getInstance().getToken(), command, args);
        Response response;
        try {
            response = requestSender.sendRequest(commandRequest);

            if (response.getClass().isAssignableFrom(CommandExecutionResponse.class)) {
                new ExitCommandHandler().handleResponse((CommandExecutionResponse) response);
                CommandHandler.getMissedCommands().remove(command, args);
            } else {
                response.accept(responseVisitor);
                CommandHandler.getMissedCommands().put(command, args);
            }
        } catch (StreamCorruptedException | ServerUnavailableException
                 | ResponseTimeoutException | NullPointerException e) {
            CommandHandler.getMissedCommands().put(command, args);
        } catch (IOException e) {
            System.out.println("Something went wrong during I/O operations");
        }
    }

    private boolean confirmExit() {
        System.out.print("Are you sure you want to exit? [Y/N]\n$ ");
        Scanner consoleInputReader = new Scanner(System.in);
        String consoleInput;

        while (!(consoleInput = consoleInputReader.nextLine()).equalsIgnoreCase("Y")) {
            if (consoleInput.equalsIgnoreCase("N")) {
                System.out.println("Returning to the console input");
                return false;
            }
            System.out.print("$ ");
        }

        return true;
    }
}