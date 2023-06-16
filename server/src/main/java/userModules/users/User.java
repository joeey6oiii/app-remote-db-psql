package userModules.users;

import java.net.InetAddress;

/**
 * A class that contains the address and port of the machine from which the request was sent.
 */

public class User {
    private final InetAddress address;
    private final int port;

    /**
     * A constructor for the client.
     *
     * @param address address of the client
     * @param port port of the client
     */

    public User(InetAddress address, int port) {
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