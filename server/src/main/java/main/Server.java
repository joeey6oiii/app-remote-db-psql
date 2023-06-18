package main;

import database.Database;
import database.LoadService;
import database.PersonDB;
import defaultClasses.Person;
import fileService.FileService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import requests.Request;
import serverModules.connection.ConnectionModule;
import serverModules.connection.ConnectionModuleFactory;
import serverModules.connection.UdpConnectionModuleFactory;
import serverModules.context.ServerContext;
import serverModules.request.data.RequestData;
import serverModules.request.handlers.RequestHandlerManager;
import serverModules.request.reader.RequestReader;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

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

    public static void main(String[] args) throws SocketException {

        ConnectionModuleFactory factory = new UdpConnectionModuleFactory();
        ConnectionModule module;
        try {
            module = factory.createConnectionModule(PORT);
        } catch (SocketException e) {
            logger.fatal("There was a problem while creating the server core. Can not start new server");
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
                ServerContext context = new ServerContext(module, requestData.getCallerBack(), request);
                new RequestHandlerManager().manageRequest(context);
            } catch (IOException e) {
                logger.error("Something went wrong during I/O operations", e);
            } catch (ClassNotFoundException e) {
                logger.error("Could not find request class", e);
            } catch (Exception e) {
                logger.error("Unexpected error happened during server operations", e);
            }
        }
    }

}