package it.polimi.ingsw.client.GUI.login;
import it.polimi.ingsw.client.Client;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Login extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("/GUI/login.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Sagrada");
        primaryStage.setScene(new Scene(root, 500, 650));
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> System.exit(0));
    }

    public void showGUI() {
        launch();
    }
}
