package objectprotocol;

public class ResponseError implements Response {
    private String message;

    public ResponseError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
