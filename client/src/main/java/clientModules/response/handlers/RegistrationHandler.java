package clientModules.response.handlers;

import response.responses.RegistrationResponse;

public class RegistrationHandler implements ResponseHandler<RegistrationResponse> {

    @Override
    public boolean handleResponse(RegistrationResponse registrationResponse) {
        System.out.println(registrationResponse.getResult());
        return registrationResponse.isSuccess();
    }

}
