package utility;

/**
 * An enum with UDP data transfer utilities.
 */
public enum UdpDataTransferUtilities {
    INSTANCE;

    private final int packetSizeValue = 1500;

    /**
     * @return the size value of a packet
     */

    public int getPacketSizeValue() {
        return packetSizeValue;
    }

}
