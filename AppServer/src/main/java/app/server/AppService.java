package app.server;

import app.model.Show;
import app.model.Ticket;
import app.model.User;
import app.persistence.interfaces.ShowRepository;
import app.persistence.interfaces.TicketRepository;
import app.persistence.interfaces.UserRepository;
import app.server.validators.TicketValidator;
import app.server.validators.UserValidator;
import app.services.AppException;
import app.services.IAppObserver;
import app.services.IAppServices;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppService implements IAppServices {
    private ShowRepository showRepository;
    private TicketRepository ticketRepository;
    private UserRepository userRepository;
    private UserValidator userValidator;
    private TicketValidator ticketValidator;

    private Map<Long, IAppObserver> loggedClients;

    public AppService(ShowRepository showRepository, TicketRepository ticketRepository, UserRepository userRepository, UserValidator userValidator, TicketValidator ticketValidator) {
        this.showRepository = showRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.ticketValidator = ticketValidator;

        loggedClients = new ConcurrentHashMap<>();
    }

    public synchronized List<Show> getAllShows() throws AppException {
        return showRepository.findAll();
    }

    public synchronized User login(String username, String password, IAppObserver client) throws AppException {
        userValidator.validate(Arrays.asList(username, password));
        User user = userRepository.getUser(username, password);
        if (user == null)
            throw new AppException("There is no user with this username and password");
        else {
            if (loggedClients.get(user.getId()) != null)
                throw new AppException("User already logged in!");
            loggedClients.put(user.getId(), client);
        }
        return user;
    }

    public synchronized void logout(User user) throws AppException {
        loggedClients.remove(user.getId());
    }

    public synchronized List<Show> getFilteredShows(LocalDate date) throws AppException {
        return showRepository.filterByDate(date);
    }

    public synchronized void buyTicket(long showId, String buyerName, int noOfSeats) throws AppException {
        ticketValidator.validate(Arrays.asList(String.valueOf(showId), buyerName));
        Ticket ticket = new Ticket(showRepository.findOne(showId), buyerName, noOfSeats);
        ticketRepository.save(ticket);
        showRepository.updateSeats(showId, noOfSeats);
        Long[] ticketData = {showId, (long) noOfSeats};
        notifyTicketBought(ticketData);
    }

    public synchronized User loginNewUser(String username, String password, String password2, IAppObserver client) throws AppException {
        userValidator.validate(Arrays.asList(username, password));
        if (!password.equals(password2))
            throw new AppException("The passwords have to match!");
        User user = userRepository.save(new User(username, password));
        if (user != null)
            throw new AppException("There already exists an user with this username!");
        User user2 = userRepository.getUser(username, password);
        loggedClients.put(user2.getId(), client);
        return user2;
    }

    private final int defaultThreadsNo = 5;

    private void notifyTicketBought(Long[] ticketData) throws AppException {
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for (long id : loggedClients.keySet()) {
            IAppObserver client = loggedClients.get(id);
            if (client != null)
                executor.execute(() -> {
                    try {
                        System.out.println("Notifying [" + id + "] ticket [" + ticketData.toString() + "] bought.");
                        client.ticketBought(ticketData);
                    } catch (AppException e) {
                        System.err.println("Error notifying user " + e);
                    }
                });
        }
        executor.shutdown();
    }

}