package response.responses;

public class AuthorizationResponse implements Response {
    private final boolean success;
    private final String result;

    public AuthorizationResponse(boolean success, String result) {
        this.success = success;
        this.result = result;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public String getResult() {
        return this.result;
    }

}
