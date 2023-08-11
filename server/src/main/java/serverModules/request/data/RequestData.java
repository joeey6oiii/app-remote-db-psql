package serverModules.request.data;

import userModules.users.User;

/**
 * A class that contains the information about the received request.
 */
public class RequestData {
    private byte[] data;
    private User user;
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
    public RequestData(byte[] data, User user) {
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
     * A method that sets the specified data byte array.
     *
     * @param data data to set
     */
    public void setByteArray(byte[] data) {
        this.data = data;
    }

    /**
     * A method that returns the client.
     */
    public User getUser() {
        return user;
    }

    /**
     * A method that sets the specified client.
     *
     * @param user client to set
     */
    public void setUser(User user) {
        this.user = user;
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