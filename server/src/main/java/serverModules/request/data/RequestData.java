package serverModules.request.data;

import userModules.users.AbstractUser;

/**
 * A class that contains the information about the received request.
 */
public class RequestData {
    private final byte[] data;
    private final AbstractUser user;
    private final boolean nullStatus;

    /**
     * A default constructor when the empty request received.
     */
    public RequestData() {
        data = new byte[0];
        user = null;
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
     * Retrieves the data byte array.
     */
    public byte[] getByteArray() {
        return data;
    }

    /**
     * Retrieves the client.
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