package utility;

/**
 * An enum with UDP data transfer utilities.
 */

public enum UdpDataTransferUtilities {
    INSTANCE;

    private final int PACKET_SIZE = 1500;

    /**
     * @return the size value of a packet
     */

    public int getPacketSize() {
        return PACKET_SIZE;
    }

}
