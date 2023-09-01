package clientModules.authentication;

import clientModules.connection.DataTransferConnectionModule;
import commandsModule.commandReceivers.authenticationReceivers.AuthorizationReceiver;
import commandsModule.commandReceivers.authenticationReceivers.RegistrationReceiver;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import exceptions.UnsupportedConsoleException;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;

import java.io.Console;
import java.io.IOException;

public class AuthenticationManager {
    private final DataTransferConnectionModule dataTransferConnectionModule;
    private final LineReader lineReader;

    public AuthenticationManager(DataTransferConnectionModule dataTransferConnectionModule, Terminal terminal) {
        this.dataTransferConnectionModule = dataTransferConnectionModule;
        lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(new StringsCompleter("reg", "log", "exit"))
                .parser(new DefaultParser())
                .build();
    }

    public int authenticateFromInput() throws ResponseTimeoutException, ServerUnavailableException, IOException {
        try {
            String prompt = "Would you like to register or log in? [reg/log/exit]\n$ ";
            String input = lineReader.readLine(prompt);

            while (true) {
                switch (input) {
                    case "exit" -> {
                        return 2;
                    }
                    case "reg" -> {
                        return handleRegistration(lineReader);
                    }
                    case "log" -> {
                        return handleLogin(lineReader);
                    }
                }

                input = lineReader.readLine("Invalid input. " + prompt);
            }
        } catch (EndOfFileException e) {
            return 2;
        }
    }

    private int handleRegistration(LineReader lineReader) throws ResponseTimeoutException, ServerUnavailableException, IOException {
        String login = handleInput(lineReader, "login", false);
        if (login.equalsIgnoreCase("exit")) {
            return 2;
        }

        String password = handleInput(lineReader, "password", true);
        if (password.equalsIgnoreCase("exit")) {
            return 2;
        }

        boolean isRegistered = new RegistrationReceiver(dataTransferConnectionModule).register(login, password.toCharArray());

        return isRegistered ? 1 : 0;
    }

    private int handleLogin(LineReader lineReader) throws ResponseTimeoutException, ServerUnavailableException, IOException {
        String login = handleInput(lineReader, "login", false);
        if (login.equalsIgnoreCase("exit")) {
            return 2;
        }

        String password = handleInput(lineReader, "password", true);
        if (password.equalsIgnoreCase("exit")) {
            return 2;
        }

        boolean isLogged = new AuthorizationReceiver(dataTransferConnectionModule).authorize(login, password.toCharArray());

        return isLogged ? 1 : 0;
    }

    private String handleInput(LineReader lineReader, String param, boolean secureInput) {
        lineReader.printAbove("You can enter \"exit\" to shut down the program");

        String prompt = "Enter " + param + ":\n$ ";

        String input;
        do {
            if (secureInput) {
                try {
                    input = this.readSecureInput(prompt);
                } catch (UnsupportedConsoleException e) {
                    input = lineReader.readLine(prompt);
                }
            } else {
                input = lineReader.readLine(prompt);
            }
        } while (input.isEmpty());

        return input;
    }

    // doesn't work in IDE
    private String readSecureInput(String prompt) throws UnsupportedConsoleException {
        Console console = System.console();
        if (console == null) {
            throw new UnsupportedConsoleException("Console input is not supported in this environment");
        }

        return new String(console.readPassword(prompt));
    }
}