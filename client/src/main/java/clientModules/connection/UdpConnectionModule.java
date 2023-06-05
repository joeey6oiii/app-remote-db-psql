package clientModules.connection;

import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import utility.UdpDataTransferUtilities;

import java.io.IOException;
import java.net.PortUnreachableException;
import java.net.SocketAddress;
import java.util.concurrent.Semaphore;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A class that represents the data transfer datagram connection module.
 */

public class UdpConnectionModule implements DataTransferConnectionModule {
    private final int PACKET_SIZE = UdpDataTransferUtilities.PACKET_SIZE.getPacketSizeValue();
    private DatagramChannel datagramChannel;
    private final SocketAddress socketAddress;

    /**
     * A constructor for the data transfer datagram connection module.
     *
     * @param datagramChannel the specified datagram channel
     * @param socketAddress the specified server address
     */

    protected UdpConnectionModule(DatagramChannel datagramChannel, SocketAddress socketAddress) {
        this.datagramChannel = datagramChannel;
        this.socketAddress = socketAddress;
    }

    /**
     * A method that returns the datagram channel.
     */

    protected DatagramChannel getDatagramChannel() {
        return this.datagramChannel;
    }

    /**
     * A method that connects the {@link UdpConnectionModule} to the server.
     *
     * @throws IOException if failed during I/O operations
     */

    @Override
    public void connect() throws IOException {
        if (!datagramChannel.isConnected() && datagramChannel.isOpen()) {
            datagramChannel.connect(socketAddress);
        }
    }

    /**
     * A method that disconnects the {@link UdpConnectionModule} from the server.
     *
     * @throws IOException if failed during I/O operations
     */

    @Override
    public void disconnect() throws IOException {
        if (datagramChannel.isConnected() && datagramChannel.isOpen()) {
            try {
                datagramChannel.disconnect();
                datagramChannel.close();
            } catch (IOException e) {
                datagramChannel.close();
            }
        }
    }

    /**
     * A method that sends the specified data.
     *
     * @param data data to send
     * @throws IOException if failed during I/O operations
     * @throws ServerUnavailableException if the server was unavailable during sending and receiving operations
     */

    @Override
    public void sendData(byte[] data) throws IOException, ServerUnavailableException {
        ByteBuffer buffer = ByteBuffer.allocate(PACKET_SIZE);
        buffer.put(data);
        buffer.flip();

        try {
            datagramChannel.send(buffer, socketAddress);
        } catch (PortUnreachableException e) {
            throw new ServerUnavailableException("Server is currently unavailable");
        }
    }

    /**
     * A method that receives data. Supports blocking and non-blocking datagram channel.
     *
     * @throws IOException if failed during I/O operations
     * @throws ServerUnavailableException if the server was unavailable during sending and receiving operations
     * @throws ResponseTimeoutException if client could not get response from the server during the given time
     */

    @Override
    public byte[] receiveData() throws IOException, ServerUnavailableException, ResponseTimeoutException {
        ByteBuffer buffer = ByteBuffer.allocate(PACKET_SIZE);
        byte[] data;

        if (datagramChannel.isBlocking()) {
            AtomicBoolean serverUnavailable = new AtomicBoolean(false);
            Semaphore semaphore = new Semaphore(0);

            try {
                Thread thread = new Thread(() -> {
                    try {
                        datagramChannel.receive(buffer);

                        semaphore.release();
                    } catch (IOException e) {
                        serverUnavailable.set(true);
                        Thread.currentThread().interrupt();
                    }
                });
                thread.start();

                boolean acquired = semaphore.tryAcquire(5, TimeUnit.SECONDS);
                if (!acquired) {
                    thread.interrupt();
                }
            } catch (InterruptedException e) {
                if (serverUnavailable.get()) {
                    throw new ServerUnavailableException("Server is currently unavailable");
                } else {
                    throw new ResponseTimeoutException("Could not get response from the server during the given time");
                }
            } finally {
                semaphore.release();
            }
        } else {
            boolean read = false;
            Selector selector = Selector.open();
            datagramChannel.register(selector, SelectionKey.OP_READ);

            int timeout = 4999;
            long startTime = System.currentTimeMillis();

            while (!read) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                long remainingTime = timeout - elapsedTime;

                if (remainingTime <= 0) {
                    throw new ResponseTimeoutException("Could not get response from the server during the given time");
                }

                int readyChannels;
                readyChannels = selector.select(remainingTime);
                if (readyChannels == 0) {
                    continue;
                }

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();

                    if (key.isReadable()) {
                        datagramChannel = (DatagramChannel) key.channel();
                        try {
                            datagramChannel.read(buffer);
                            read = true;
                        } catch (PortUnreachableException e) {
                            throw new ServerUnavailableException("Server is currently unavailable");
                        }
                    }

                    keyIterator.remove();
                }
            }
        }

        buffer.flip();
        data = new byte[buffer.remaining()];
        buffer.get(data);

        return data;
    }

}