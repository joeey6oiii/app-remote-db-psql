package serverModules.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.SocketException;

/**
 * A class that represents a factory of {@link DatagramConnectionModule} objects.
 */
public class DatagramConnectionModuleFactory implements ConnectionModuleFactory {
    private static final Logger logger = LogManager.getLogger("logger.DatagramConnectionModuleFactory");

    /**
     * A method that creates the {@link DatagramConnectionModule} object with the specified port.
     *
     * @param PORT specified port of the server
     * @return server core
     */
    @Override
    public DatagramConnectionModule createConnectionModule(int PORT) {
        try {
            return new DatagramConnectionModule(PORT);
        } catch (SocketException e) {
            logger.fatal("Failed to create server core", e);
            System.exit(-99);
        }
        return null;
    }
}