package utils;

import utility.UdpDataTransferUtilities;

/**
 * A class that represents response data parser.
 */

public class ResponseDataParser {

    /**
     * A method that receives byte array, extracts and returns part of the response.
     * All received byte arrays have headers that must not be in the actual response.
     *
     * @param data byte array with header
     * @return <code>byte[]</code> extracted response data
     */

    public byte[] extractResponseData(byte[] data) {
        int headerSize = data[0];
        int responseDataSize = data.length - headerSize - 1;

        if (responseDataSize <= 0) {
            return new byte[0];
        }

        byte[] responseData = new byte[responseDataSize];
        System.arraycopy(data, headerSize + 1, responseData, 0, responseDataSize);

        return responseData;
    }
}
