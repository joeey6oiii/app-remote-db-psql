package commandsModule.commands;

import clientModules.authentication.AuthenticationManager;
import clientModules.connection.DataTransferConnectionModule;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;

import java.io.IOException;

public class AuthCommand implements OfflineCommand {
    private final DataTransferConnectionModule dataTransferConnectionModule;

    public AuthCommand(DataTransferConnectionModule dataTransferConnectionModule) {
        this.dataTransferConnectionModule = dataTransferConnectionModule;

    }

    @Override
    public String getName() {
        return "auth";
    }

    @Override
    public String getDescription() {
        return "Calls the authentication manager to authenticate from input ([reg/log/exit])." +
                " Note: [exit] call here exits authentication manager, not the program";
    }

    @Override
    public void execute() {
        try {
            new AuthenticationManager(dataTransferConnectionModule).authenticateFromInput();
        } catch (ResponseTimeoutException | ServerUnavailableException | IOException e) {
            System.out.println("Server is currently unavailable, please try again later");
        }
    }
}
