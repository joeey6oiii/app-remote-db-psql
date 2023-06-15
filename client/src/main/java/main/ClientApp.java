package main;

import clientModules.authentication.AuthenticationManager;
import clientModules.connection.DataTransferConnectionModule;
import clientModules.connection.UdpConnectionModuleFactory;
import clientModules.response.receivers.CommandsReceiver;
import commands.CommandDescription;
import commandsModule.ClientCommandsKeeper;
import commandsModule.handler.CommandHandler;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.BufferOverflowException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

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

        UdpConnectionModuleFactory factory = new UdpConnectionModuleFactory();
        try {
            DataTransferConnectionModule connectionModule = factory.createConfigureBlocking
                    (new InetSocketAddress(InetAddress.getLocalHost(), PORT), false);

            connectionModule.connect();
            System.out.println("Server connection established");

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

            List<CommandDescription> commands = ClientCommandsKeeper.getCommands();
            Scanner consoleInputReader = new Scanner(System.in);
            CommandHandler handler = new CommandHandler(commands, consoleInputReader, connectionModule);

            AuthenticationManager authenticationManager = new AuthenticationManager(connectionModule);

            while (true) {
                try {
                    int authenticated = authenticationManager.authenticateFromInput();

                    if (authenticated == 2) {
                        System.out.println("Shutdown...");
                        System.exit(0);
                    } else if (authenticated == 1) {
                        break;
                    }

                } catch (ServerUnavailableException | ResponseTimeoutException | IOException | NullPointerException e) {
                    System.out.println("Server is currently unavailable. Please try again later");
                }
            }

            System.out.println("Console input allowed");
            handler.startHandlingInput();

        } catch (UnknownHostException e) {
            System.out.println("Could not find host");
        } catch (IOException e) {
            System.out.println("Something went wrong during I/O operations");
        } catch (BufferOverflowException e) {
            System.out.println("byte[] size is larger than allocated size in buffer");
        } catch (Exception e) {
            System.out.println("Unexpected error happened during client operations");
        }
    }

}
