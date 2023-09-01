package commandsModule.commands;

import clientModules.authentication.AuthenticationManager;
import clientModules.authentication.User;
import clientModules.connection.DataTransferConnectionModule;
import clientModules.request.sender.RequestAble;
import clientModules.request.sender.RequestSender;
import commands.CommandDescription;
import commandsModule.commandsManagement.CommandHandler;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import org.jline.terminal.Terminal;
import requests.CommandExecutionRequest;
import token.Token;

import java.io.IOException;

public class AuthCommand implements ClientCommand {
    private final RequestAble requestSender;
    private final AuthenticationManager authenticationManager;

    public AuthCommand(DataTransferConnectionModule dataTransferConnectionModule, Terminal terminal) {
        this.requestSender = new RequestSender(dataTransferConnectionModule);
        this.authenticationManager = new AuthenticationManager(dataTransferConnectionModule, terminal);
    }

    @Override
    public String getName() {
        return "auth";
    }

    @Override
    public String getDescription() {
        return "Calls the authentication manager to authenticate from input ([reg/log/exit]) and ends current session." +
                " Note: [exit] call here exits authentication manager, not the program";
    }

    @Override
    public void execute() {
        try {
            String[] args = new String[1];
            args[0] = "exit";

            CommandDescription command = CommandHandler.getCommandFromName(args[0]);
            Token<?> token = User.getInstance().getToken();

            if (command != null && token != null && token.getTokenValue() != null) {
                requestSender.sendRequest(new CommandExecutionRequest(token, command, args));
            }

            authenticationManager.authenticateFromInput();
        } catch (ResponseTimeoutException | ServerUnavailableException e) {
            System.out.println("Server is currently unavailable, please try again later");
        } catch (IOException e) {
            System.out.println("Something went wrong during I/O operations");
        }
    }
}
