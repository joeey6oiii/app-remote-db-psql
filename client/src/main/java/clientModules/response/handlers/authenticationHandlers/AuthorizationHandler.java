package clientModules.response.handlers.authenticationHandlers;

import clientModules.authentication.User;
import clientModules.response.handlers.ResponseHandler;
import response.responses.AuthorizationResponse;
import token.Token;

public class AuthorizationHandler implements ResponseHandler<AuthorizationResponse> {

    @Override
    public boolean handleResponse(AuthorizationResponse authorizationResponse) {
        if (authorizationResponse == null) {
            System.out.println("Received invalid response from server");
            return false;
        }

        System.out.println(authorizationResponse.getResult());

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