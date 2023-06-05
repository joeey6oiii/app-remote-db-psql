package clientModules.connection;

import java.net.SocketAddress;

/**
 * A class that represents a factory of {@link ConnectionModule} objects.
 */

public interface ConnectionModuleFactory {

    /**
     * A method that creates the {@link ConnectionModule} object with the specified address of the server.
     *
     * @param address specified address of the server
     * @return client connection core
     */

    ConnectionModule create(SocketAddress address);

}
