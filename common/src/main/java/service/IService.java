package service;

import domain.Account;
import domain.Game;
import domain.Ticket;
import observer.IObservable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IService extends IObservable {

    Optional<Account> findEmployee(String userName, String password) throws ServiceException;

    void close();

    List<Game> getAllGames() throws ServiceException;

    List<Game> getAvailableGames() throws ServiceException;

    void addTicket(int id, String name, Integer seats, Integer gameID ) throws ServiceException;

    List<Ticket> getAllTickets();

    void addGame(int id, String homeTeam, String awayTeam, Date date, Double ticketPrice, Integer seatsAvailable);
}
