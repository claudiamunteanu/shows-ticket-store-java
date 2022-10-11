package app.network.rpcprotocol;

import app.model.Show;
import app.model.User;
import app.services.AppException;
import app.services.IAppObserver;
import app.services.IAppServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class AppServicesRpcProxy implements IAppServices {
    private String host;
    private int port;

    private IAppObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public AppServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
    }

    @Override
    public List<Show> getAllShows() throws AppException {
        Request req = new Request.Builder().type(RequestType.GET_ALL_SHOWS).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new AppException(err);
        }
        List<Show> shows = (List<Show>) response.data();
        return shows;
    }

    @Override
    public User login(String username, String password, IAppObserver client) throws AppException {
        initializeConnection();
        User user = new User(username, password);
        Request req = new Request.Builder().type(RequestType.LOGIN).data(user).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new AppException(err);
        }
        this.client = client;
        return (User) response.data();
    }

    @Override
    public List<Show> getFilteredShows(LocalDate date) throws AppException {
        Request req = new Request.Builder().type(RequestType.GET_FILTERED_SHOWS).data(date).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new AppException(err);
        }
        return (List<Show>) response.data();
    }

    @Override
    public void buyTicket(long showId, String buyerName, int noOfSeats) throws AppException {
        String[] data = {String.valueOf(showId), buyerName, String.valueOf(noOfSeats)};
        Request req = new Request.Builder().type(RequestType.BUY_TICKET).data(data).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new AppException(err);
        }
    }

    @Override
    public User loginNewUser(String username, String password, String password2, IAppObserver client) throws AppException {
        initializeConnection();
        String[] data = {username, password, password2};
        Request req = new Request.Builder().type(RequestType.ADD_USER).data(data).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new AppException(err);
        }
        this.client = client;
        return (User) response.data();
    }

    @Override
    public void logout(User user) throws AppException {
        Request req = new Request.Builder().type(RequestType.LOGOUT).data(user).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new AppException(err);
        }
    }


    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request) throws AppException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new AppException("Error sending object " + e);
        }

    }

    private Response readResponse() throws AppException {
        Response response = null;
        try {

            response = qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() throws AppException {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }


    private void handleUpdate(Response response) {
        if (response.type() == ResponseType.TICKET_BOUGHT) {
            Long[] ticketData = (Long[]) response.data();
            System.out.println("Ticket bought " + ticketData);
            try {
                client.ticketBought(ticketData);
            } catch (AppException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isUpdate(Response response) {
        return response.type() == ResponseType.TICKET_BOUGHT;
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("response received " + response);
                    if (isUpdate((Response) response)) {
                        handleUpdate((Response) response);
                    } else {

                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }
}
