import interfaces.IAccountRepository;
import interfaces.ITicketRepository;
import repositories.databaseRepository.AccountDBRepository;
import repositories.databaseRepository.GameDBRepository;
import repositories.databaseRepository.TicketDBRepository;
import server.AbstractConcurrentServer;
import server.ObjectConcurrentServer;
import server.ServerException;
import interfaces.IGameRepository;
import service.IService;
import service.Service;
import service.ServiceException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MainServer {
    private static int defaultPort=55555;

    public static void main(String[] args) throws ServiceException {
        Properties serverProps=new Properties();
        try {
            serverProps.load(MainServer.class.getResourceAsStream("chatclient.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (Exception e) {
            System.err.println("Cannot find chatserver.properties "+e);
            return;
        }

        Properties databaseProps=new Properties();
        try {
            //to do
//            databaseProps.load(new FileReader("D:\\Programare\\MPP\\mpp-proiect-repository-mateimacovei\\lab_5_java_socket\\server\\bd.config.properties"));
//            databaseProps.load(new FileReader("./server/bd.config.properties"));
            databaseProps.load(new FileReader("bd.config.properties"));

        } catch (IOException e) {
            System.out.println("1. Cannot find bd.config "+e);
            return;
        }
        int chatServerPort=defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("chat.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+chatServerPort);

        IAccountRepository accountRepo = new AccountDBRepository(databaseProps);
        IGameRepository gamesRepo = new GameDBRepository(databaseProps);
        ITicketRepository ticketRepo = new TicketDBRepository(databaseProps);
        IService service = new Service(accountRepo,ticketRepo,gamesRepo);

        System.out.println(service.getAllGames().size());

        AbstractConcurrentServer server = new ObjectConcurrentServer(chatServerPort, service);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }
    }
}
