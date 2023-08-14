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

import java.io.IOException;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.HashSet;

/**
 * Program entry point class. Contains <code>main()</code> method.
 */
public class Server {
    private static final Logger logger = LogManager.getLogger("logger.Server");
    private static final int PORT = 64999;

    /**
     * Program entry point.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        PersonCollectionHandler collectionHandler = PersonCollectionHandler.getInstance();

        try {
            try (PersonCollectionLoader<HashSet<Person>> collectionLoader = new PersonCollectionLoader<>(collectionHandler.getCollection())) {
                collectionLoader.loadElementsFromDB();
            }
            collectionHandler.sortCollection();
            logger.info("Loaded elements from the database to a collection");

            ConnectionModuleFactory factory = new DatagramConnectionModuleFactory();
            ConnectionModule module;

            logger.info("Initializing server...");
            try {
                module = factory.createConnectionModule(PORT);
            } catch (SocketException e) {
                logger.error("An error occurred while creating server core. Can not run a new server");
                throw e;
            }
            logger.info("Server started");

            while (true) {
                try {
                    RequestData requestData = module.receiveData();
                    if (requestData.hasNullStatus()) {
                        logger.debug("Empty request received");
                        continue;
                    }

                    Request request = new RequestReader().readRequest(requestData.getByteArray());
                    ClientRequestInfo info = new ClientRequestInfo(module, requestData.getUser(), request);
                    new RequestHandlerManager().manageRequest(info);
                } catch (IOException e) {
                    logger.error("Something went wrong during I/O operations", e);
                } catch (ClassNotFoundException e) {
                    logger.error("Could not find request class", e);
                } catch (Exception e) {
                    logger.error("Unexpected error happened during server operations", e);
                }
            }
        } catch (SQLException e) {
            logger.error("An error occurred while connecting to the database", e);
        }
    }
}