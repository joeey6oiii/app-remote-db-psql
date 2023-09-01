package main;

import clientModules.authentication.AuthenticationManager;
import clientModules.connection.DataTransferConnectionModule;
import clientModules.connection.DatagramConnectionModuleFactory;
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
public class ClientApp {
    private final static int PORT = 64999;

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
            DataTransferConnectionModule connectionModule = connectionModuleFactory
                    .createConnectionModule(new InetSocketAddress(InetAddress.getLocalHost(), PORT), false);

            connectionModule.connect();
            System.out.println("Server connection established");

            Terminal terminal = org.jline.terminal.TerminalBuilder.builder().system(true).build();

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

            AuthenticationManager authenticationManager = new AuthenticationManager(connectionModule, terminal);
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

            long timeout = 5;
            boolean initializedCommands = false;
            CommandsReceiver commandsReceiver = new CommandsReceiver(connectionModule);

            while (!initializedCommands) {
                System.out.println("Trying to initialize commands...");
                try {
                    initializedCommands = commandsReceiver.initCommands();
                } catch (ServerUnavailableException | ResponseTimeoutException | IOException | NullPointerException e) {
                    TimeUnit.SECONDS.sleep(timeout);
                }
            }
            System.out.println("Commands initialized");

            List<CommandDescription> commands = CommandRegistry.getInstance().getCommands();
            CommandHandler handler = new CommandHandler(commands, terminal, connectionModule);

            System.out.println("Console input allowed");
            handler.startHandlingInput();
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
}