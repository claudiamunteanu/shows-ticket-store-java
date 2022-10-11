package app.network.rpcprotocol;

import app.model.Show;
import app.model.User;

import app.services.AppException;
import app.services.IAppObserver;
import app.services.IAppServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;


public class AppClientRpcReflectionWorker implements Runnable, IAppObserver {
    private IAppServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public AppClientRpcReflectionWorker(IAppServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
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
        Response resp=new Response.Builder().type(ResponseType.TICKET_BOUGHT).data(ticketData).build();
        System.out.println("Ticket received  "+ticketData);
        try {
            sendResponse(resp);
        } catch (IOException e) {
            throw new AppException("Sending error: "+e);
        }
    }


    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();
    //  private static Response errorResponse=new Response.Builder().type(ResponseType.ERROR).build();

    private Response handleRequest(Request request){
        Response response=null;
        String handlerName="handle"+(request).type();
        System.out.println("HandlerName "+handlerName);
        try {
            Method method=this.getClass().getDeclaredMethod(handlerName, Request.class);
            response=(Response)method.invoke(this,request);
            System.out.println("Method "+handlerName+ " invoked");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return response;
    }

    private Response handleGET_ALL_SHOWS(Request request){
        System.out.println("GetAllShows Request ...");
        try {
            List<Show> shows = server.getAllShows();
            return new Response.Builder().type(ResponseType.GET_ALL_SHOWS).data(shows).build();
        } catch (AppException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleLOGIN(Request request){
        System.out.println("Login request ..."+request.type());
        User user=(User)request.data();
        try {
            user = server.login(user.getUsername(), user.getPassword(), this);
            return new Response.Builder().type(ResponseType.USER_LOGGED_IN).data(user).build();
        } catch (AppException e) {
            connected=false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_FILTERED_SHOWS(Request request){
        System.out.println("GetFilteredShows Request ...");
        LocalDate date =(LocalDate) request.data();
        try {
            List<Show> shows = server.getFilteredShows(date);
            return new Response.Builder().type(ResponseType.GET_FILTERED_SHOWS).data(shows).build();
        } catch (AppException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleBUY_TICKET(Request request){
        System.out.println("BuyTicketRequest ...");
        String [] data = (String[]) request.data();
        try {
            server.buyTicket(Long.parseLong(data[0]), data[1], Integer.parseInt(data[2]));
            return okResponse;
        } catch (AppException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleADD_USER(Request request){
        System.out.println("LoginNewUserRequest ...");
        String [] data = (String[]) request.data();
        try {
            User user = server.loginNewUser(data[0], data[1], data[2], this);
            return new Response.Builder().type(ResponseType.NEW_USER).data(user).build();
        } catch (AppException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleLOGOUT(Request request){
        System.out.println("Logout request...");
        User user=(User)request.data();
        try {
            server.logout(user);
            connected=false;
            return okResponse;

        } catch (AppException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }


    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        synchronized (output){
            output.writeObject(response);
            output.flush();
        }
    }
}
