package app.services;

import app.model.Show;
import app.model.Ticket;
import app.model.User;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public interface IAppServices {
    public List<Show> getAllShows() throws AppException;

    public User login(String username, String password, IAppObserver client) throws AppException;

    public List<Show> getFilteredShows(LocalDate date) throws AppException;

    public void buyTicket(long showId, String buyerName, int noOfSeats) throws AppException;

    public User loginNewUser(String username, String password, String password2, IAppObserver client) throws AppException;

    public void logout(User user) throws AppException;


}
