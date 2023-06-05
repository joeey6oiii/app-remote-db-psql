package serverModules.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import serverModules.callerBack.CallerBack;
import serverModules.request.data.RequestData;
import utility.UdpDataTransferUtilities;

import java.io.IOException;
import java.net.*;

/**
 * A class that represents the datagram connection module.
 */

public class UdpConnectionModule implements ConnectionModule {
    private static final Logger logger = LogManager.getLogger("logger.ConnectionModule");
    private final int PACKET_SIZE = UdpDataTransferUtilities.PACKET_SIZE.getPacketSizeValue();
    private final DatagramSocket socket;

    /**
     * A constructor for the datagram connection module.
     *
     * @param port the specified server port
     * @throws SocketException if error happened during socket operations
     */

    protected UdpConnectionModule(int port) throws SocketException {
        this.socket = new DatagramSocket(port);
    }

    /**
     * A method that receives data.
     *
     * @return a class that contains the information about the received request.
     * Returns empty {@link RequestData} if empty request received.
     */

    @Override
    public RequestData receiveData() {
        byte[] bytes = new byte[PACKET_SIZE];
        try {
            DatagramPacket packet = new DatagramPacket(bytes, PACKET_SIZE);
            socket.receive(packet);
            logger.debug("Received data");

            return new RequestData(packet.getData(), new CallerBack(packet.getAddress(), packet.getPort()));
        } catch (IOException e) {
            logger.error("Something went wrong during receiving data", e);
        }

        return new RequestData();
    }

    /**
     * A method that sends the specified data to the specified address and port.
     *
     * @param data data to send
     * @param address address to send to
     * @param port port to send to
     */

    @Override
    public void sendData(byte[] data, InetAddress address, int port) {
        try {
            if (data.length < PACKET_SIZE) {
                socket.send(new DatagramPacket(data, data.length, address, port));
            } else if (data.length == PACKET_SIZE) {
                socket.send(new DatagramPacket(data, PACKET_SIZE, address, port));
            } else {
                throw new IOException("Unexpected error: byte[] size is larger than packet size");
            }
        } catch (IOException e) {
            logger.error("Something went wrong during data sending", e);
        }
        logger.debug("Data sent");
    }

}