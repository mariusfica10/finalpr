package domain;

import java.util.Objects;

public class Ticket extends Entity<Integer>{

    private final String name;
    private final Integer seats;
    private final Integer gameID; // obiect


    public Ticket(Integer id, String name, Integer seats, Integer gameID)
    {
        super(id);
        this.name = name;
        this.seats = seats;
        this.gameID = gameID;
    }

    public String getName() {
        return name;
    }

    public Integer getSeats() {
        return seats;
    }

    public Integer getGameID() {
        return gameID;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "name='" + name + '\'' +
                ", seats=" + seats +
                ", gameID=" + gameID +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket)) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(getName(), ticket.getName()) &&
                Objects.equals(getSeats(), ticket.getSeats()) &&
                Objects.equals(getGameID(), ticket.getGameID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getID());
    }
}
