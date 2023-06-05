package main;

import database.Database;
import database.LoadService;
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
import java.util.ArrayList;
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

        Database database = Database.getInstance();

        File workPathAsFile = FileService.getWorkPathAsFile();
        String workDir = "server";
        if (FileService.isProgramRunningFromJar()) {
            workDir = workPathAsFile.getParentFile().getPath();
        }

        File file = new File(workDir + "/Person.yaml");
        FileService fileService = new FileService();
        Class<Person> type = Person.class;

        List<Person> list = new ArrayList<>();
        try {
            if (file.exists()) {
                logger.info("Reading file...");
                if (file.length() == 0) {
                    logger.info("Upload data not found: File is empty");
                } else {
                    list = fileService.readFile(file, type);
                }
            } else {
                logger.fatal("\"Person.yaml\" file not found");
                fileService.createFile(file);
                logger.info("Created new eponymous file");
            }
        } catch (IOException | NullPointerException e) {
            logger.fatal("Failed to proceed file. Can not continue program execution", e);
            System.exit(-99);
        }

        if (!list.isEmpty()) {
            logger.info("Uploading data from file to database...");
            new LoadService().loadToDatabase(list, database);
        } else {
            logger.info("Continuing execution with an empty database");
        }

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