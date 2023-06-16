package response.responses;

import java.io.Serializable;

public class ErrorResponse implements Response, Serializable {
    private final String result;

    public ErrorResponse(String result) {
        this.result = result;
    }

    public String getResult() {
        return this.result;
    }

}
