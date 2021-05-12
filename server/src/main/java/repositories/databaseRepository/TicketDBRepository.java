package repositories.databaseRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import domain.Ticket;
import interfaces.ITicketRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TicketDBRepository implements ITicketRepository {

    private JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger(TicketDBRepository.class);

    public TicketDBRepository(Properties props)
    {
        logger.info("Initializing EmployeeRepository with properties: {} ",props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public void add(Ticket elem) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStat = con.prepareStatement("insert into Ticket (id, name, seats, gameID) values (?,?,?,?)")) {
            preStat.setInt(1,elem.getID());
            preStat.setString(2, elem.getName());
            preStat.setInt(3, elem.getSeats());
            preStat.setInt(4, elem.getGameID());
            int result = preStat.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error db" + ex);
        }
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Ticket entity) {

    }

    @Override
    public Optional<Ticket> findOne(Integer integer) {

        Connection con = dbUtils.getConnection();
        Optional<Ticket> ticket=Optional.empty();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Account where id=?")) {
            preStmt.setInt(1, integer);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next() && ticket.isEmpty()) {

                    String name = result.getString("name");
                    int seats = result.getInt("seats");
                    int gameID = result.getInt("gameID");

                    ticket = Optional.of(new Ticket(integer,name,seats,gameID));
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }

        return ticket;
    }

    @Override
    public List<Ticket> findAll()
    {
        Connection con = dbUtils.getConnection();
        List<Ticket> tickets = new ArrayList<>();
        try(PreparedStatement preStat = con.prepareStatement("select * from Ticket")){
            try(ResultSet result = preStat.executeQuery())
            {
                while(result.next())
                {
                    int id = result.getInt("id");
                    String name = result.getString("name");
                    Integer seats = result.getInt("seats");
                    Integer gameID = result.getInt("gameID");
                    Ticket ticket = new Ticket(id, name, seats, gameID);
                    ticket.setID(id);
                    tickets.add(ticket);
                }
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return tickets;
    }


    @Override
    public List<Ticket> findByName(String name) {
        return null;
    }
}
