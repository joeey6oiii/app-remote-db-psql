package serverModules.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.BindException;
import java.net.SocketException;

/**
 * A class that represents a factory of {@link UdpConnectionModule} objects.
 */

public class UdpConnectionModuleFactory implements ConnectionModuleFactory {
    private static final Logger logger = LogManager.getLogger("logger.DatagramConnectionModuleFactory");

    /**
     * A method that creates the {@link UdpConnectionModule} object with the specified port.
     *
     * @param PORT specified port of the server
     * @return server core
     */

    @Override
    public UdpConnectionModule createConnectionModule(int PORT) {
        try {
            return new UdpConnectionModule(PORT);
        } catch (SocketException e) {
            logger.fatal("Failed to create server core", e);
            System.exit(-99);
        }
        return null;
    }
}
