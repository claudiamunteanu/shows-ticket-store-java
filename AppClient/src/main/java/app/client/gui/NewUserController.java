package app.client.gui;

import app.model.User;
import app.services.AppException;
import app.services.IAppServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class NewUserController {
    IAppServices service;
    Stage dialogStage;

    @FXML
    TextField textUsername;
    @FXML
    PasswordField textPassword;
    @FXML
    PasswordField confirmPassword;
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
    private void handleNewUser() {
        String username = textUsername.getText();
        String password = textPassword.getText();
        String password2 = confirmPassword.getText();

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/mainWindowView.fxml"));
            AnchorPane root = loader.load();

            //Stage
            Stage stage = new Stage();
            stage.setTitle("Create account");
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            stage.setScene(scene);

            MainWindowController mainWindowController = loader.getController();
            User user = service.loginNewUser(username, password, password2, mainWindowController);
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
