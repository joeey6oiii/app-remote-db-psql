package clientModules.response.reader;

import exceptions.ServerUnavailableException;
import response.responses.Response;
import serializer.ObjectSerializer;

import java.io.IOException;

/**
 * A class that represents the base response reader.
 */

public class ResponseReader implements ResponseReadAble<Response> {

    /**
     * A method that reads response from the received data byte array.
     *
     * @param data response to deserialize
     * @throws IOException if failed during I/O operations
     * @throws ClassNotFoundException when could not create a response object after deserialization
     * @throws ServerUnavailableException if the server was unavailable during sending and receiving operations
     * @return response
     */

    @Override
    public Response readResponse(byte[] data) throws IOException, ClassNotFoundException, ServerUnavailableException {
        ObjectSerializer serializer = new ObjectSerializer();
        Response response = (Response) serializer.deserialize(data);

        if (response == null) {
            throw new ServerUnavailableException("Server is currently unavailable");
        }

        return response;
    }

}
