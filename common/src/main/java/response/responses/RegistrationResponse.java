package response.responses;

public class RegistrationResponse implements Response {
    private final boolean success;
    private final String response;

    public RegistrationResponse(boolean success, String response) {
        this.success = success;
        this.response = response;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public String getResponse() {
        return this.response;
    }

}
