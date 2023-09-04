package clientModules.response.handlers;

import outputService.ColoredPrintStream;
import outputService.MessageType;
import outputService.OutputSource;
import response.responses.CommandExecutionResponse;

/**
 * A class that works with the command execution result response.
 */
public class ExecutionResultHandler implements ResponseHandler<CommandExecutionResponse> {

    /**
     * A method that handles the command execution result response and outputs the result.
     *
     * @param response the received response
     */
    @Override
    public boolean handleResponse(CommandExecutionResponse response) {
        ColoredPrintStream cps = new ColoredPrintStream(OutputSource.getOutputStream());
        
        if (response != null) {
            cps.println(cps.formatMessage(MessageType.INFO, response.getResult()));
        } else {
            cps.println(cps.formatMessage(MessageType.WARNING, "Received invalid response from server"));
            return false;
        }

        return true;
    }
}