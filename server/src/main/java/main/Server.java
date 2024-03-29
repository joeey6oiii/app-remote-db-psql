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
import serverModules.request.data.RequestInfo;
import serverModules.request.data.RequestData;
import serverModules.request.handlers.RequestHandlerManager;
import serverModules.request.reader.RequestReader;
import userModules.sessionService.AuthenticatedUserRegistrySessionManager;
import userModules.sessionService.SessionManager;
import userModules.users.AbstractUser;
import userModules.users.User;
import userModules.users.utils.UserUtils;

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
        Server.loadAndSortCollection();

        ConnectionModuleFactory connectionModuleFactory = new DatagramConnectionModuleFactory();
        ConnectionModule connectionModule = Server.initServer(connectionModuleFactory);
        logger.info("Server started");

        RequestReader requestReader = new RequestReader();
        RequestHandlerManager requestHandlerManager = new RequestHandlerManager(connectionModule);

        SessionManager sessionManager = new AuthenticatedUserRegistrySessionManager();
        sessionManager.startSessionExpirationCheck(UserUtils.INSTANCE.getSessionCheckIntervalInMinutes());

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

                requestReadingThreadPool.submit(() -> Server.proceedRequest(requestData, requestReader, requestHandlerManager));
            } catch (Exception e) {
                logger.error("Unexpected error happened during server operations", e);
            }
        }
    }

    private static void loadAndSortCollection() throws SQLException, IOException {
        PersonCollectionHandler collectionHandler = PersonCollectionHandler.getInstance();
        try (PersonCollectionLoader<HashSet<Person>> collectionLoader = new PersonCollectionLoader<>(collectionHandler.getCollection())) {
            collectionLoader.loadElementsFromDB();
        } catch (SQLException | IOException e) {
            logger.error("Could not load elements from the database", e);
            throw e;
        }
        collectionHandler.sortCollection();
        logger.info("Loaded elements from the database to a collection");
    }

    private static ConnectionModule initServer(ConnectionModuleFactory connectionModuleFactory) throws IOException {
        ConnectionModule connectionModule;

        logger.info("Initializing server...");
        try {
            connectionModule = connectionModuleFactory.createConnectionModule(PORT);
        } catch (SocketException e) {
            logger.error("Error occurred while initializing server", e);
            throw e;
        }

        return connectionModule;
    }

    private static void proceedRequest(RequestData requestData, RequestReader requestReader, RequestHandlerManager requestHandlerManager) {
        try {
            byte[] dataByteArray = requestData.getData();
            Request request = requestReader.readRequest(dataByteArray);
            AbstractUser user = new User(requestData.getAddress(), requestData.getPort());

            RequestInfo info = new RequestInfo(user, request);
            requestHandlerManager.manageRequest(info);
        } catch (IOException e) {
            logger.error("Something went wrong during I/O operations", e);
        } catch (ClassNotFoundException e) {
            logger.error("Could not find request class", e);
        }
    }
}