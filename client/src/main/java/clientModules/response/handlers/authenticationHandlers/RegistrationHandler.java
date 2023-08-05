package clientModules.response.handlers.authenticationHandlers;

import clientModules.response.handlers.ResponseHandler;
import utility.Token;
import response.responses.RegistrationResponse;

public class RegistrationHandler implements ResponseHandler<RegistrationResponse> {

    @Override
    public boolean handleResponse(RegistrationResponse registrationResponse) {
        if (registrationResponse == null) {
            return false;
        }

        boolean isSuccess = registrationResponse.isSuccess();

        if (isSuccess) {
            Token userCurrentTemporaryToken = registrationResponse.getToken();

            if (userCurrentTemporaryToken == null) {
                return false;
            }
            User.setToken(userCurrentTemporaryToken);
        }

        System.out.println(registrationResponse.getResult());

        return isSuccess;
    }
}