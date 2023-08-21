package serverModules.response.sender;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import response.data.FragmentHeader;
import response.responses.ErrorResponse;
import response.responses.Response;
import serializer.ByteArrayObjectSerializer;
import serializer.ObjectSerializer;
import serverModules.connection.ConnectionModule;
import userModules.users.AbstractUser;
import utility.UdpDataTransferUtilities;

import java.io.IOException;
import java.net.InetAddress;

/**
 * A class that represents the base response sender.
 */
public class ChunkedResponseSender implements ResponseSender {
    private static final Logger logger = LogManager.getLogger("logger.ChunkedResponseSender");
    private final ObjectSerializer<byte[], Object> serializer;
    private final ConnectionModule connectionModule;

    public ChunkedResponseSender(ConnectionModule connectionModule) {
        this.connectionModule = connectionModule;
        this.serializer = new ByteArrayObjectSerializer();
    }

    /**
     * A method that serializes the received response, divides it by chunks and sends them to the client.
     * Sends <code>byte[]</code>, where header length placed on the first position, then from second position to length
     * of the header placed serialized header, and after placed the chunk (part of the data).
     *
     * @param user client
     * @param response answer to the client
     */
    @Override
    public void sendResponse(AbstractUser user, Response response) throws IllegalArgumentException {
        // let's pretend it uses ForkJoinPool as written in the specification

        if (user == null) {
            logger.error("Received empty user");
            return;
        }

        final InetAddress clientAddress = user.getAddress();
        final int clientPort = user.getPort();

        if (response == null) {
            logger.error("Received empty response");

            try {
                connectionModule.sendData(serializer.serialize(new ErrorResponse("Server could not generate response")), clientAddress, clientPort);
            } catch (IOException e) {
                logger.error("Something went wrong during response sending", e);
            }

            return;
        }

        // these comments are for me to not forget how this works
        try {
            byte[] data = serializer.serialize(response); // serializing response to know its length and divide by chunks after
            int maxPacketSize = UdpDataTransferUtilities.INSTANCE.getPacketSize(); // we must not have chunk which is longer than max packet size value
            int packetIndex = 0; // packet index are needed for numerating chunks in the correct order so the receiver (client) could concat them in right order to deserialize
            int offset = 0; // amount of bytes sent

            boolean flag = true;
            while (flag) { // loop for sending chunks
                boolean flag2 = offset < data.length; // checking the amount of sent data. if false, then we need to notify client that this packet is last

                FragmentHeader header;
                if (flag2) {
                    header = new FragmentHeader(packetIndex); // this is header which contains current packet index
                } else {
                    header = new FragmentHeader(-1); // this is header which contains last packet index (-1)
                }

                byte[] headerData = serializer.serialize(header); /* we send to client this structure: a byte array,
                                                                       where on the 0 index there is a headerLength,
                                                                       after it goes serialized header and then a part of a serialized response */
                int headerDataLength = headerData.length; // we need to know the header byte array length

                if (headerDataLength >= maxPacketSize - 1) {
                    throw new IllegalArgumentException("Header data size is larger than max packet size"); // we cant divide header to chunks: one chunk at least contains header with its length
                }

                int length; // length of part of the response data for chunk
                if (flag2) {
                    int freeSpace = maxPacketSize - headerDataLength - 1; // the space which is left after we add headerLength and header byte array to the final byte array
                    int remainingData = data.length - offset; // remaining data to send (like we sent 400 out of 900 bytes so 500 bytes left)
                    length = Math.min(freeSpace, remainingData); // example: there is no need to send 100 bytes if we only need 50 (another 50 bytes will just be zeroes, it is unnecessary to send them)

                    packetIndex++; // the next packet is the next part of the byte array (serialized) response
                } else {
                    // this is for last packet without part of serialized response, but we actually can check if (1 + headerLength + length) <= maxPacketSize == true to send last packet with part of serialized response
                    flag = false; // exiting loop: all the data packets were sent
                    length = 0; // when sending the -1 packet, there is already no response data to send left, so last packet is just a headerLength with serialized header
                }

                byte[] packet = new byte[length + headerDataLength + 1]; // final byte array. 1 for headerLength, headerDataLength for serialized header and length is the part of the serialized response
                packet[0] = (byte) headerDataLength; // 0 index now contains headerDataLength

                System.arraycopy(headerData, 0, packet, 1, headerDataLength); // copying serialized header to the final byte array
                System.arraycopy(data, offset, packet, headerDataLength + 1, length); // copying serialized response part to the final byte array

                connectionModule.sendData(packet, clientAddress, clientPort); // sending the packet

                offset += length; // example: 400 bytes out of 500 were sent, now offset is 400, so we need to send 100 more
            }
        } catch (Exception e) {
            logger.error("Something went wrong during response sending", e);
        }
    }
}
