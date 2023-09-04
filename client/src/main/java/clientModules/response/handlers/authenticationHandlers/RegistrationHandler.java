package clientModules.response.handlers.authenticationHandlers;

import clientModules.authentication.User;
import clientModules.response.handlers.ResponseHandler;
import outputService.ColoredPrintStream;
import outputService.MessageType;
import outputService.OutputSource;
import response.responses.RegistrationResponse;
import token.Token;

public class RegistrationHandler implements ResponseHandler<RegistrationResponse> {

    @Override
    public boolean handleResponse(RegistrationResponse registrationResponse) {
        ColoredPrintStream cps = new ColoredPrintStream(OutputSource.getOutputStream());

        if (registrationResponse == null) {
            cps.println(cps.formatMessage(MessageType.WARNING, "Received invalid response from server"));
            return false;
        }

        cps.println(cps.formatMessage(MessageType.INFO, registrationResponse.getResult()));

        boolean isSuccess = registrationResponse.isSuccess();
        if (isSuccess) {
            Token<?> token = registrationResponse.getToken();

            if (token == null || token.getTokenValue() == null) {
                return false;
            }
            User.getInstance().setToken(token);
        }

        return isSuccess;
    }
}