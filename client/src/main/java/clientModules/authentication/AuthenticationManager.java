package clientModules.authentication;

import clientModules.connection.DataTransferConnectionModule;
import clientModules.response.receivers.authenticationReceivers.AuthorizationReceiver;
import clientModules.response.receivers.authenticationReceivers.RegistrationReceiver;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;

import java.io.IOException;
import java.util.Scanner;

public class AuthenticationManager {
    private final DataTransferConnectionModule dataTransferConnectionModule;
    private final Scanner scanner = new Scanner(System.in);

    public AuthenticationManager(DataTransferConnectionModule dataTransferConnectionModule) {
        this.dataTransferConnectionModule = dataTransferConnectionModule;
    }

    public int authenticateFromInput() throws ResponseTimeoutException, ServerUnavailableException, IOException {
        System.out.print("Would you like to register or log in? [reg/log/exit]\n$ ");

        String input = scanner.nextLine();

        while (true) {
            switch (input) {
                case "exit" -> {
                    return 2;
                }
                case "reg" -> {
                    return handleRegistration();
                }
                case "log" -> {
                    return handleLogin();
                }
            }

            System.out.print("Invalid input. Would you like to register or log in? [reg/log/exit]\n$ ");
            input = scanner.nextLine();
        }
    }

    private int handleRegistration() throws ResponseTimeoutException, ServerUnavailableException, IOException {
        String login = handleInput("login");
        if (login.equalsIgnoreCase("exit")) {
            return 2;
        }

        String password = handleInput("password");
        if (password.equalsIgnoreCase("exit")) {
            return 2;
        }

        boolean isRegistered = new RegistrationReceiver(dataTransferConnectionModule).register(login, password.toCharArray());

        return isRegistered ? 1 : 0;
    }

    private int handleLogin() throws ResponseTimeoutException, ServerUnavailableException, IOException {
        String login = handleInput("login");
        if (login.equalsIgnoreCase("exit")) {
            return 2;
        }

        String password = handleInput("password");
        if (password.equalsIgnoreCase("exit")) {
            return 2;
        }

        boolean isLogged = new AuthorizationReceiver(dataTransferConnectionModule).authorize(login, password.toCharArray());

        return isLogged ? 1 : 0;
    }

    private String handleInput(String param) {
        System.out.println("You can enter \"exit\" to shut down the program");

        String input;
        do {
            System.out.print("Enter " + param + ":\n$ ");
            input = scanner.nextLine();
        } while (input.isEmpty());

        return input;
    }

}
