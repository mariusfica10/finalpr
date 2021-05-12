package objectprotocol;


import domain.Game;

public class GetAllGamesResponse implements Response{
    private Game[] games;

    public GetAllGamesResponse(Game[] games) {
        this.games = games;
    }

    public Game[] getGames() {
        return games;
    }
}