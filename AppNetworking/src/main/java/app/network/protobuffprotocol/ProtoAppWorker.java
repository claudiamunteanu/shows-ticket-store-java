package app.network.protobuffprotocol;

import app.model.Show;
import app.model.User;

import app.services.AppException;
import app.services.IAppObserver;
import app.services.IAppServices;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;


public class ProtoAppWorker implements Runnable, IAppObserver {
    private IAppServices server;
    private Socket connection;

    private InputStream input;
    private OutputStream output;
    private volatile boolean connected;

    public ProtoAppWorker(IAppServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = connection.getOutputStream();
            input = connection.getInputStream();
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {
                // Object request=input.readObject();
                System.out.println("Waiting requests ...");
                AppProtobufs.AppRequest request= AppProtobufs.AppRequest.parseDelimitedFrom(input);
                System.out.println("Request received: "+request);
                AppProtobufs.AppResponse response=handleRequest(request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    @Override
    public void ticketBought(Long[] ticketData) throws AppException {
        System.out.println("Ticket received  " + ticketData);
        try {
            sendResponse(ProtoUtils.createTicketBoughtResponse(ticketData));
        } catch (IOException e) {
            throw new AppException("Sending error: " + e);
        }
    }

    private AppProtobufs.AppResponse handleRequest(AppProtobufs.AppRequest request) {
        AppProtobufs.AppResponse response = null;
        switch (request.getType()){
            case Login:{
                System.out.println("Login request ...");
                User user = ProtoUtils.getUser(request);
                try {
                    user = server.login(user.getUsername(), user.getPassword(), this);
                    return ProtoUtils.createUserLoggedInResponse(user);
                } catch (AppException e) {
                    connected = false;
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case Logout:{
                System.out.println("Logout request...");
                User user = ProtoUtils.getUser(request);
                try {
                    server.logout(user);
                    connected = false;
                    return ProtoUtils.createOkResponse();

                } catch (AppException e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case GetAllShows:{
                System.out.println("GetAllShows Request ...");
                try {
                    List<Show> shows = server.getAllShows();
                    return ProtoUtils.createGetAllShowsResponse(shows);
                } catch (AppException e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case GetFilteredShows:{
                System.out.println("GetFilteredShows Request ...");
                LocalDate date = ProtoUtils.getDate(request);
                try {
                    List<Show> shows = server.getFilteredShows(date);
                    return ProtoUtils.createGetFilteredShowsResponse(shows);
                } catch (AppException e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }

            case AddUser:{
                System.out.println("LoginNewUserRequest ...");
                String[] data = ProtoUtils.getData(request);
                try {
                    User user = server.loginNewUser(data[0], data[1], data[2], this);
                    return ProtoUtils.createNewUserResponse(user);
                } catch (AppException e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }

            case BuyTicket:{
                System.out.println("BuyTicketRequest ...");
                String[] data = ProtoUtils.getTicket(request);
                try {
                    server.buyTicket(Long.parseLong(data[0]), data[1], Integer.parseInt(data[2]));
                    return ProtoUtils.createOkResponse();
                } catch (AppException e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
        }
        return response;
    }


    private void sendResponse(AppProtobufs.AppResponse response) throws IOException {
        System.out.println("sending response " + response);
        response.writeDelimitedTo(output);
        output.flush();
    }
}
