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
    public DatagramConnectionModule create(SocketAddress address) {
        try {
            return new DatagramConnectionModule(DatagramChannel.open(), address);
        } catch (IOException e) {
            System.out.println("Unable to create client connection core");
            System.exit(-99);
        }
        return null;
    }

    /**
     * A method that creates the {@link DatagramConnectionModule} object with the specified address of the server and
     * configures blocking state of the datagram channel with the <code>boolean</code> isBlocking parameter.
     *
     * @param address specified address of the server
     * @param isBlocking blocking state of the datagram channel
     * @return client connection core
     */
    public DatagramConnectionModule createConfigureBlocking(SocketAddress address, boolean isBlocking) {
        DatagramChannel datagramChannel;
        try {
            datagramChannel = DatagramChannel.open();
            datagramChannel.configureBlocking(isBlocking);
            return new DatagramConnectionModule(datagramChannel, address);
        } catch (IOException e) {
            System.out.println("Unable to create client connection core");
            System.exit(-99);
        }
        return null;
    }

    /**
     * A method
     * that configures blocking state of the datagram channel with the <code>boolean</code> isBlocking parameter.
     *
     * @param module client connection core
     * @param isBlocking blocking state of the datagram channel
     */
    public void configureBlocking(DatagramConnectionModule module, boolean isBlocking) {
        try {
            if (module.getDatagramChannel() != null && module.getDatagramChannel().isOpen()) {
                    module.getDatagramChannel().configureBlocking(isBlocking);
            }
        } catch (IOException e) {
            System.out.println("Unable to configure client connection core");
            System.exit(-99);
        }
    }
}