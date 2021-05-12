package observer;

import domain.Game;

import java.io.IOException;

public interface IObserver {
    void seatsReserved(Game game) throws IOException;
}
