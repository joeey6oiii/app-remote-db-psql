package clientModules.response.handlers;

import response.responses.ErrorResponse;

public class ServerErrorResultHandler implements ResponseHandler<ErrorResponse> {

    @Override
    public boolean handleResponse(ErrorResponse response) {
        if (response != null) {
            System.out.println(response.getResult());
        } else {
            System.out.println("Received invalid response from server");
            return false;
        }

        return true;
    }

}
