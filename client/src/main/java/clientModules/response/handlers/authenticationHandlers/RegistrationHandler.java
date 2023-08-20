package clientModules.response.handlers.authenticationHandlers;

import clientModules.authentication.User;
import clientModules.response.handlers.ResponseHandler;
import response.responses.RegistrationResponse;
import token.Token;

public class RegistrationHandler implements ResponseHandler<RegistrationResponse> {

    @Override
    public boolean handleResponse(RegistrationResponse registrationResponse) {
        if (registrationResponse == null) {
            System.out.println("Received invalid response from server");
            return false;
        }

        System.out.println(registrationResponse.getResult());

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