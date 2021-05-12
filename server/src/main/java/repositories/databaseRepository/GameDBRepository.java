package repositories.databaseRepository;

import domain.Game;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import interfaces.IGameRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameDBRepository implements IGameRepository {

    private JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger(GameDBRepository.class);

    public GameDBRepository(Properties props)
    {

        logger.info("Initializing EmployeeRepository with properties: {} ",props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public void add(Game elem) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStat = con.prepareStatement("insert into Game (id, homeTeam, awayTeam, date, ticketPrice, seatsAvailable) values (?,?,?,?,?,?)")) {
            preStat.setInt(1,elem.getID());
            preStat.setString(2, elem.getHomeTeam());
            preStat.setString(3, elem.getAwayTeam());
            java.util.Date utilStartDate = elem.getDate();
            java.sql.Date sqlStartDate = new java.sql.Date(utilStartDate.getTime());
            preStat.setDate(4, sqlStartDate);
            preStat.setDouble(5, elem.getTicketPrice());
            preStat.setInt(6, elem.getSeatsAvailable());

            int result = preStat.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error db" + ex);
        }
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Game game) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("UPDATE Game SET homeTeam=?,awayTeam=?,date=?,ticketPrice=?,seatsAvailable=? WHERE id=?"))
        {

            preStmt.setString(1,game.getHomeTeam());
            preStmt.setString(2,game.getAwayTeam());
            java.util.Date utilStartDate = game.getDate();
            java.sql.Date sqlStartDate = new java.sql.Date(utilStartDate.getTime());
            preStmt.setDate(3,sqlStartDate);
            preStmt.setDouble(4,game.getTicketPrice());
            preStmt.setInt(5,game.getSeatsAvailable());
            preStmt.setInt(6,game.getID());
            var rowsAff = preStmt.executeUpdate();
            logger.trace("update affected {} rows",rowsAff);
            if(rowsAff==0)
                logger.warn("update affected 0 rows: id {}",game.getID());
        }
        catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
    }

    @Override
    public Optional<Game> findOne(Integer id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        Optional<Game> game = Optional.empty();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Game where id=?")) {
            preStmt.setInt(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next() && game.isEmpty()) {
                    String homeTeam = result.getString("homeTeam");
                    String awayTeam = result.getString("awayTeam");
                    Date date = result.getDate("date");
                    Double ticketPrice = result.getDouble("ticketPrice");
                    int seatsAvailable = result.getInt("seatsAvailable");

                    game = Optional.of(new Game(id, homeTeam, awayTeam, date, ticketPrice, seatsAvailable));
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }

        logger.traceExit();
        return game;
    }

    @Override
    public List<Game> findAll() {
        Connection con = dbUtils.getConnection();
        List<Game> games = new ArrayList<>();
        try(PreparedStatement preStat = con.prepareStatement("select * from Game")){
            try(ResultSet result = preStat.executeQuery())
            {
                while(result.next())
                {
                    int id = result.getInt("id");
                    String homeTeam = result.getString("homeTeam");
                    String awayTeam = result.getString("awayTeam");
                    Date date = result.getDate("date");
                    Double ticketPrice = result.getDouble("ticketPrice");
                    int seatsAvailable = result.getInt("seatsAvailable");
                    Game game = new Game(id, homeTeam, awayTeam, date, ticketPrice, seatsAvailable);
                    game.setID(id);
                    games.add(game);
                }
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return games;
    }

    @Override
    public List<Game> getSortedAvailableGames() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Game>games =new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Game where seatsAvailable>0 order by seatsAvailable desc")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String homeTeam = result.getString("homeTeam");
                    String awayTeam = result.getString("awayTeam");
                    Date date = result.getDate("date");
                    Double ticketPrice = result.getDouble("ticketPrice");
                    int seatsAvailable = result.getInt("seatsAvailable");

                    games.add(new Game(id,homeTeam,awayTeam,date,ticketPrice,seatsAvailable));
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }

        logger.traceExit();
        return games;
    }

    @Override
    public void setTicketsForGame(Integer gameId, Integer ticketsNumber) {
    }

    @Override
    public Game getGame(int id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        Game game = null;
        try (PreparedStatement preStmt = con.prepareStatement("select * from Game where id=?")) {
            preStmt.setInt(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    String homeTeam = result.getString("homeTeam");
                    String awayTeam = result.getString("awayTeam");
                    Date date = result.getDate("date");
                    Double ticketPrice = result.getDouble("ticketPrice");
                    int seatsAvailable = result.getInt("seatsAvailable");

                    game = new Game(id, homeTeam, awayTeam, date, ticketPrice, seatsAvailable);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }

        logger.traceExit();
        return game;
    }
}
