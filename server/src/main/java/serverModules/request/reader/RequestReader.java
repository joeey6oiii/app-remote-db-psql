package serverModules.request.reader;

import requests.Request;
import serializer.ObjectSerializer;

import java.io.IOException;

/**
 * A class that represents the base request reader.
 */

public class RequestReader implements RequestReadAble<Request> {

    /**
     * A method that reads request from the received data byte array.
     *
     * @param data request to deserialize
     * @throws IOException if failed during I/O operations
     * @throws ClassNotFoundException when could not create request object after deserialization
     * @return request
     */

    @Override
    public Request readRequest(byte[] data) throws IOException, ClassNotFoundException {
        ObjectSerializer serializer = new ObjectSerializer();

        return (Request) serializer.deserialize(data);
    }

}