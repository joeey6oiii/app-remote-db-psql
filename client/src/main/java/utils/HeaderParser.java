package utils;

import response.data.FragmentHeader;
import serializer.ObjectSerializer;

import java.io.IOException;

/**
 * A class that has helpful method to parse header in the bytes array.
 */

public class HeaderParser {

    /**
     * A method that parses header in the specified bytes array.
     *
     * @param data the specified bytes array
     * @return {@link FragmentHeader} - object of a header type
     * @throws IOException if there was some problem during I/O operations
     * @throws ClassNotFoundException if the class to deserialize was not found
     */

    public FragmentHeader parseHeader(byte[] data) throws IOException, ClassNotFoundException {
        int headerSize = data[0];
        byte[] headerData = new byte[headerSize];
        System.arraycopy(data, 1, headerData, 0, headerSize);

        Object obj = new ObjectSerializer().deserialize(headerData);

        if (obj instanceof FragmentHeader fragmentHeader) {
            return fragmentHeader;
        } else {
            throw new IOException("Unexpected error: Received object of non-FragmentHeader type");
        }
    }

}
