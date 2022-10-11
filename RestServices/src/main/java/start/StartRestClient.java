package start;

import app.model.Show;
import app.services.rest.ServiceException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import rest.client.ShowsClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class StartRestClient {
    private final static ShowsClient showsClient = new ShowsClient();

    private static Show show;

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            getAll();

            show(() -> System.out.println(showsClient.getById(3L)));

            show = new Show("Jennifer Lopez", LocalDateTime.parse("2021-03-19T20:30:00"), "Paris", 3500, 500);
            show(() -> System.out.println(showsClient.create(show)));
            getAll();

            show.setPlace("Barcelona");
            show.setAvailableSeats(3000);
            show.setSoldSeats(1000);


            show(() -> showsClient.update(show));
            getAll();

            show(() -> showsClient.delete(show.getId()));
            getAll();

        } catch (RestClientException ex) {
            System.out.println("Exception..." + ex.getMessage());
        }
    }

    private static void getAll() {
        System.out.println();
        show(() -> {
            List<Show> shows = showsClient.getAll();
            for (Show u : shows) {
                System.out.println(u);
            }
            show = shows.get(shows.size() - 1);
        });
        System.out.println();
    }

    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            System.out.println("Service exception" + e);
        }
    }
}
