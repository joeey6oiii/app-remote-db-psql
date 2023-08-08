package utils;

/**
 * A class that offers some useful byte array utilities.
 */
public class ByteArrayUtils {

    /**
     * Turns array of chars into array of bytes by casting every element in the char array to the byte type.
     *
     * @param chars char array to transform into a byte array
     * @return byte array as a result of transforming the specified char array
     */
    public static byte[] charArrayToByteArray(char[] chars) {
        byte[] bytes = new byte[chars.length];
        for (int i = 0; i < chars.length; i++) {
            bytes[i] = (byte) chars[i];
        }
        return bytes;
    }

    /**
     * Turns a number of byte arrays into one byte array.
     * Concatenation order depends on the order of the given arguments.
     *
     * @param arrays a number of byte arrays to concatenate
     * @return one byte array with elements from every byte array given as an argument
     */
    public static byte[] concatByteArrays(byte[]... arrays) {
        int totalLength = 0;
        for (byte[] array : arrays) {
            totalLength += array.length;
        }

        byte[] result = new byte[totalLength];
        int destPos = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, destPos, array.length);
            destPos += array.length;
        }

        return result;
    }
}
