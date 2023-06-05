package clientModules.connection;

import java.net.SocketAddress;

/**
 * A class that represents a factory of {@link DataTransferConnectionModule} objects.
 */

public interface DataTransferConnectionModuleFactory extends ConnectionModuleFactory {

    /**
     * A method that creates the {@link DataTransferConnectionModule} object with the specified address of the server.
     *
     * @param address specified address of the server
     * @return client connection core
     */

    DataTransferConnectionModule create(SocketAddress address);

}
