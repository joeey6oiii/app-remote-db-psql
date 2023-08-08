package commandsModule.receivers;

import clientModules.authentication.AuthenticationManager;
import clientModules.connection.DataTransferConnectionModule;
import clientModules.request.sender.RequestSender;
import clientModules.response.handlers.ExecutionResultHandler;
import clientModules.response.handlers.ServerErrorResultHandler;
import clientModules.response.handlers.authenticationHandlers.User;
import commands.CommandDescription;
import commandsModule.commandsManagement.CommandHandler;
import commandsModule.commandsManagement.CommandManager;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import org.apache.commons.io.IOUtils;
import requests.CommandExecutionRequest;
import response.responses.AuthorizationResponse;
import response.responses.CommandExecutionResponse;
import response.responses.ErrorResponse;
import response.responses.Response;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

/**
 * A class that represents the script command receiver.
 */
public class ScriptCommandReceiver implements CommandReceiver {
    private static final LinkedList<String> historyOfDangerScript = new LinkedList<>();

    /**
     * A method that receives the simplified "execute_script" command, parses file, sends request to a server,
     * gets response and calls the {@link ExecutionResultHandler#handleResponse(CommandExecutionResponse)} method.
     * After, iterates through <code>String[]</code> of commands from the parsed file
     * and calls the {@link CommandManager#manageCommand(CommandDescription, String[], DataTransferConnectionModule)} method.
     * Stores "history of danger scripts" collection to avoid looping
     *
     * @param scriptCommand simplified command
     * @param args simplified command arguments
     * @param dataTransferConnectionModule client core
     */
    @Override
    public void receiveCommand(CommandDescription scriptCommand, String[] args, DataTransferConnectionModule dataTransferConnectionModule) {
        if (historyOfDangerScript.contains(args[0])) {
            System.out.println("Detected dangerous command: the script will loop if the command is executed\n" +
                    "Continuing executing script from the next command...");
            historyOfDangerScript.clear();
            return;
        }

        if (args.length < 2) {
            System.out.println("Not enough arguments. Returning to the console input");
            return;
        }

        File script = new File(args[1]);
        if (!script.exists()) {
            System.out.println("File not found. Returning to the console input");
            return;
        }

        CommandManager commandManager = new CommandManager();
        try (InputStream inputStream = new FileInputStream(script)) {
            boolean isSuccess = false;
            try {
                RequestSender requestSender = new RequestSender();
                CommandExecutionRequest commandRequest = new CommandExecutionRequest(User.getToken(), scriptCommand, args);

                Response response = requestSender.sendRequest(dataTransferConnectionModule, commandRequest);

                if (response instanceof ErrorResponse errResponse) {
                    new ServerErrorResultHandler().handleResponse(errResponse);
                } else if (response instanceof CommandExecutionResponse executionResponse) {
                    isSuccess = new ExecutionResultHandler().handleResponse(executionResponse);
                } else if (response instanceof AuthorizationResponse authorizationResponse && !authorizationResponse.isSuccess()) {
                    new AuthenticationManager(dataTransferConnectionModule).authenticateFromInput();
                } else {
                    System.out.println("Received invalid response from server");
                }

            } catch (StreamCorruptedException | ServerUnavailableException | ResponseTimeoutException ignored) {
            } catch (NullPointerException e) {
                System.out.println("Empty response received");
            }

            if (isSuccess) {
                CommandHandler.getMissedCommands().remove(scriptCommand, args);
            } else {
                CommandHandler.getMissedCommands().put(scriptCommand, args);
                return;
            }

            String contents = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            String[] lines = contents.split("\n");
            String[] tokens;
            for (String line : lines) {
                tokens = line.split(" ");

                if (tokens[0].equals("execute_script")) {
                    historyOfDangerScript.add(args[0]);
                }

                CommandDescription command = CommandHandler.getCommandByName(tokens[0].toLowerCase());
                if (command != null) {
                    commandManager.manageCommand(command, tokens, dataTransferConnectionModule);
                } else {
                    System.out.println("Command \"" + tokens[0] + "\" Was Not Recognized as an" +
                            " Internal or External Command\nContinuing executing script...");
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to continue executing script. Returning to the console input");
        }

        historyOfDangerScript.clear();
    }
}