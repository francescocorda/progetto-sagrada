package it.polimi.ingsw.server;

import it.polimi.ingsw.database.ClientData;
import it.polimi.ingsw.database.ClientDatabase;
import it.polimi.ingsw.database.Phase;
import it.polimi.ingsw.database.VirtualViewsDataBase;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.NetworkErrorException;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;


public class Lobby implements Observer {
    private ArrayList<VirtualView> views;
    private static Lobby instance = null;
    private ArrayList<ClientData> connectedPlayers;
    private ArrayList<Long> connectedPlayersLastTime;
    private Timer timer;
    private boolean isTimerSet = false;
    private ClientDatabase players;
    private static final String LOCATION = "lobby";
    private ArrayList<Controller> controllers;
    private int timerSeconds;
    private VirtualViewsDataBase vvdb;

    private Lobby() {
        connectedPlayers = new ArrayList<>();
        connectedPlayersLastTime = new ArrayList<>();
        timer = new Timer();
        players = ClientDatabase.getPlayerDatabase();
        views = new ArrayList<>();
        controllers = new ArrayList<>();
        timerSeconds = ServerMain.getServerMain().getTimerSeconds();
        vvdb = VirtualViewsDataBase.getVirtualViewsDataBase();
    }

    public void status(){
        //TODO eliminate
        System.out.println("LOBBY:");
        System.out.println(" - Players in lobby:");
        for(VirtualView view : views){
            System.out.println("   + "+view.getUsername());
        }
        System.out.println(" - number of games: "+controllers.size());
        int counter = 0;
        for (Controller controller : controllers){
            System.out.println(" - Controller "+counter+":");
            System.out.println("   + Players:");
            for(String player : controller.getPlayers()){
                System.out.println("     > "+player);
            }
            System.out.println("\n   + Offline Players:");
            for(String player : controller.getOFFPlayer()){
                System.out.println("     > "+player);
            }
            counter++;
        }
    }

    public static synchronized Lobby getLobby() {
        if (instance == null) {
            instance = new Lobby();
        }
        return instance;
    }

    public synchronized void addPlayer(String username, long time) {
        ClientData player = players.getPlayerData(username);
        if (!reconnect(username)) {
            VirtualView virtualView;
            if(vvdb.contains(username)){
                virtualView = vvdb.getVirtualView(username);
            } else {
                virtualView = new VirtualView(player);
            }
            views.add(virtualView);
            vvdb.addVirtualView(virtualView);
            virtualView.deleteObservers();
            virtualView.addObserver(this);
            connectedPlayers.add(player);
            connectedPlayersLastTime.add(time);
            send(username, "/welcome");
            toTerminal("player: " + player.getUsername() + " signed in");
            broadcast("/player_joined/" + player.getUsername());
            broadcast(listOfPlayers());
            if (player.getPhase() == Phase.LOGIN)
                player.setPhase(Phase.LOBBY);
            trigger();
        }
    }

    public synchronized void removePlayer(String username) {
        ClientData player = players.getPlayerData(username);
        int index = connectedPlayers.indexOf(player);
        if(index<0)
            return;
        views.get(index).deleteObserver(this);
        views.remove(index);
        VirtualViewsDataBase.getVirtualViewsDataBase().removeVirtualView(username);
        connectedPlayers.remove(index);
        connectedPlayersLastTime.remove(index);
        toTerminal("User: " + player.getUsername() + " has logged out");
        broadcast("/player_left/" + username);
        broadcast(listOfPlayers());
        trigger();
    }

    private void broadcast(String message) {
        for (VirtualView view : views) {
            view.displayMessage(LOCATION + message);
        }
    }

    private void send(String username, String message) {
        for (VirtualView view : views) {
            if (view.getUsername().equals(username)) {
                view.displayMessage(LOCATION + message);
            }
        }
    }

    private synchronized void trigger() {
        switch (size()) {
            case 1:
                if (isTimerSet) {
                    timer.cancel();
                    timer.purge();
                    isTimerSet = false;
                    broadcast("/timer_reset");
                }
                break;
            case 2:
                if (!isTimerSet) {
                    isTimerSet = true;
                    broadcast("/timer_started");
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            startGame();
                        }
                    }, (long) timerSeconds * 1000);
                }
                break;
            case 4:
                startGame();
                break;
            default:
                break;
        }
    }

    private int size() {
        ArrayList<ClientData> playerToBeChecked = new ArrayList<>(connectedPlayers);
        for (ClientData player : playerToBeChecked) {
            try {
                player.getClientHandler().check();
            } catch (NetworkErrorException e) {
                players.disconnect(player.getUsername());
            }
        }
        return connectedPlayers.size();
    }

    private synchronized void startGame() {
        if (size() > 1) {
            isTimerSet = false;
            timer.cancel();
            toTerminal("game start");
            broadcast("/start_game" + listOfPlayers());
            ArrayList<ClientData> playersInTheRightOrder = new ArrayList<>();
            ArrayList<VirtualView> viewsInTheRightOrder = new ArrayList<>();
            while (!connectedPlayersLastTime.isEmpty()) {
                int indexOfMax = 0;
                for (Long lastTimeVisit : connectedPlayersLastTime) {
                    if (lastTimeVisit > connectedPlayersLastTime.get(indexOfMax)) {
                        indexOfMax = connectedPlayersLastTime.indexOf(lastTimeVisit);
                    }
                }
                playersInTheRightOrder.add(connectedPlayers.get(indexOfMax));
                viewsInTheRightOrder.add(views.get(indexOfMax));
                connectedPlayers.remove(indexOfMax);
                connectedPlayersLastTime.remove(indexOfMax);
                views.get(indexOfMax).deleteObserver(this);
                views.remove(indexOfMax);
            }
            for (ClientData player : playersInTheRightOrder) {
                player.nextPhase();
            }
            controllers.add(new Controller(controllers.size() + 1, viewsInTheRightOrder));
            connectedPlayers = new ArrayList<>();
            connectedPlayersLastTime = new ArrayList<>();
            views = new ArrayList<>();
            notifyAll();
        } else {
            trigger();
        }
    }

    private String listOfPlayers() {
        String message = "/list_of_players";
        for (ClientData player : connectedPlayers)
            message = message.concat("/" + player.getUsername());
        return message;
    }

    private void toTerminal(String message) {
        System.out.println("Lobby: " + message);
    }

    public void removeController(Controller controller) {
        controllers.remove(controller);
    }

    private boolean reconnect(String username) {
        for (Controller controller : controllers) {
            if (controller.contains(username)) {
                VirtualView view = VirtualViewsDataBase.getVirtualViewsDataBase().getVirtualView(username);
                view.addObserver(controller);
                view.notifyObservers(username + "/join");
                return true;
            }
        }
        return false;
    }

    public void close() {
        System.exit(0);
    }

    @Override
    public void update(String message) {
        if (message!=null)
            handleEvent(message);
    }

    @Override
    public void update(Observable o) {
        //handleEvent(message);
    }

    private synchronized void handleEvent(String message) {
        String username = new String();
        if (message == null)
            return;
        ArrayList<String> commands = new ArrayList<>(Arrays.asList(message.split("\\s*/\\s*")));
        if (commands.size() != 2)
            return;
        username = commands.remove(0);
        for (ClientData player : connectedPlayers) {
            if (player.getUsername().equals(username)) {
                String command = commands.remove(0);
                if (command.equals("logout")) {
                    players.disconnect(username);
                    VirtualViewsDataBase.getVirtualViewsDataBase().removeVirtualView(username);
                    return;
                }
            }
        }
    }
}
