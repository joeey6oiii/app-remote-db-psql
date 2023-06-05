package clientModules.request.sender;

import clientModules.connection.DataTransferConnectionModule;
import clientModules.response.reader.ResponseReader;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import utils.HeaderParser;
import utils.ResponseAssembler;
import utils.ResponseDataParser;
import requests.Request;
import response.data.FragmentHeader;
import response.responses.Response;
import serializer.ObjectSerializer;

import java.io.IOException;
import java.util.HashMap;

/**
 * A class that represents the base request sender.
 */

public class RequestSender implements RequestAble<Response, Request> {

    /**
     * A method that serializes the request and sends it to the server. After, receives chunks, collects into one
     * serialized response, deserializes and returns the read response.
     *
     * @param dataTransferConnectionModule server core
     * @param request request
     * @return base response
     */

    @Override
    public Response sendRequest(DataTransferConnectionModule dataTransferConnectionModule, Request request) throws IOException, ServerUnavailableException, ResponseTimeoutException {
        Response response = null;
        HashMap<Integer, byte[]> chunks = new HashMap<>();
        ObjectSerializer serializer = new ObjectSerializer();

        HeaderParser headerParser = new HeaderParser();
        ResponseDataParser responseDataParser = new ResponseDataParser();
        FragmentHeader header;

        try {
            dataTransferConnectionModule.sendData(serializer.serialize(request));

            do {
                byte[] data = dataTransferConnectionModule.receiveData();
                header = headerParser.parseHeader(data);
                byte[] partOfResponseData = responseDataParser.extractResponseData(data);

                chunks.put(header.getPacketIndex(), partOfResponseData);

            } while (header.getPacketIndex() != -1);

            byte[] responseData = new ResponseAssembler().combineResponseParts(chunks);
            response = new ResponseReader().readResponse(responseData);
        } catch (IllegalArgumentException e) {
            System.out.println("Response part is missing");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not find response class");
        }

        return response;
    }

}
