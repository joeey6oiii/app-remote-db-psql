package requests;

import response.data.AuthenticationData;

import java.io.Serializable;

public class AuthorizationRequest implements Request, Serializable {
    private final AuthenticationData authenticationData;

    public AuthorizationRequest(AuthenticationData authenticationData) {
        this.authenticationData = authenticationData;
    }

    public AuthenticationData getAuthenticationData() {
        return this.authenticationData;
    }
}