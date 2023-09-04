package clientModules.response.handlers.authenticationHandlers;

import clientModules.authentication.User;
import clientModules.response.handlers.ResponseHandler;
import outputService.ColoredPrintStream;
import outputService.MessageType;
import outputService.OutputSource;
import response.responses.AuthorizationResponse;
import token.Token;

public class AuthorizationHandler implements ResponseHandler<AuthorizationResponse> {

    @Override
    public boolean handleResponse(AuthorizationResponse authorizationResponse) {
        ColoredPrintStream cps = new ColoredPrintStream(OutputSource.getOutputStream());

        if (authorizationResponse == null) {
            cps.println(cps.formatMessage(MessageType.WARNING, "Received invalid response from server"));
            return false;
        }

        cps.println(cps.formatMessage(MessageType.INFO, authorizationResponse.getResult()));

        boolean isSuccess = authorizationResponse.isSuccess();
        if (isSuccess) {
            Token<?> token = authorizationResponse.getToken();

            if (token == null || token.getTokenValue() == null) {
                return false;
            }
            User.getInstance().setToken(token);
        }

        return isSuccess;
    }
}