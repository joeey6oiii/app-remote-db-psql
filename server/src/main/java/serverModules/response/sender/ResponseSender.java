package serverModules.response.sender;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import response.data.FragmentHeader;
import response.responses.Response;
import serializer.ByteArrayObjectSerializer;
import userModules.users.User;
import serverModules.connection.ConnectionModule;
import utility.UdpDataTransferUtilities;

import java.net.InetAddress;

/**
 * A class that represents the base response sender.
 */
public class ResponseSender implements ResponseAble<Response> {
    private static final Logger logger = LogManager.getLogger("logger.ResponseSender");
    private final ConnectionModule connectionModule;

    public ResponseSender(ConnectionModule connectionModule) {
        this.connectionModule = connectionModule;
    }

    /**
     * A method that serializes the received response, divides it by chunks and sends them to the client.
     * Sends <code>byte[]</code>, where header length placed on the first position, then from second position to length
     * of the header placed serialized header, and after placed the chunk (part of the data)
     *
     * @param user client
     * @param response answer to the client
     */
    @Override
    public void sendResponse(User user, Response response) throws IllegalArgumentException {
        if (response != null) {
            ByteArrayObjectSerializer serializer = new ByteArrayObjectSerializer();

            final InetAddress clientAddress = user.getAddress();
            final int clientPort = user.getPort();
            try {
                byte[] data = serializer.serialize(response);
                int maxPacketSize = UdpDataTransferUtilities.INSTANCE.getPacketSize();
                int packetIndex = 0;
                int offset = 0;

                while (offset < data.length) {
                    FragmentHeader header = new FragmentHeader(packetIndex);
                    byte[] headerData = serializer.serialize(header);
                    int headerDataLength = headerData.length;

                    if (headerDataLength >= maxPacketSize - 1) {
                        throw new IllegalArgumentException("Header data size is larger than max packet size");
                    }

                    int freeSpace = maxPacketSize - headerDataLength - 1;
                    int remainingData = data.length - offset;
                    int length = Math.min(freeSpace, remainingData);

                    byte[] packet = new byte[length + headerDataLength + 1];
                    packet[0] = (byte) headerDataLength;
                    System.arraycopy(headerData, 0, packet, 1, headerDataLength);
                    System.arraycopy(data, offset, packet, headerDataLength + 1, length);

                    connectionModule.sendData(packet, clientAddress, clientPort);

                    packetIndex++;
                    offset += length;
                }

                FragmentHeader header = new FragmentHeader(-1);
                byte[] headerData = serializer.serialize(header);
                int headerDataLength = headerData.length;

                if (headerDataLength >= maxPacketSize - 1) {
                    throw new IllegalArgumentException("Header data size is larger than max packet size");
                }

                byte[] noDataNotification = new byte[headerDataLength + 1];
                noDataNotification[0] = (byte) headerDataLength;
                System.arraycopy(headerData, 0, noDataNotification, 1, headerDataLength);

                connectionModule.sendData(noDataNotification, clientAddress, clientPort);
            } catch (Exception e) {
                logger.fatal("Something went wrong during I/O operations", e);
            }
        }
    }
}