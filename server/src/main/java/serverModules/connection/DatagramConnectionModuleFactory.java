package serverModules.connection;

import java.net.SocketException;

/**
 * A class that represents a factory of {@link DatagramConnectionModule} objects.
 */
public class DatagramConnectionModuleFactory implements ConnectionModuleFactory {

    /**
     * A method that creates the {@link DatagramConnectionModule} object with the specified port.
     *
     * @param PORT specified port of the server
     * @return server core
     */
    @Override
    public DatagramConnectionModule createConnectionModule(int PORT) throws SocketException {
        return new DatagramConnectionModule(PORT);
    }
}