package app.client.gui;

import app.model.Show;
import app.model.User;
import app.network.dto.ShowDTO;
import app.services.AppException;
import app.services.IAppObserver;
import app.services.IAppServices;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class MainWindowController implements IAppObserver {
    IAppServices service;
    Stage dialogStage;
    User user = null; // current user

    ObservableList<Show> shows = FXCollections.observableArrayList();
    ObservableList<ShowDTO> filteredShows = FXCollections.observableArrayList();

    @FXML
    Label labelErrors;
    @FXML
    Label labelErrors2;
    @FXML
    Label labelTitle;


    @FXML
    DatePicker datePicker;

    @FXML
    Button btnBuyTicket;

    @FXML
    TextField buyerNameText;

    @FXML
    Spinner<Integer> seatsSpinner;

    @FXML
    TableView<ShowDTO> tableFilteredShows;

    @FXML
    TableColumn<ShowDTO, String> filteredArtistNameColumn;
    @FXML
    TableColumn<ShowDTO, String> filteredDateColumn;
    @FXML
    TableColumn<ShowDTO, String> filteredPlaceColumn;
    @FXML
    TableColumn<ShowDTO, String> filteredAvailableSeatsColumn;
    @FXML
    TableColumn<ShowDTO, String> filteredHourColumn;

    @FXML
    TableView<Show> tableShows;

    @FXML
    TableColumn<Show, String> artistNameColumn;
    @FXML
    TableColumn<Show, LocalDateTime> dateTimeColumn;
    @FXML
    TableColumn<Show, String> placeColumn;
    @FXML
    TableColumn<Show, Integer> availableSeatsColumn;
    @FXML
    TableColumn<Show, Integer> soldSeatsColumn;

    public User setService(IAppServices serviceAll, Stage dialogStage, User user) throws AppException {
        this.service = serviceAll;
        this.dialogStage = dialogStage;
        this.user = user;

        tableFilteredShows.setRowFactory(row -> new TableRow<ShowDTO>(){
            @Override
            protected void updateItem(ShowDTO item, boolean empty){
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                } else if (Integer.parseInt(item.getAvailableSeats())==0){
                    setStyle("-fx-background-color: lightcoral");
                }
            }
        });

        //datePicker.setValue(LocalDate.now());
        dialogStage.setTitle("Welcome " + user.getUsername() + "!");
        shows.setAll(service.getAllShows());
        return user;
    }

    @FXML
    public void initialize() {
        artistNameColumn.setCellValueFactory(new PropertyValueFactory<>("artistName"));
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        placeColumn.setCellValueFactory(new PropertyValueFactory<>("place"));
        availableSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));
        soldSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("soldSeats"));
        tableShows.setItems(shows);

        filteredArtistNameColumn.setCellValueFactory(new PropertyValueFactory<>("artistName"));
        filteredDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        filteredPlaceColumn.setCellValueFactory(new PropertyValueFactory<>("place"));
        filteredAvailableSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));
        filteredHourColumn.setCellValueFactory(new PropertyValueFactory<>("hour"));
        tableFilteredShows.setItems(filteredShows);
    }

    public User showLogin() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/welcomeView.fxml"));
            AnchorPane root = loader.load();

            //Stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Welcome!");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            WelcomeController welcomeController = loader.getController();
            welcomeController.setService(service, dialogStage);
            dialogStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @FXML
    public void handleSearchShows() throws AppException {
        LocalDate date = datePicker.getValue();
        if(date == null){
            labelErrors.setText("You have to pick a date!");
        } else{
            labelErrors.setText("");
            labelErrors2.setText("");
            datePicker.setValue(null);
            labelTitle.setText("Shows from date " + date.toString());
            seatsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100));
            filteredShows.setAll(getFilteredShows(date));
        }
    }

    @FXML
    public void handleLogout() throws AppException {
        service.logout(user);
        dialogStage.close();
        showLogin();
    }

    @FXML
    private void handleBuyTicket(){
        try{
            ShowDTO show = tableFilteredShows.getSelectionModel().getSelectedItem();
            String buyerName = buyerNameText.getText();
            int seats = seatsSpinner.getValue();
            service.buyTicket(show.getId(), buyerName, seats);
            buyerNameText.clear();
            labelErrors2.setStyle("-fx-text-fill: black");
            labelErrors2.setText("Ticket bought successfully!");
        }catch (AppException ex){
            labelErrors2.setStyle("-fx-text-fill: red");
            labelErrors2.setPrefHeight(20 * ex.getMessage().split("\n").length);
            labelErrors2.setText(ex.getMessage());
        }
    }

    @FXML
    private void handleRowSelection() {
        ShowDTO selectedShow = tableFilteredShows.getSelectionModel().getSelectedItem();
        int availableSeats = Integer.parseInt(selectedShow.getAvailableSeats());
        labelErrors2.setText("");
        if (availableSeats > 0){
            seatsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, availableSeats));
            btnBuyTicket.setDisable(false);
        }
        else{
            seatsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0));
            btnBuyTicket.setDisable(true);
        }

    }

    public void updateTableShows(Long [] ticketData) {
        Show show = null;
        int index = 0;
        for(Show s : shows){
            if(s.getId().equals(ticketData[0])){
                show = s;
                index = shows.indexOf(show);
                break;
            }
        }
        int seats = show.getAvailableSeats()- Integer.parseInt(ticketData[1].toString());
        show.setAvailableSeats(seats);
        shows.set(index, show);
    }

    private void updateTableFilteredShows(Long[] ticketData) {
        ShowDTO show = null;
        int index = 0;
        for(ShowDTO s : filteredShows){
            if(s.getId().equals(ticketData[0])){
                show = s;
                index = filteredShows.indexOf(show);
                break;
            }
        }
        if(show!=null){
            int seats = Integer.parseInt(show.getAvailableSeats()) - Integer.parseInt(ticketData[1].toString());
            show.setAvailableSeats(String.valueOf(seats));
            filteredShows.set(index, show);
        }
    }

    public List<ShowDTO> getFilteredShows(LocalDate date) throws AppException {
        return service.getFilteredShows(date).stream().map(s -> {
            LocalDateTime dateTime = s.getDateTime();
            LocalDate localDate = dateTime.toLocalDate();
            LocalTime localTime = LocalTime.of(dateTime.getHour(), dateTime.getMinute());
            ShowDTO show = new ShowDTO(s.getArtistName(), localDate.toString(), s.getPlace(), String.valueOf(s.getAvailableSeats()), localTime.toString());
            show.setId(s.getId());
            return show;
        }).collect(Collectors.toList());
    }

    @Override
    public void ticketBought(Long[] ticketData) throws AppException {
        Platform.runLater(()->{
            updateTableShows(ticketData);
            updateTableFilteredShows(ticketData);
        });
    }
}
