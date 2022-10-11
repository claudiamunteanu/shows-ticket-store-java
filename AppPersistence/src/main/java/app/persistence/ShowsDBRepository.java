package app.persistence;

import app.model.Show;
import app.persistence.interfaces.ShowRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Component
@PropertySource("classpath:jdbc.properties")
public class ShowsDBRepository implements ShowRepository {

    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    public ShowsDBRepository(@Value("${property.jdbcProps}") Properties props) {
        logger.info("Initializing ShowsDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Show findOne(Long id) {
        if (id == null) {
            String error = "ID cannot be null!";
            logger.error(error);
            throw new IllegalArgumentException(error);
        }

        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        Show show = null;
        try (PreparedStatement preStmt = con.prepareStatement("select * from Shows where Sid=" + id.toString())) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int Sid = result.getInt("Sid");
                    String artistName = result.getString("ArtistName");
                    long dateTimeString = result.getLong("DateTime");
                    String place = result.getString("Place");
                    int availableSeats = result.getInt("AvailableSeats");
                    int soldSeats = result.getInt("SoldSeats");

                    Date date = new Date(dateTimeString);
                    LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                    show = new Show(artistName, localDateTime, place, availableSeats, soldSeats);
                    show.setId((long) Sid);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return show;
    }

    @Override
    public List<Show> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Show> shows = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Shows")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int Sid = result.getInt("Sid");
                    String artistName = result.getString("ArtistName");
                    long dateTimeString = result.getLong("DateTime");
                    String place = result.getString("Place");
                    int availableSeats = result.getInt("AvailableSeats");
                    int soldSeats = result.getInt("SoldSeats");

                    Date date = new Date(dateTimeString);
                    LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                    Show show = new Show(artistName, localDateTime, place, availableSeats, soldSeats);
                    show.setId((long) Sid);
                    shows.add(show);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return shows;
    }

    @Override
    public Show save(Show entity) {
        if (entity == null) {
            String error = "Entity cannot be null!";
            logger.error(error);
            throw new IllegalArgumentException(error);
        }

        logger.traceEntry("saving show {} ", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Shows (ArtistName, DateTime, Place, AvailableSeats, SoldSeats) values (?,?,?,?,?)")) {
            preStmt.setString(1, entity.getArtistName());

            LocalDateTime dateTime = entity.getDateTime();
            Timestamp timestamp = Timestamp.valueOf(dateTime);
            preStmt.setTimestamp(2, timestamp);

            preStmt.setString(3, entity.getPlace());
            preStmt.setInt(4, entity.getAvailableSeats());
            preStmt.setInt(5, entity.getSoldSeats());

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
    public List<Show> filterByDate(LocalDate date) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Show> shows = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Shows where date(DateTime/1000, 'unixepoch')='" + date.toString() + "'")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int Sid = result.getInt("Sid");
                    String artistName = result.getString("ArtistName");
                    long dateTimeString = result.getLong("DateTime");
                    String place = result.getString("Place");
                    int availableSeats = result.getInt("AvailableSeats");
                    int soldSeats = result.getInt("SoldSeats");

                    Date dateTime = new Date(dateTimeString);
                    LocalDateTime localDateTime = dateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                    Show show = new Show(artistName, localDateTime, place, availableSeats, soldSeats);
                    show.setId((long) Sid);
                    shows.add(show);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return shows;
    }

    @Override
    public void updateSeats(long id, int noOfSeats) {
        logger.traceEntry("updating show seats");
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update Shows set AvailableSeats=AvailableSeats-?, SoldSeats=SoldSeats+? where Sid=" + id)) {
            preStmt.setInt(1, noOfSeats);
            preStmt.setInt(2, noOfSeats);
            int result = preStmt.executeUpdate();
            logger.trace("Updated{} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            String error = "The id cannot be null!";
            logger.error(error);
            throw new IllegalArgumentException(error);
        }

        logger.traceEntry("deleting show {} ", id);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("delete from Shows where Sid=?")) {
            preStmt.setFloat(1, id);

            int result = preStmt.executeUpdate();
            logger.trace("Deleted {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Long id, Show entity) {
        logger.traceEntry("updating show {}", id);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update Shows set ArtistName=?, DateTime=?, Place=?, AvailableSeats=?, SoldSeats=? where Sid=?")) {
            preStmt.setString(1, entity.getArtistName());

            LocalDateTime dateTime = entity.getDateTime();
            Timestamp timestamp = Timestamp.valueOf(dateTime);
            preStmt.setTimestamp(2, timestamp);

            preStmt.setString(3, entity.getPlace());
            preStmt.setInt(4, entity.getAvailableSeats());
            preStmt.setInt(5, entity.getSoldSeats());
            preStmt.setFloat(6, id);

            int result = preStmt.executeUpdate();
            logger.trace("Updated{} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

}
