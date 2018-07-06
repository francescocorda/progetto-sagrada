package it.polimi.ingsw.model.cards.public_objectives;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.game.WindowFrame;

public class PublicObjectiveCard extends ObjectiveCard {

    int points;

    public PublicObjectiveCard(String name, int ID, int points) {
        super(name, ID);
        this.points = points;
    }

    public PublicObjectiveCard(){}

    @Override
    public int countScore(WindowFrame windowFrame) {
        return 0;
    }

    public int getPoints() {
        return points;
    }


    @Override
    public String toString() {
        String string = super.toString();
        string=string.concat("\nPoints: " + points);
        return string;
    }

    @Override
    public void dump() {
        System.out.println(toString());
    }

}