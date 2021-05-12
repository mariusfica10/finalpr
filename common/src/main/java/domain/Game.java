package domain;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

public class Game extends Entity<Integer>{
    private final String homeTeam;
    private final String awayTeam;
    private final Date date;
    private final Double ticketPrice;
    private Integer seatsAvailable;


    public Game(Integer id, String homeTeam, String awayTeam, Date date, Double ticketPrice, Integer seatsAvailable)
    {
        super(id);
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.date = date;
        this.ticketPrice = ticketPrice;
        this.seatsAvailable = seatsAvailable;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public Date getDate() {
        return date;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public Integer getSeatsAvailable() {
        return seatsAvailable;
    }

    public boolean getAvailable()
    {
        if (this.seatsAvailable == 0)
            return false;
        return true;
    }

    public boolean checkGame(int seats)
    {
        if (this.seatsAvailable - seats >= 0)
            return true;
        return false;
    }

    public void setSeats( int newValue)
    {
        this.seatsAvailable = newValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game game = (Game) o;
        return Objects.equals(getHomeTeam(), game.getHomeTeam()) &&
                Objects.equals(getAwayTeam(), game.getAwayTeam()) &&
                Objects.equals(getDate(), game.getDate()) &&
                Objects.equals(getTicketPrice(), game.getTicketPrice()) &&
                Objects.equals(getSeatsAvailable(), game.getSeatsAvailable());
    }

    @Override
    public String toString() {
        return "Game{" +
                "homeTeam='" + homeTeam + '\'' +
                ", awayTeam='" + awayTeam + '\'' +
                ", date=" + date +
                ", ticketPrice=" + ticketPrice +
                ", seatsAvailable=" + seatsAvailable +
                ", id=" + id +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getID());
    }
}
