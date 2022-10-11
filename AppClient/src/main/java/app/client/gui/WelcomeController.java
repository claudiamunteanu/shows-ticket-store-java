package app.client.gui;

import app.model.Ticket;
import app.model.User;
import app.services.AppException;
import app.services.IAppObserver;
import app.services.IAppServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WelcomeController{
    IAppServices service;
    Stage dialogStage;

    public void setService(IAppServices service, Stage dialogStage){
        this.service = service;
        this.dialogStage = dialogStage;
    }

    @FXML
    public void initialize() {
    }

    @FXML
    public void handleLogin(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/loginView.fxml"));
            AnchorPane root = loader.load();

            //Stage
            Stage stage = new Stage();
            stage.setTitle("Login!");
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            stage.setScene(scene);

            LoginController loginController = loader.getController();
            loginController.setService(service, stage);
            dialogStage.close();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleNewUser(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/newUserView.fxml"));
            AnchorPane root = loader.load();

            //Stage
            Stage stage = new Stage();
            stage.setTitle("Create account");
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            stage.setScene(scene);

            NewUserController newUserController = loader.getController();
            newUserController.setService(service, stage);
            dialogStage.close();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
