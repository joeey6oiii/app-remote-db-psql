package serverModules.callerBack;

import java.net.InetAddress;

/**
 * A class that represents a client.
 */

public class CallerBack {
    private final InetAddress address;
    private final int port;

    /**
     * A constructor for the client.
     *
     * @param address address of the client
     * @param port port of the client
     */

    public CallerBack(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    /**
     * A method that returns the address of the client.
     */

    public InetAddress getAddress() {
        return address;
    }

    /**
     * A method that returns the port of the client.
     */

    public int getPort() {
        return port;
    }

}