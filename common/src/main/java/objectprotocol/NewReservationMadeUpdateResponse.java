package objectprotocol;

import domain.Game;

public class NewReservationMadeUpdateResponse implements UpdateResponse{
    private Game game;

    public NewReservationMadeUpdateResponse(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
