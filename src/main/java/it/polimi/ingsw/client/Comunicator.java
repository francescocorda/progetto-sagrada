package it.polimi.ingsw.client;

import it.polimi.ingsw.exceptions.NetworkErrorException;
import it.polimi.ingsw.exceptions.NotValidInputException;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Comunicator {
    public void inizialize(ArrayList<String> parameters) throws NetworkErrorException;
    public void login(String username, String password) throws NetworkErrorException, NotValidInputException;
    public void lobby(String username, Long time) throws NetworkErrorException;
    public void sendMessage(String message) throws NetworkErrorException;
}