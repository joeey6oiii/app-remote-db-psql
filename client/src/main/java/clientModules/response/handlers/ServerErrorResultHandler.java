package clientModules.response.handlers;

import outputService.ColoredPrintStream;
import outputService.MessageType;
import outputService.OutputSource;
import response.responses.ErrorResponse;

public class ServerErrorResultHandler implements ResponseHandler<ErrorResponse> {

    @Override
    public boolean handleResponse(ErrorResponse response) {
        ColoredPrintStream cps = new ColoredPrintStream(OutputSource.getOutputStream());

        if (response != null) {
            cps.println(cps.formatMessage(MessageType.INFO, response.getResult()));
        } else {
            cps.println(cps.formatMessage(MessageType.WARNING,"Received invalid response from server"));
            return false;
        }

        return true;
    }
}