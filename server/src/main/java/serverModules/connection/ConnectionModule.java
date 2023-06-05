package serverModules.connection;

import serverModules.request.data.RequestData;

import java.net.InetAddress;

/**
 * An interface for all connection module-implementers.
 */

public interface ConnectionModule {

    /**
     * A method that receives data.
     *
     * @return a class that contains the information about the received request
     */

    RequestData receiveData();

    /**
     * A method that sends the specified data to the specified address and port.
     *
     * @param data data to send
     * @param address address to send to
     * @param port port to send to
     */

    void sendData(byte[] data, InetAddress address, int port);
}
