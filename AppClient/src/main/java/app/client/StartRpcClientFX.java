package app.client;

import app.client.gui.MainWindowController;
import app.client.gui.WelcomeController;
import app.model.User;
import app.network.rpcprotocol.AppServicesRpcProxy;
import app.services.AppException;
import app.services.IAppServices;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class StartRpcClientFX extends Application {
    private Stage primaryStage;

    private static int defaultChatPort=55555;
    private static String defaultServer="localhost";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartRpcClientFX.class.getResourceAsStream("/appclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find appclient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("app.server.host", defaultServer);
        int serverPort = defaultChatPort;
        try {
            serverPort = Integer.parseInt(clientProps.getProperty("app.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);
        IAppServices server = new AppServicesRpcProxy(serverIP, serverPort);

        initView(primaryStage, server);
        primaryStage.show();
    }

    private void initView(Stage primaryStage, IAppServices server) throws IOException, AppException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/welcomeView.fxml"));
        AnchorPane root = loader.load();
        primaryStage.setScene(new Scene(root));

        WelcomeController welcomeController = loader.getController();
        welcomeController.setService(server, primaryStage);
    }
}
