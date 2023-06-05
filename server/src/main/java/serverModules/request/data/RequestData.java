package serverModules.request.data;

import serverModules.callerBack.CallerBack;

/**
 * A class that contains the information about the received request.
 */

public class RequestData {
    private byte[] data;
    private CallerBack callerBack;
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
     * @param callerBack the client
     */

    public RequestData(byte[] data, CallerBack callerBack) {
        this.data = data;
        this.callerBack = callerBack;
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

    public CallerBack getCallerBack() {
        return callerBack;
    }

    /**
     * A method that sets the specified client.
     *
     * @param callerBack client to set
     */

    public void setCallerBack(CallerBack callerBack) {
        this.callerBack = callerBack;
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