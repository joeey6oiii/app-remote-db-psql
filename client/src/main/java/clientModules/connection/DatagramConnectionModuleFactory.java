package clientModules.connection;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;

/**
 * A class that represents a factory of {@link DatagramConnectionModule} objects.
 */
public class DatagramConnectionModuleFactory implements DataTransferConnectionModuleFactory {

    /**
     * A method that creates the {@link DatagramConnectionModule} object with the specified address of the server.
     *
     * @param address specified address of the server
     * @return client connection core
     */
    @Override
    public DatagramConnectionModule createConnectionModule(SocketAddress address) throws IOException {
        return new DatagramConnectionModule(DatagramChannel.open(), address);
    }

    /**
     * A method that creates the {@link DatagramConnectionModule} object with the specified address of the server and
     * configures blocking state of the datagram channel with the <code>boolean</code> isBlocking parameter.
     *
     * @param address specified address of the server
     * @param isBlocking blocking state of the datagram channel
     * @return client connection core
     */
    public DatagramConnectionModule createConnectionModule(SocketAddress address, boolean isBlocking) throws IOException {
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.configureBlocking(isBlocking);

        return new DatagramConnectionModule(datagramChannel, address);
    }
}