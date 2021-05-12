package objectprotocol;

public class NewReservationSeatsNotAvailable implements Response{
    private String message;

    public NewReservationSeatsNotAvailable(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
