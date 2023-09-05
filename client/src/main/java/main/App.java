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
import org.jline.terminal.TerminalBuilder;
import outputService.MessageType;
import outputService.ColoredPrintStream;
import outputService.OutputSource;

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

        ColoredPrintStream printer = new ColoredPrintStream(OutputSource.getOutputStream());

        DatagramConnectionModuleFactory connectionModuleFactory = new DatagramConnectionModuleFactory();
        try {
            DataTransferConnectionModule connectionModule = (DataTransferConnectionModule) App
                    .initConnection(connectionModuleFactory, false);
            printer.println(printer.formatMessage(MessageType.SUCCESS, "Server connection established"));

            Terminal terminal = TerminalBuilder.builder().system(true).build();

            App.addShutdownHook(connectionModule, terminal);

            App.authenticateUser(connectionModule, terminal);

            printer.println(printer.formatMessage(MessageType.INFO, "Initializing command registry"));
            App.initCommandRegistry(connectionModule, 5000);
            printer.println(printer.formatMessage(MessageType.SUCCESS, "Command registry initialized"));

            CommandHandler commandHandler = App.initCommandHandler(connectionModule, terminal);

            printer.println(printer.formatMessage(MessageType.INFO, "Console input allowed"));
            commandHandler.startHandlingInput();
        } catch (UnknownHostException e) {
            printer.println(printer.formatMessage(MessageType.ERROR,  "Could not find host"));
        } catch (AlreadyConnectedException e) {
            printer.println(printer.formatMessage(MessageType.ERROR, "Already connected to the server"));
        } catch (SecurityException e) {
            printer.println(printer.formatMessage(MessageType.ERROR, "Security manager does not permit access to the remote address"));
        } catch (IOException e) {
            printer.println(printer.formatMessage(MessageType.ERROR, "Something went wrong during I/O operations"));
        } catch (BufferOverflowException e) {
            printer.println(printer.formatMessage(MessageType.ERROR, "byte[] size is larger than allocated size in buffer"));
        } catch (InterruptedException e) {
            printer.println(printer.formatMessage(MessageType.ERROR, "Thread interrupted while initializing commands"));
        } catch (UserInterruptException e) {
            printer.println(printer.formatMessage(MessageType.INFO, "Force shutdown"));
        } catch (Exception e) {
            printer.println(printer.formatMessage(MessageType.ERROR, "Unexpected error happened during app operations"));
        }
    }

    private static ConnectionModule initConnection(DatagramConnectionModuleFactory connectionModuleFactory, boolean isBlocking) throws IOException {
        DataTransferConnectionModule connectionModule = connectionModuleFactory
                .createConnectionModule(new InetSocketAddress(ADDRESS, PORT), isBlocking);
        connectionModule.connect();

        return connectionModule;
    }

    private static void addShutdownHook(ConnectionModule connectionModule, Terminal terminal) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ColoredPrintStream printer = new ColoredPrintStream(OutputSource.getOutputStream());

            try {
                if (connectionModule.isConnected()) {
                    connectionModule.disconnect();
                    printer.println(printer.formatMessage(MessageType.INFO, "Disconnected from the server"));
                }

                if (terminal != null) {
                    terminal.close();
                }
            } catch (IOException e) {
                printer.println(printer.formatMessage(MessageType.ERROR, "An error occurred while disconnecting from the server"));
                printer.println(printer.formatMessage(MessageType.INFO, "Force shutdown"));
            }
        }));
    }

    private static void authenticateUser(DataTransferConnectionModule dataTransferConnectionModule, Terminal terminal) {
        AuthenticationManager authenticationManager = new AuthenticationManager(dataTransferConnectionModule, terminal);
        int authenticated = 0;

        ColoredPrintStream printer = new ColoredPrintStream(OutputSource.getOutputStream());

        while (authenticated != 1) {
            try {
                authenticated = authenticationManager.authenticateFromInput();

                if (authenticated == 2) {
                    printer.println(printer.formatMessage(MessageType.INFO, "Shutdown"));
                    System.exit(0);
                }
            } catch (ServerUnavailableException | ResponseTimeoutException | IOException | NullPointerException e) {
                printer.println(printer.formatMessage(MessageType.WARNING, "Server is currently unavailable. Please try again later"));
            }
        }
    }

    private static void initCommandRegistry(DataTransferConnectionModule connectionModule, long timeMills) throws InterruptedException {
        boolean initializedCommands = false;
        CommandsReceiver commandsReceiver = new CommandsReceiver(connectionModule);

        Thread loadingAnimationThread = App.getLoadingAnimationThread(250);
        loadingAnimationThread.start();

        while (!initializedCommands) {
            try {
                initializedCommands = commandsReceiver.initCommands();
            } catch (ServerUnavailableException | ResponseTimeoutException | IOException | NullPointerException e) {
                TimeUnit.MILLISECONDS.sleep(timeMills);
            }
        }

        loadingAnimationThread.interrupt();

        new ColoredPrintStream(OutputSource.getOutputStream()).println();
    }

    private static Thread getLoadingAnimationThread(long timeMills) {
        return new Thread(() -> {
            String[] rotatingStick = { "|", "/", "-", "\\" };

            ColoredPrintStream printer = new ColoredPrintStream(OutputSource.getOutputStream());

            while (true) {
                for (String stick : rotatingStick) {
                    printer.print("\rLoading... " + stick);
                    try {
                        TimeUnit.MILLISECONDS.sleep(timeMills);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        });
    }

    private static CommandHandler initCommandHandler(DataTransferConnectionModule connectionModule, Terminal terminal) throws IOException {
        List<CommandDescription> commands = CommandRegistry.getInstance().getCommands();
        return new CommandHandler(commands, terminal, connectionModule);
    }
}