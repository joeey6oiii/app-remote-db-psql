package response.responses;

public class AuthorizationResponse implements Response {
    private final boolean success;
    private final String response;

    public AuthorizationResponse(boolean success, String response) {
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
