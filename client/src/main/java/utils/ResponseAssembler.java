package utils;

import java.util.HashMap;

/**
 * A class that represents response assembler.
 */

public class ResponseAssembler {

    /**
     * A method to assembly response parts into one actual response.
     *
     * @param responseParts parts of the actual response
     * @return byte array which can be converted to a response
     * @throws IllegalArgumentException if one of the response parts is missing
     */

    public byte[] combineResponseParts(HashMap<Integer, byte[]> responseParts) throws IllegalArgumentException {
        int totalSize = 0;
        for (byte[] part : responseParts.values()) {
            totalSize += part.length;
        }

        byte[] combinedResponse = new byte[totalSize];

        int offset = 0;
        if (responseParts.size() > 1) {
            for (int i = 0; i < responseParts.size() - 1; i++) {
                byte[] part = responseParts.get(i);
                if (part != null) {
                    System.arraycopy(part, 0, combinedResponse, offset, part.length);
                    offset += part.length;
                } else {
                    throw new IllegalArgumentException("Response part is missing for index " + i);
                }
            }
        }

        byte[] lastPart = responseParts.get(-1);
        if (lastPart != null) {
            System.arraycopy(lastPart, 0, combinedResponse, offset, lastPart.length);
        }

        return combinedResponse;
    }

}
