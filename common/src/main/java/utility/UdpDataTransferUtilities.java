package utility;

/**
 * An enum with UDP data transfer utilities.
 */
public enum UdpDataTransferUtilities {
    PACKET_SIZE(1500);

    private final int packetSizeValue;

    /**
     * Private constructor for the PACKET_SIZE UDP data transfer utility.
     *
     * @param sizeValue the size of a packet
     */

    UdpDataTransferUtilities(int sizeValue) {
        this.packetSizeValue = sizeValue;
    }

    /**
     * @return the size value of a packet
     */

    public int getPacketSizeValue() {
        return packetSizeValue;
    }

}
