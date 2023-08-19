package main;

import databaseModule.handler.PersonCollectionHandler;
import model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import databaseModule.loader.PersonCollectionLoader;
import requests.Request;
import serverModules.connection.ConnectionModule;
import serverModules.connection.ConnectionModuleFactory;
import serverModules.connection.DatagramConnectionModuleFactory;
import serverModules.request.data.ClientRequestInfo;
import serverModules.request.data.RequestData;
import serverModules.request.handlers.RequestHandlerManager;
import serverModules.request.reader.RequestReader;
import userModules.sessionService.AuthenticatedUserRegistrySessionManager;
import userModules.sessionService.SessionManager;
import userModules.users.User;
import utils.UserUtils;

import java.io.IOException;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Program entry point class. Contains <code>main()</code> method.
 */
public class Server {
    private static final Logger logger = LogManager.getLogger("logger.Server");
    private static final ExecutorService requestReadingThreadPool = Executors.newFixedThreadPool(2);
    private static final int PORT = 64999;

    /**
     * Program entry point.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SQLException {
        PersonCollectionHandler collectionHandler = PersonCollectionHandler.getInstance();

        try (PersonCollectionLoader<HashSet<Person>> collectionLoader = new PersonCollectionLoader<>(collectionHandler.getCollection())) {
            collectionLoader.loadElementsFromDB();
        } catch (SQLException e) {
            logger.error("Could not load elements from the database", e);
            throw e;
        }
        collectionHandler.sortCollection();
        logger.info("Loaded elements from the database to a collection");

        ConnectionModuleFactory connectionModuleFactory = new DatagramConnectionModuleFactory();
        ConnectionModule connectionModule;

        logger.info("Initializing server...");
        try {
            connectionModule = connectionModuleFactory.createConnectionModule(PORT);
        } catch (SocketException e) {
            logger.error("An error occurred while creating server core. Can not run a new server");
            throw e;
        }
        logger.info("Server started");

        RequestReader requestReader = new RequestReader();
        RequestHandlerManager requestHandlerManager = new RequestHandlerManager(connectionModule);

        SessionManager sessionManager = new AuthenticatedUserRegistrySessionManager();
        sessionManager.startSessionExpirationCheck(UserUtils.INSTANCE.getSessionDurationInMinutes());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            sessionManager.stopSessionExpirationCheck();
            requestReadingThreadPool.shutdown();
        }));

        while (true) {
            try {
                RequestData requestData = connectionModule.receiveData();
                if (requestData.hasNullStatus()) {
                    logger.debug("Empty request received");
                    continue;
                }

                requestReadingThreadPool.submit(() -> {
                    try {
                        byte[] dataByteArray = requestData.getByteArray();
                        Request request = requestReader.readRequest(dataByteArray);
                        User user = requestData.getUser();

                        ClientRequestInfo info = new ClientRequestInfo(user, request);
                        requestHandlerManager.manageRequest(info);
                    } catch (IOException e) {
                        logger.error("Something went wrong during I/O operations", e);
                    } catch (ClassNotFoundException e) {
                        logger.error("Could not find request class", e);
                    }
                });
            } catch (Exception e) {
                logger.error("Unexpected error happened during server operations", e);
            }
        }
    }
}