package serverModules.request.data;

import userModules.users.AbstractUser;

/**
 * A class that contains the information about the received request.
 */
public class RequestData {
    private byte[] data;
    private AbstractUser user;
    private final boolean nullStatus;

    /**
     * A default constructor when the empty request received.
     */
    public RequestData() {
        nullStatus = true;
    }

    /**
     * A constructor for a client request received.
     *
     * @param data received data
     * @param user the client
     */
    public RequestData(byte[] data, AbstractUser user) {
        this.data = data;
        this.user = user;
        nullStatus = false;
    }

    /**
     * A method that returns the data byte array.
     */
    public byte[] getByteArray() {
        return data;
    }

    /**
     * A method that returns the client.
     */
    public AbstractUser getUser() {
        return user;
    }

    /**
     * A method that checks if the received request is empty or not.
     *
     * @return true if the request is empty, otherwise false
     */
    public boolean hasNullStatus() {
        return this.nullStatus;
    }
}