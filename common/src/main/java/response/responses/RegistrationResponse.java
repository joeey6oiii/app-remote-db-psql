package response.responses;

public class RegistrationResponse implements Response {
    private final boolean success;
    private final String result;

    public RegistrationResponse(boolean success, String result) {
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
