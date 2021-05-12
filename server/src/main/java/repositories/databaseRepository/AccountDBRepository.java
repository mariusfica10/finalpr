package repositories.databaseRepository;
import domain.Account;
import interfaces.IAccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repositories.databaseRepository.JdbcUtils;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountDBRepository implements IAccountRepository {


    private JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger(AccountDBRepository.class);

    public AccountDBRepository(Properties props)
    {
        logger.info("Initializing EmployeeRepository with properties: {} ",props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public void add(Account elem) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStat = con.prepareStatement("insert into Account (id, username, password) values (?,?,?)")) {
            preStat.setInt(1,elem.getID());
            preStat.setString(2, elem.getUsername());
            preStat.setString(3, elem.getPassword());
            int result = preStat.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error db" + ex);
        }
    }

    @Override
    public Optional<Account> findOne(Integer integer) {
        Connection con = dbUtils.getConnection();
        Optional<Account> account=Optional.empty();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Account where id=?")) {
            preStmt.setInt(1, integer);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next() && account.isEmpty()) {

                    String username = result.getString("username");
                    String password = result.getString("password");

                    account = Optional.of(new Account(integer,username,password));
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }

        return account;
    }

    @Override
    public List<Account> findAll() {
        Connection con = dbUtils.getConnection();
        List<Account> accounts = new ArrayList<>();
        try(PreparedStatement preStat = con.prepareStatement("select * from Account")){
            try(ResultSet result = preStat.executeQuery())
            {
                while(result.next())
                {
                    int id = result.getInt("id");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    Account account = new Account(username, password);
                    account.setID(id);
                    accounts.add(account);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println(ex);
        }
        logger.traceExit();
        return accounts;
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Account entity) {

    }

    @Override
    public Optional<Account> findForLogin(String username, String password) {
        Connection con = dbUtils.getConnection();
        Optional<Account> account=Optional.empty();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Account where username=? AND password=?")) {
            preStmt.setString(1, username);
            preStmt.setString(2, password);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next() && account.isEmpty()) {

                    int id = result.getInt("id");
                    String name = result.getString("username");

                    account = Optional.of(new Account(id,username,password));
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }

        return account;

    }
}
