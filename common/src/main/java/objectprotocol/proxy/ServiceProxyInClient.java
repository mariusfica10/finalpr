package objectprotocol.proxy;


import domain.Account;
import domain.Game;
import domain.Ticket;
import objectprotocol.*;
import service.IService;
import service.ServiceException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ServiceProxyInClient implements IService {
    private final String host;
    private final int port;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private final BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ServiceProxyInClient(String host, int port) throws ServiceException {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<>();
        initializeConnection();
    }

    @Override
    public Optional<Account> findEmployee(String userName, String password) throws ServiceException {
        sendRequest(new LoginRequest(userName, password));
        Response response = readResponse();
        if (response instanceof LoginResponse)
            return ((LoginResponse) response).getEmployee();
        if (response instanceof ResponseError) {
            ResponseError er = (ResponseError) response;
            closeConnection();
            throw new ServiceException(er.getMessage());
        }
        return Optional.empty();
    }

    public List<Game> getAllGames() throws ServiceException {
        sendRequest(new GetAllGamesRequest());
        Response response = readResponse();
        if (response instanceof ResponseError) {
            ResponseError er = (ResponseError) response;
            throw new ServiceException(er.getMessage());
        }
        GetAllGamesResponse resp = (GetAllGamesResponse) response;
        var games = resp.getGames();
        return Arrays.asList(games);
    }

    @Override
    public List<Game> getAvailableGames() throws ServiceException {
        return null;
    }

    @Override
    public void addTicket(int id, String name, Integer seats, Integer gameID) throws ServiceException {
        sendRequest(new NewReservationRequest(id, name, seats, gameID));
        Response response = readResponse();
        if (response instanceof ResponseError) {
            ResponseError er = (ResponseError) response;
            throw new ServiceException(er.getMessage());
        }
        if (response instanceof NewReservationSeatsNotAvailable) {
            NewReservationSeatsNotAvailable er = (NewReservationSeatsNotAvailable) response;
            throw new ServiceException(er.getMessage());
        }
    }

    @Override
    public List<Ticket> getAllTickets() {
        return null;
    }

    @Override
    public void addGame(int id, String homeTeam, String awayTeam, Date date, Double ticketPrice, Integer seatsAvailable) {

    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            sendRequest(new CloseRequest());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    private void sendRequest(Request request) throws ServiceException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new ServiceException("Error sending object " + e);
        }

    }

    private Response readResponse() {
        Response response = null;
        try {
            response = qresponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(UpdateResponse update) {
        if (update instanceof NewReservationMadeUpdateResponse) {
            NewReservationMadeUpdateResponse reservationMadeResponse = (NewReservationMadeUpdateResponse) update;
            notifyObservers(reservationMadeResponse.getGame());
        }
    }


    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("response received " + response);
                    if (response instanceof UpdateResponse) {
                        handleUpdate((UpdateResponse) response);
                    } else {
                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }
}
