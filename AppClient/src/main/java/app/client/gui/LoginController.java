package app.client.gui;

import app.model.Ticket;
import app.model.User;
import app.services.AppException;
import app.services.IAppObserver;
import app.services.IAppServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class LoginController{
    IAppServices service;
    Stage dialogStage;

    @FXML
    TextField textUsername;
    @FXML
    PasswordField textPassword;
    @FXML
    Label labelErrors;

    public void setService(IAppServices service, Stage stage) {
        this.service = service;
        dialogStage = stage;
    }

    @FXML
    public void initialize() {
    }

    @FXML
    private void handleLogin() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/mainWindowView.fxml"));
            AnchorPane root = loader.load();

            //Stage
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            stage.setScene(scene);

            MainWindowController mainWindowController = loader.getController();

            String username = textUsername.getText();
            String password = textPassword.getText();
            User user = service.login(username, password, mainWindowController);
            mainWindowController.setService(service, stage, user);
            dialogStage.close();
            stage.showAndWait();
        } catch (AppException | IOException ex) {
            labelErrors.setPrefHeight(20 * ex.getMessage().split("\n").length);
            labelErrors.setText(ex.getMessage());
        }
    }

    @FXML
    private void handleClose() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/welcomeView.fxml"));
            AnchorPane root = loader.load();

            //Stage
            Stage stage = new Stage();
            stage.setTitle("Welcome!");
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            stage.setScene(scene);

            WelcomeController welcomeController = loader.getController();
            welcomeController.setService(service, stage);
            dialogStage.close();
            stage.showAndWait();

        } catch (IOException ex) {
            labelErrors.setPrefHeight(20 * ex.getMessage().split("\n").length);
            labelErrors.setText(ex.getMessage());
        }
    }

}
