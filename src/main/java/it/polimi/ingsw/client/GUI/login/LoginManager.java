package it.polimi.ingsw.client.GUI.login;

import it.polimi.ingsw.Model.Cards.Patterns.PatternCard;
import it.polimi.ingsw.Model.Cards.PrivateObjectives.PrivateObjectiveCard;
import it.polimi.ingsw.Model.Game.ScoreTrack;
import it.polimi.ingsw.Model.Game.Table;
import it.polimi.ingsw.client.Communicator;
import it.polimi.ingsw.client.CommunicatorRMI;
import it.polimi.ingsw.client.CommunicatorSocket;
import it.polimi.ingsw.client.GUI.GUIData;
import it.polimi.ingsw.client.GUI.GUIManager;
import it.polimi.ingsw.exceptions.NetworkErrorException;
import it.polimi.ingsw.exceptions.NotValidInputException;
import it.polimi.ingsw.view.GUIView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.polimi.ingsw.client.CLI.CLI.DEFAULT_SERVER;
import static it.polimi.ingsw.client.CLI.CLI.DEFAULT_SERVER_RMI_PORT;
import static it.polimi.ingsw.client.CLI.CLI.DEFAULT_SERVER_SOCKET_PORT;

public class LoginManager implements GUIManager{
    private Communicator communicator;
    private static final Logger logger = Logger.getLogger( LoginManager.class.getName() );

    private static GUIView view;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private MenuButton connection;
    @FXML
    private TextField IPaddress;
    @FXML
    private TextField serverPort;
    @FXML
    private Button login;
    @FXML
    private ImageView background;
    private static String DEFAULT_PASS = "default";

    /**
     * This method is called if the loginButton is pressed.
     * It create a new View and a new Communicator and it sends to the server the message with the user credentials.
     * The FXMLLoader loads the lobby.fxml file.
     * .
     */
    @FXML
    public void loginAction(MouseEvent event) {
        if (!connection.getText().equals("socket")) {
            view = new GUIView();
            view.setGUIManager(this);
            communicator = new CommunicatorRMI(view);
            if(IPaddress.getText().equals("")) IPaddress.setText(DEFAULT_SERVER);
            if(serverPort.getText().equals("")) serverPort.setText(DEFAULT_SERVER_RMI_PORT);
            try {
                int port = Integer.parseInt(serverPort.getText());
                communicator.initialize(IPaddress.getText(), port);
                if(password.getText().equals("")) password.setText(DEFAULT_PASS);
                communicator.login(username.getText(), password.getText());
                GUIData.getGUIData().setCommunicator(communicator);
                GUIData.getGUIData().setUsername(username.getText());
                GUIData.getGUIData().setView(view);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                try {
                    view.setUsername(username.getText());
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/GUI/lobby.fxml"))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stage.centerOnScreen();
            } catch (NetworkErrorException | NotValidInputException e) {
                logger.log(Level.SEVERE, "Network Error.");
                //serverPort.setText("Error.");
                //IPaddress.setText("Error.");
            }catch (NumberFormatException e){
                logger.log(Level.SEVERE, "Wrong server port.");
            }
        } else {
            view = new GUIView();
            view.setGUIManager(this);
            communicator = new CommunicatorSocket(view);
            if(IPaddress.getText().equals("")) IPaddress.setText(DEFAULT_SERVER);
            if(serverPort.getText().equals("")) serverPort.setText(DEFAULT_SERVER_SOCKET_PORT);
            try {
                int port = Integer.parseInt(serverPort.getText());
                communicator.initialize(IPaddress.getText(), port);
                if(password.getText().equals("")) password.setText(DEFAULT_PASS);
                communicator.login(username.getText(), password.getText());
                GUIData.getGUIData().setCommunicator(communicator);
                GUIData.getGUIData().setUsername(username.getText());
                GUIData.getGUIData().setView(view);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                try {
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/GUI/lobby.fxml"))));
                } catch (IOException e) {
                    logger.log(Level.SEVERE, e.toString());
                }
                stage.centerOnScreen();
            } catch (NetworkErrorException | NotValidInputException e) {
                logger.log(Level.SEVERE, "Network Error.");
            } catch (NumberFormatException e){
                logger.log(Level.SEVERE, "Wrong server port.");
            }
        }
        event.consume();
    }

    /**
     * This method is called if the RMI item of the connectionButton is pressed and
     * it changes the text of the connectionButton to "RMI".
     */
    @FXML
    public void connectionRMI() {
        IPaddress.setVisible(true);
        serverPort.setVisible(true);
        login.setDisable(false);
        connection.setText("RMI");
    }

    /**
     * This method is called if the socket item of the connectionButton is pressed and
     * it changes the text of the connectionButton to "socket".
     */
    @FXML
    public void connectionSocket() {
        IPaddress.setVisible(true);
        serverPort.setVisible(true);
        login.setDisable(false);
        connection.setText("socket");
    }

    /**
     * It's called by the FXMLLoader when the file login.fxml is loaded.
     * It initializes all the javaFx application's items.
     */
    @FXML
    public void initialize() {
        Image image;
        InputStream inputStream= this.getClass().getResourceAsStream("/GUI/loginBackground.jpg");
        image = new Image(inputStream);
        background.setImage(image);
        background.setVisible(true);
        IPaddress.setVisible(false);
        serverPort.setVisible(false);
        login.setDisable(true);
    }
    public void editMessage(String message){}
    public void showPattern(PatternCard pattern){}
    public void updateTable(Table table){}
    public void displayPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard){}
    public void showScoreTrack(ScoreTrack scoreTrack){}
    public void activeElement(String element){}
}

