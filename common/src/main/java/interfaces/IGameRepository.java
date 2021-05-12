package interfaces;
import domain.Game;

import java.util.List;

public interface IGameRepository extends IRepository<Integer, Game> {
    List<Game> getSortedAvailableGames();
    void setTicketsForGame(Integer gameId, Integer ticketsNumber);
    Game getGame(int id);
}