package serverModules.connection;

import java.net.SocketException;

/**
 * A class that represents a factory of {@link ConnectionModule} objects.
 */

public interface ConnectionModuleFactory {

    /**
     * A method that creates the {@link ConnectionModule} object with the specified port.
     *
     * @param PORT specified port of the server
     * @return server core
     * @throws SocketException if any failures happen during server core creation
     */

    ConnectionModule createConnectionModule(int PORT) throws SocketException;

}
