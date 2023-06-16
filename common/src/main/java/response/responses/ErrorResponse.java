package response.responses;

public class ErrorResponse implements Response {
    private final String result;

    public ErrorResponse(String result) {
        this.result = result;
    }

    public String getResult() {
        return this.result;
    }

}
