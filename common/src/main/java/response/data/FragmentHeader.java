package response.data;

import java.io.Serializable;

/**
 * A class that represents a header in the bytes array.
 */

public class FragmentHeader implements Serializable {
    private final int packetIndex;

    /**
     * A constructor for a header in the bytes array.
     *
     * @param packetIndex index of the current packet
     */

    public FragmentHeader(int packetIndex) {
        this.packetIndex = packetIndex;
    }

    /**
     * @return index of the current packet
     */

    public int getPacketIndex() {
        return packetIndex;
    }

}

