package serializer;

import java.io.IOException;

/**
 * An interface for all ObjectSerializer-implementers.
 *
 * @param <T> the value to return after serialization
 * @param <V> the value to return after deserialization
 */

public interface SerializeObjectAble<T, V> {

    /**
     * A method to serialize V to a T.
     *
     * @param v object to serialize
     * @throws IOException if failed during I/O operations
     */

    T serialize(V v) throws IOException;

    /**
     * A method to deserialize T to a V.
     *
     * @param t data to deserialize
     * @throws IOException if failed during I/O operations
     * @throws ClassNotFoundException when could not create V after deserialization
     */

    V deserialize(T t) throws IOException, ClassNotFoundException;
}
