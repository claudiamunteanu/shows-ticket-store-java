package app.persistence.interfaces;

import app.model.Show;

import java.time.LocalDate;
import java.util.List;

public interface ShowRepository extends  Repository<Long, Show>{
    /**
     * @param date - the date by which we filter the shows
     *           date must not be null
     * @return the list with the filtered shows
     * @throws IllegalArgumentException if date is null.
     */
    List<Show> filterByDate(LocalDate date);

    /**
     * @param id - the id of the show
     * @param noOfSeats - the number of bought seats
     */
    void updateSeats(long id, int noOfSeats);

    void delete(Long id);

    void update(Long id, Show show);
}
