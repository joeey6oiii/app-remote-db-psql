package clientModules.response.handlers;

import outputService.ColoredPrintStream;
import outputService.MessageType;
import outputService.OutputSource;
import response.responses.CommandExecutionResponse;

/**
 * A class that works with the "exit" command execution result response.
 */
public class ExitCommandHandler implements ResponseHandler<CommandExecutionResponse> {

    /**
     * A method that handles the "exit" command execution result response and ends the program.
     *
     * @param response the received response
     */
    @Override
    public boolean handleResponse(CommandExecutionResponse response) {
        ColoredPrintStream cps = new ColoredPrintStream(OutputSource.getOutputStream());

        cps.println(cps.formatMessage(MessageType.INFO, "Shutdown..."));
        System.exit(0);

        return true;
    }
}