package service;

import domain.Account;
import domain.Game;
import domain.Ticket;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import interfaces.IAccountRepository;
import interfaces.IGameRepository;
import interfaces.ITicketRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Service implements IService{

    private final IAccountRepository repoAccount;
    private final ITicketRepository repoTicket;
    private final IGameRepository repoGame;
    private static final Logger logger = LogManager.getLogger(Service.class);

    public Service(IAccountRepository repoAccount, ITicketRepository repoTicket, IGameRepository repoGame) {
        this.repoAccount = repoAccount;
        this.repoGame = repoGame;
        this.repoTicket = repoTicket;
    }


    @Override
    public Optional<Account> findEmployee(String userName, String password) {
        return repoAccount.findForLogin(userName, password);
    }

    @Override
    public void close() {

    }

    @Override
    public List<Game> getAllGames() {
        return repoGame.findAll();
    }

    @Override
    public List<Game> getAvailableGames() {
        return repoGame.getSortedAvailableGames();
    }

    public void addGame(int id, String homeTeam, String awayTeam, Date date, Double ticketPrice, Integer seatsAvailable) {
        repoGame.add(new Game(id, homeTeam, awayTeam, date, ticketPrice, seatsAvailable));
    }

    public void addTicket(int id, String name, Integer seats, Integer gameID ) {
        Game game = repoGame.getGame(gameID);
        game.setSeats(game.getSeatsAvailable()-seats);
        repoGame.update(game);
        repoTicket.add(new Ticket(id, name, seats, gameID));
        notifyObservers(game);
    }

    public void addAccount(int id, String username, String password) {
        repoAccount.add(new Account(id, username, password));
    }

    public List<Account> getAllAcoounts()
    {
        return repoAccount.findAll();
    }

    public List<Ticket> getAllTickets()
    {
        return repoTicket.findAll();
    }

}
