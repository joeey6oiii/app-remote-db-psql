package serializer;

import java.io.*;

/**
 * A class that provides serialization and deserialization methods.
 */

public class ObjectSerializer implements SerializeObjectAble<byte[], Object> {

    /**
     * A method to serialize an object to a byte array.
     *
     * @param object object to serialize
     * @throws IOException if failed during I/O operations
     */

    @Override
    public byte[] serialize(Object object) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        oos.flush();
        byte[] data = baos.toByteArray();
        oos.close();
        baos.close();
        return data;
    }

    /**
     * A method to deserialize a byte array to an object.
     *
     * @param data data to deserialize
     * @throws IOException if failed during I/O operations
     * @throws ClassNotFoundException when could not create object after deserialization
     */

    @Override
    public Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object object = ois.readObject();
        ois.close();
        bais.close();
        return object;
    }

}
