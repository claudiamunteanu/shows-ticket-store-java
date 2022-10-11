package app.network.protobuffprotocol;

import app.model.Show;
import app.model.User;
import app.services.AppException;
import app.services.IAppObserver;
import app.services.IAppServices;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ProtoAppProxy implements IAppServices {
    private String host;
    private int port;

    private IAppObserver client;

    private InputStream input;
    private OutputStream output;
    private Socket connection;

    private BlockingQueue<AppProtobufs.AppResponse> qresponses;
    private volatile boolean finished;

    public ProtoAppProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<AppProtobufs.AppResponse>();
    }

    @Override
    public List<Show> getAllShows() throws AppException {
        sendRequest(ProtoUtils.createGetAllShowsRequest());
        AppProtobufs.AppResponse response = readResponse();
        if (response.getType() == AppProtobufs.AppResponse.Type.Error) {
            String err = ProtoUtils.getError(response);
            throw new AppException(err);
        }
        return ProtoUtils.getShows(response);
    }

    @Override
    public User login(String username, String password, IAppObserver client) throws AppException {
        initializeConnection();
        sendRequest(ProtoUtils.createLoginRequest(username, password));
        AppProtobufs.AppResponse response = readResponse();
        if (response.getType() == AppProtobufs.AppResponse.Type.Error) {
            String err = ProtoUtils.getError(response);
            closeConnection();
            throw new AppException(err);
        }
        this.client = client;
        return ProtoUtils.getUser(response);
    }

    @Override
    public List<Show> getFilteredShows(LocalDate date) throws AppException {
        sendRequest(ProtoUtils.createGetFilteredShowsRequest(date));
        AppProtobufs.AppResponse response = readResponse();
        if (response.getType() == AppProtobufs.AppResponse.Type.Error) {
            String err = ProtoUtils.getError(response);
            throw new AppException(err);
        }
        return ProtoUtils.getShows(response);
    }

    @Override
    public void buyTicket(long showId, String buyerName, int noOfSeats) throws AppException {
        sendRequest(ProtoUtils.createBuyTicketRequest(showId, buyerName, noOfSeats));
        AppProtobufs.AppResponse response = readResponse();
        if (response.getType() == AppProtobufs.AppResponse.Type.Error) {
            String err = ProtoUtils.getError(response);
            throw new AppException(err);
        }
    }

    @Override
    public User loginNewUser(String username, String password, String password2, IAppObserver client) throws AppException {
        initializeConnection();
        sendRequest(ProtoUtils.createAddUserRequest(username, password, password2));
        AppProtobufs.AppResponse response = readResponse();
        if (response.getType() == AppProtobufs.AppResponse.Type.Error) {
            String err = ProtoUtils.getError(response);
            closeConnection();
            throw new AppException(err);
        }
        this.client = client;
        return ProtoUtils.getUser(response);
    }

    @Override
    public void logout(User user) throws AppException {
        sendRequest(ProtoUtils.createLogoutRequest(user));
        AppProtobufs.AppResponse response = readResponse();
        closeConnection();
        if (response.getType() == AppProtobufs.AppResponse.Type.Error) {
            String err = ProtoUtils.getError(response);
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

    private void sendRequest(AppProtobufs.AppRequest request) throws AppException {
        try {
            System.out.println("Sending request ..."+request);
            request.writeDelimitedTo(output);
            output.flush();
            System.out.println("Request sent.");
        } catch (IOException e) {
            throw new AppException("Error sending object " + e);
        }

    }

    private AppProtobufs.AppResponse readResponse() throws AppException {
        AppProtobufs.AppResponse response = null;
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
            output = connection.getOutputStream();
            //output.flush();
            input = connection.getInputStream();
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


    private void handleUpdate(AppProtobufs.AppResponse response) {
        switch (response.getType()){
            case TicketBought:{
                Long[] ticketData = ProtoUtils.getTicket(response);
                System.out.println("Ticket bought " + ticketData);
                try {
                    client.ticketBought(ticketData);
                } catch (AppException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isUpdate(AppProtobufs.AppResponse.Type type) {
        switch (type){
            case TicketBought: return true;
        }
        return false;
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    AppProtobufs.AppResponse response = AppProtobufs.AppResponse.parseDelimitedFrom(input);
                    System.out.println("response received " + response);
                    if (isUpdate((response.getType()))) {
                        handleUpdate(response);
                    } else {

                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }
}
