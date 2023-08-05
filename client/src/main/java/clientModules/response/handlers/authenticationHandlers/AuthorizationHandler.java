package clientModules.response.handlers.authenticationHandlers;

import clientModules.response.handlers.ResponseHandler;
import utility.Token;
import response.responses.AuthorizationResponse;

public class AuthorizationHandler implements ResponseHandler<AuthorizationResponse> {

    @Override
    public boolean handleResponse(AuthorizationResponse authorizationResponse) {
        if (authorizationResponse == null) {
            return false;
        }

        boolean isSuccess = authorizationResponse.isSuccess();

        if (isSuccess) {
            Token userCurrentTemporaryToken = authorizationResponse.getToken();

            if (userCurrentTemporaryToken == null) {
                return false;
            }
            User.setToken(userCurrentTemporaryToken);
        }

        System.out.println(authorizationResponse.getResult());

        return isSuccess;
    }
}