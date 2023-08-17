package requests;

import response.data.AuthenticationData;

import java.io.Serializable;

public class RegistrationRequest implements Request, Serializable {
    private final AuthenticationData authenticationData;

    public RegistrationRequest(AuthenticationData authenticationData) {
        this.authenticationData = authenticationData;
    }

    public AuthenticationData getAuthenticationData() {
        return this.authenticationData;
    }
}