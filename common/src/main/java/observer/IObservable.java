package observer;

import domain.Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface IObservable {
    List<IObserver> observers = new ArrayList<>();

    default void addObserver(IObserver e) {
        synchronized (observers) {
            observers.add(e);
        }
    }

    default void removeObserver(IObserver e) {
        synchronized (observers) {
            observers.remove(e);
        }
    }

    default void notifyObservers(Game game) {
        synchronized (observers) {
            observers.forEach(x -> {
                try {
                    x.seatsReserved(game);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
