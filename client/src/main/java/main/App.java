package main;

import clientModules.authentication.AuthenticationManager;
import clientModules.connection.*;
import commandsModule.commands.CommandsReceiver;
import commands.CommandDescription;
import commandsModule.commands.CommandRegistry;
import commandsModule.commandsManagement.CommandHandler;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.BufferOverflowException;
import java.nio.channels.AlreadyConnectedException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Program entry point class. Contains <code>main()</code> method.
 */
public class App {
    private final static int PORT = 64999;
    private static final InetAddress ADDRESS;

    static {
        try {
            ADDRESS = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Program entry point.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Logger jlineLogger = Logger.getLogger("org.jline");
        jlineLogger.setLevel(Level.OFF);

        DatagramConnectionModuleFactory connectionModuleFactory = new DatagramConnectionModuleFactory();
        try {
            DataTransferConnectionModule connectionModule = (DataTransferConnectionModule) App.initConnection(connectionModuleFactory);

            Terminal terminal = org.jline.terminal.TerminalBuilder.builder().system(true).build();

            App.addShutdownHook(connectionModule, terminal);

            App.authenticateUser(connectionModule, terminal);

            App.initCommandRegistry(connectionModule, 5000);

            App.allowInputAndHandleIt(App.initCommandHandler(connectionModule, terminal));
        } catch (UnknownHostException e) {
            System.out.println("Could not find host");
        } catch (AlreadyConnectedException e) {
            System.out.println("Already connected to the server");
        } catch (SecurityException e) {
            System.out.println("Security manager does not permit access to the remote address");
        } catch (IOException e) {
            System.out.println("Something went wrong during I/O operations");
        } catch (BufferOverflowException e) {
            System.out.println("byte[] size is larger than allocated size in buffer");
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted while initializing commands");
        } catch (UserInterruptException e) {
            System.out.println("Force shutdown...");
        } catch (Exception e) {
            System.out.println("Unexpected error happened during app operations");
        }
    }

    private static ConnectionModule initConnection(DatagramConnectionModuleFactory connectionModuleFactory) throws IOException {
        DataTransferConnectionModule connectionModule = connectionModuleFactory
                .createConnectionModule(new InetSocketAddress(ADDRESS, PORT), false);

        connectionModule.connect();
        System.out.println("Server connection established");

        return connectionModule;
    }

    private static void addShutdownHook(ConnectionModule connectionModule, Terminal terminal) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (connectionModule.isConnected()) {
                    connectionModule.disconnect();
                    System.out.println("Disconnected from the server");
                }

                if (terminal != null) {
                    terminal.close();
                }
            } catch (IOException e) {
                System.out.println("An error occurred while disconnecting from the server\nForce shutdown...");
            }
        }));
    }

    private static void authenticateUser(DataTransferConnectionModule dataTransferConnectionModule, Terminal terminal) {
        AuthenticationManager authenticationManager = new AuthenticationManager(dataTransferConnectionModule, terminal);
        int authenticated = 0;

        while (authenticated != 1) {
            try {
                authenticated = authenticationManager.authenticateFromInput();

                if (authenticated == 2) {
                    System.out.println("Shutdown...");
                    System.exit(0);
                }
            } catch (ServerUnavailableException | ResponseTimeoutException | IOException | NullPointerException e) {
                System.out.println("Server is currently unavailable. Please try again later");
            }
        }
    }

    private static void initCommandRegistry(DataTransferConnectionModule connectionModule, long timeMills) throws InterruptedException {
        boolean initializedCommands = false;
        CommandsReceiver commandsReceiver = new CommandsReceiver(connectionModule);

        while (!initializedCommands) {
            System.out.println("Trying to initialize commands...");
            try {
                initializedCommands = commandsReceiver.initCommands();
            } catch (ServerUnavailableException | ResponseTimeoutException | IOException | NullPointerException e) {
                TimeUnit.MILLISECONDS.sleep(timeMills);
            }
        }
        System.out.println("Commands initialized");
    }

    private static CommandHandler initCommandHandler(DataTransferConnectionModule connectionModule, Terminal terminal) throws IOException {
        List<CommandDescription> commands = CommandRegistry.getInstance().getCommands();
        return new CommandHandler(commands, terminal, connectionModule);
    }

    private static void allowInputAndHandleIt(CommandHandler handler) {
        System.out.println("Console input allowed");
        handler.startHandlingInput();
    }
}