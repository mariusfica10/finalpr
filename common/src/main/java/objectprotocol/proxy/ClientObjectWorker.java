package objectprotocol.proxy;

import domain.Game;
import objectprotocol.*;
import observer.IObserver;
import service.IService;
import service.ServiceException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ClientObjectWorker implements Runnable, IObserver {
    private final IService server;
    private final Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientObjectWorker(IService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        this.server.addObserver(this);
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeAll()
    {
        try {
            input.close();
            output.close();
            server.removeObserver(this);
            connected=false;
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Object response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse((Response) response);
                }
            } catch (IOException | ClassNotFoundException | ServiceException e) {
                e.printStackTrace();
                connected=false;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        closeAll();
    }

    private Response handleRequest(Request request) throws ServiceException {
        Response response=null;
        if (request instanceof CloseRequest)
        {
            server.removeObserver(this);
            connected=false;
        }
        if (request instanceof LoginRequest){
            System.out.println("Login request ...");
            LoginRequest logReq=(LoginRequest)request;
            var username = logReq.getUsername();
            var password = logReq.getPassword();

            var employee = server.findEmployee(username,password);
            return new LoginResponse(employee);
        }
        if (request instanceof GetAllGamesRequest){
            GetAllGamesRequest req= (GetAllGamesRequest)request;
            var games = server.getAllGames();
            var gamesArray = new Game[games.size()];
            gamesArray = games.toArray(gamesArray);
            return new GetAllGamesResponse(gamesArray);
        }

        if (request instanceof NewReservationRequest)
        {
            NewReservationRequest req= (NewReservationRequest)request;
            server.addTicket(req.getId(), req.getName(), req.getNrOfSeats(), req.getGameID());
            return new ResponseOK();
        }
        return response;
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }

    @Override
    public void seatsReserved(Game game) throws IOException {
        sendResponse(new NewReservationMadeUpdateResponse(game));
    }
}