package objectprotocol;

public class NewReservationRequest implements Request{
    private Integer id;
    private String name;
    private Integer nrOfSeats;
    private Integer gameID;

    public NewReservationRequest(Integer id,String name, Integer nrOfSeats, Integer gameID) {
        this.id = id;
        this.name = name;
        this.nrOfSeats = nrOfSeats;
        this.gameID = gameID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNrOfSeats() {
        return nrOfSeats;
    }

    public void setNrOfSeats(Integer nrOfSeats) {
        this.nrOfSeats = nrOfSeats;
    }

    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }
}
