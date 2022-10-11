package app.persistence;
import app.model.Show;
import app.model.Ticket;
import app.persistence.interfaces.TicketRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Component
public class TicketsDBRepository implements TicketRepository {
    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public TicketsDBRepository(Properties props) {
        logger.info("Initializing TicketsDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Ticket findOne(Long id) {
        if(id==null){
            String error = "ID cannot be null!";
            logger.error(error);
            throw new IllegalArgumentException(error);
        }

        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        Ticket ticket = null;
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * from Shows inner join Tickets on Shows.Sid=Tickets.ShowId where Tid="+id.toString())) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int Tid = result.getInt("Tid");
                    String buyerName = result.getString("BuyerName");
                    int seats = result.getInt("NoOfSeats");

                    int Sid = result.getInt("ShowId");
                    String artistName = result.getString("ArtistName");
                    long dateTimeString = result.getLong("DateTime");
                    String place = result.getString("Place");
                    int availableSeats = result.getInt("AvailableSeats");
                    int soldSeats = result.getInt("SoldSeats");

                    Date date = new Date(dateTimeString);
                    LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                    Show show = new Show(artistName, localDateTime, place, availableSeats, soldSeats);
                    show.setId((long)Sid);

                    ticket = new Ticket(show, buyerName, seats);
                    ticket.setId((long) Tid);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return ticket;
    }

    @Override
    public List<Ticket> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Ticket> tickets = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * from Shows inner join Tickets on Shows.Sid=Tickets.ShowId")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int Tid = result.getInt("Tid");
                    String buyerName = result.getString("BuyerName");
                    int seats = result.getInt("NoOfSeats");

                    int Sid = result.getInt("ShowId");
                    String artistName = result.getString("ArtistName");
                    long dateTimeString = result.getLong("DateTime");
                    String place = result.getString("Place");
                    int availableSeats = result.getInt("AvailableSeats");
                    int soldSeats = result.getInt("SoldSeats");

                    Date date = new Date(dateTimeString);
                    LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                    Show show = new Show(artistName, localDateTime, place, availableSeats, soldSeats);
                    show.setId((long)Sid);

                    Ticket ticket = new Ticket(show, buyerName, seats);
                    ticket.setId((long) Tid);
                    tickets.add(ticket);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return tickets;
    }

    @Override
    public Ticket save(Ticket entity) {
        if(entity==null){
            String error = "Entity cannot be null!";
            logger.error(error);
            throw new IllegalArgumentException(error);
        }

        logger.traceEntry("saving show {} ", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Tickets (ShowId, BuyerName, NoOfSeats) values (?,?,?)")) {
            preStmt.setInt(1,entity.getshow().getId().intValue());
            preStmt.setString(2,entity.getBuyerName());
            preStmt.setInt(3,entity.getNoOfSeats());

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
}
