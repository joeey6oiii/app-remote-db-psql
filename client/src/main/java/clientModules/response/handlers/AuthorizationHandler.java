package clientModules.response.handlers;

import response.responses.AuthorizationResponse;

public class AuthorizationHandler implements ResponseHandler<AuthorizationResponse> {

    @Override
    public boolean handleResponse(AuthorizationResponse authorizationResponse) {
        System.out.println(authorizationResponse.getResult());
        return authorizationResponse.isSuccess();
    }

}
