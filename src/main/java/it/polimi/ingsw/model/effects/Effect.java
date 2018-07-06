package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.game.Round;
import it.polimi.ingsw.model.game.Table;
import it.polimi.ingsw.exceptions.ImpossibleMoveException;
import it.polimi.ingsw.exceptions.RollBackException;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Effect implements Serializable {

    boolean stop;
    int commandsLenght;

    public abstract boolean applyEffect(ArrayList<String> commands, Table table, Round round) throws RollBackException, ImpossibleMoveException;
    public abstract void explainEffect(Table table, Round round);
    public boolean isStop() {
        return stop;
    }
    public int getCommandsLenght() {
        return commandsLenght;
    }
    public abstract String getActiveTableElement();
}