package clientModules.connection;

import java.io.IOException;

/**
 * An interface for all connection module-implementers.
 */

public interface ConnectionModule {

    /**
     * A method that connects a {@link ConnectionModule} to the server.
     *
     * @throws IOException if failed during I/O operations
     */

    void connect() throws IOException;

    /**
     * A method that disconnects a {@link ConnectionModule} from the server.
     *
     * @throws IOException if failed during I/O operations
     */

    void disconnect() throws IOException;
}
