package app.network.protobuffprotocol;

import app.model.Show;
import app.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ProtoUtils {
    public static AppProtobufs.AppRequest createLoginRequest(String username, String password) {
        AppProtobufs.User userDTO = AppProtobufs.User.newBuilder()
                .setUsername(username).setPassword(password).build();
        AppProtobufs.AppRequest request = AppProtobufs.AppRequest.newBuilder()
                .setType(AppProtobufs.AppRequest.Type.Login)
                .setUser(userDTO).build();
        return request;
    }

    public static AppProtobufs.AppRequest createLogoutRequest(User user) {
        AppProtobufs.User userDTO = AppProtobufs.User.newBuilder()
                .setId(user.getId().toString()).build();
        AppProtobufs.AppRequest request = AppProtobufs.AppRequest.newBuilder()
                .setType(AppProtobufs.AppRequest.Type.Logout)
                .setUser(userDTO).build();
        return request;
    }

    public static AppProtobufs.AppRequest createGetAllShowsRequest() {
        AppProtobufs.AppRequest request = AppProtobufs.AppRequest.newBuilder()
                .setType(AppProtobufs.AppRequest.Type.GetAllShows).build();
        return request;
    }

    public static AppProtobufs.AppRequest createGetFilteredShowsRequest(LocalDate date) {
        AppProtobufs.AppRequest request = AppProtobufs.AppRequest.newBuilder()
                .setType(AppProtobufs.AppRequest.Type.GetFilteredShows)
                .setDate(date.toString()).build();
        return request;
    }

    public static AppProtobufs.AppRequest createAddUserRequest(String username, String password, String confirmedPassword) {
        AppProtobufs.User userDTO = AppProtobufs.User.newBuilder()
                .setUsername(username)
                .setPassword(password)
                .setConfirmedPassword(confirmedPassword)
                .build();
        AppProtobufs.AppRequest request = AppProtobufs.AppRequest.newBuilder()
                .setType(AppProtobufs.AppRequest.Type.AddUser)
                .setUser(userDTO).build();
        return request;
    }

    public static AppProtobufs.AppRequest createBuyTicketRequest(long showId, String buyerName, int noOfSeats) {
        AppProtobufs.Ticket ticketDTO = AppProtobufs.Ticket.newBuilder()
                .setShowId(String.valueOf(showId))
                .setBuyerName(buyerName)
                .setNoOfSeats(String.valueOf(noOfSeats))
                .build();
        AppProtobufs.AppRequest request = AppProtobufs.AppRequest.newBuilder()
                .setType(AppProtobufs.AppRequest.Type.BuyTicket)
                .setTicket(ticketDTO).build();
        return request;
    }

    public static AppProtobufs.AppResponse createOkResponse() {
        AppProtobufs.AppResponse response = AppProtobufs.AppResponse.newBuilder()
                .setType(AppProtobufs.AppResponse.Type.Ok).build();
        return response;
    }

    public static AppProtobufs.AppResponse createErrorResponse(String text) {
        AppProtobufs.AppResponse response = AppProtobufs.AppResponse.newBuilder()
                .setType(AppProtobufs.AppResponse.Type.Error)
                .setError(text).build();
        return response;
    }

    public static AppProtobufs.AppResponse createUserLoggedInResponse(User user) {
        AppProtobufs.User userDTO = AppProtobufs.User.newBuilder()
                .setId(user.getId().toString())
                .setUsername(user.getUsername())
                .setPassword(user.getPassword())
                .build();
        AppProtobufs.AppResponse response = AppProtobufs.AppResponse.newBuilder()
                .setType(AppProtobufs.AppResponse.Type.UserLoggedIn)
                .setUser(userDTO).build();
        return response;
    }

    public static AppProtobufs.AppResponse createUserLoggedOutResponse() {
        AppProtobufs.AppResponse response = AppProtobufs.AppResponse.newBuilder()
                .setType(AppProtobufs.AppResponse.Type.UserLoggedOut).build();
        return response;
    }

    public static AppProtobufs.AppResponse createGetAllShowsResponse(List<Show> shows) {
        AppProtobufs.AppResponse.Builder response = AppProtobufs.AppResponse.newBuilder()
                .setType(AppProtobufs.AppResponse.Type.GetAllShows);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        for (Show s : shows) {
            AppProtobufs.Show showDTO = AppProtobufs.Show.newBuilder()
                    .setId(s.getId().toString())
                    .setArtistName(s.getArtistName())
                    .setDateTime(s.getDateTime().format(formatter))
                    .setPlace(s.getPlace())
                    .setAvailableSeats(String.valueOf(s.getAvailableSeats()))
                    .setSoldSeats(String.valueOf(s.getSoldSeats()))
                    .build();
            response.addShows(showDTO);
        }
        return response.build();
    }

    public static AppProtobufs.AppResponse createGetFilteredShowsResponse(List<Show> filteredShows) {
        AppProtobufs.AppResponse.Builder response = AppProtobufs.AppResponse.newBuilder()
                .setType(AppProtobufs.AppResponse.Type.GetFilteredShows);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        for (Show s : filteredShows) {
            AppProtobufs.Show showDTO = AppProtobufs.Show.newBuilder()
                    .setId(s.getId().toString())
                    .setArtistName(s.getArtistName())
                    .setDateTime(s.getDateTime().format(formatter))
                    .setPlace(s.getPlace())
                    .setAvailableSeats(String.valueOf(s.getAvailableSeats()))
                    .setSoldSeats(String.valueOf(s.getSoldSeats()))
                    .build();
            response.addShows(showDTO);
        }
        return response.build();
    }

    public static AppProtobufs.AppResponse createNewUserResponse(User user) {
        AppProtobufs.User userDTO = AppProtobufs.User.newBuilder()
                .setId(user.getId().toString())
                .setUsername(user.getUsername())
                .setPassword(user.getPassword())
                .build();
        AppProtobufs.AppResponse response = AppProtobufs.AppResponse.newBuilder()
                .setType(AppProtobufs.AppResponse.Type.NewUser)
                .setUser(userDTO).build();
        return response;
    }

    public static AppProtobufs.AppResponse createTicketBoughtResponse(Long[] ticketData) {
        AppProtobufs.Ticket ticketDTO = AppProtobufs.Ticket.newBuilder()
                .setShowId(ticketData[0].toString())
                .setNoOfSeats(ticketData[1].toString()).build();
        AppProtobufs.AppResponse response = AppProtobufs.AppResponse.newBuilder()
                .setType(AppProtobufs.AppResponse.Type.TicketBought)
                .setTicket(ticketDTO).build();
        return response;
    }

    public static Long[] getTicket(AppProtobufs.AppResponse response) {
        Long[] ticketData = new Long[2];
        AppProtobufs.Ticket ticket = response.getTicket();
        ticketData[0] = Long.parseLong(ticket.getShowId());
        ticketData[1] = Long.parseLong(ticket.getNoOfSeats());
        return ticketData;
    }

    public static String getError(AppProtobufs.AppResponse response){
        String errorMessage=response.getError();
        return errorMessage;
    }

    public static User getUser(AppProtobufs.AppResponse response){
        AppProtobufs.User userDTO = response.getUser();
        User user = new User(userDTO.getUsername(), userDTO.getPassword());
        user.setId(Long.parseLong(userDTO.getId()));
        return user;
    }

    public static List<Show> getShows(AppProtobufs.AppResponse response){
        List<Show> shows = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        for(int i=0; i< response.getShowsCount(); i++){
            AppProtobufs.Show showDTO = response.getShows(i);
            Show show = new Show(showDTO.getArtistName(), LocalDateTime.parse(showDTO.getDateTime(), formatter), showDTO.getPlace(), Integer.parseInt(showDTO.getAvailableSeats()), Integer.parseInt(showDTO.getSoldSeats()));
            show.setId(Long.parseLong(showDTO.getId()));
            shows.add(show);
        }
        return shows;
    }

    public static User getUser(AppProtobufs.AppRequest request){
        User user = new User(request.getUser().getUsername(), request.getUser().getPassword());
        String id = request.getUser().getId();
        if(!id.isEmpty())
            user.setId(Long.parseLong(id));
        return user;
    }

    public static LocalDate getDate(AppProtobufs.AppRequest request){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(request.getDate(),formatter);
    }

    public static String [] getData(AppProtobufs.AppRequest request){
        String [] data = new String[3];
        data[0] = request.getUser().getUsername();
        data[1] = request.getUser().getPassword();
        data[2] = request.getUser().getConfirmedPassword();
        return data;
    }

    public static String[] getTicket(AppProtobufs.AppRequest request){
        String[] ticketData = new String[3];
        ticketData[0] = request.getTicket().getShowId();
        ticketData[1] = request.getTicket().getBuyerName();
        ticketData[2] = request.getTicket().getNoOfSeats();
        return ticketData;

    }
}
