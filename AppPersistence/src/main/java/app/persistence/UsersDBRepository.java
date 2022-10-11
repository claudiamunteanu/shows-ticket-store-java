package app.persistence;

import app.model.User;
import app.persistence.interfaces.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Component
public class UsersDBRepository implements UserRepository {
    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public UsersDBRepository(Properties props) {
        logger.info("Initializing UsersDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public User findOne(Long id) {
        if (id == null) {
            String error = "ID cannot be null!";
            logger.error(error);
            throw new IllegalArgumentException(error);
        }

        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        User user = null;
        try (PreparedStatement preStmt = con.prepareStatement("select * from Users where Uid=" + id.toString())) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int Uid = result.getInt("Uid");
                    String username = result.getString("Username");
                    String password = result.getString("Password");

                    user = new User(username, password);
                    user.setId((long) Uid);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return user;
    }

    @Override
    public List<User> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<User> users = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Users")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int Uid = result.getInt("Uid");
                    String username = result.getString("Username");
                    String password = result.getString("Password");

                    User user = new User(username, password);
                    user.setId((long) Uid);
                    users.add(user);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return users;
    }

    @Override
    public User save(User entity) {
        if (entity == null) {
            String error = "Entity cannot be null!";
            logger.error(error);
            throw new IllegalArgumentException(error);
        }

        logger.traceEntry("saving show {} ", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Users (Username, Password) values (?,?)")) {
            preStmt.setString(1, entity.getUsername());
            preStmt.setString(2, entity.getPassword());

            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
            return entity;
        }
        logger.traceExit();
        return null;
    }

    @Override
    public User getUser(String username, String password) {
        if (username == null || password == null) {
            String error = "Username and password cannot be null!";
            logger.error(error);
            throw new IllegalArgumentException(error);
        }

        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        User user = null;
        try (PreparedStatement preStmt = con.prepareStatement("select * from Users where Username='" + username + "' and Password='" + password + "'")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int Uid = result.getInt("Uid");
                    String username2 = result.getString("Username");
                    String password2 = result.getString("Password");

                    user = new User(username2, password2);
                    user.setId((long) Uid);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return user;
    }
}
