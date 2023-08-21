package serverModules.request.data;

import java.net.InetAddress;

/**
 * A class that contains the information about the received request.
 */
public class RequestData {
    private final byte[] data;
    private final InetAddress address;
    private final int port;
    private final boolean nullStatus;

    /**
     * A default constructor when the empty request received.
     */
    public RequestData() {
        data = new byte[0];
        address = null;
        port = 0;
        nullStatus = true;
    }

    public RequestData(byte[] data, InetAddress address, int port) {
        this.data = data;
        this.address = address;
        this.port = port;
        nullStatus = false;
    }

    /**
     * Retrieves the data byte array.
     */
    public byte[] getData() {
        return this.data;
    }

    /**
     * Retrieves the address value.
     */
    public InetAddress getAddress() {
        return this.address;
    }

    /**
     * Retrieves the port value.
     *
     * @return the port value
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Checks if the received request is empty or not.
     *
     * @return true if the request is empty, otherwise false
     */
    public boolean hasNullStatus() {
        return this.nullStatus;
    }
}