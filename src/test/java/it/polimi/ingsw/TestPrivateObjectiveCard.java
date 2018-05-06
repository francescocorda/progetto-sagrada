package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.InvalidFirstMoveException;
import it.polimi.ingsw.exceptions.InvalidNeighboursException;
import it.polimi.ingsw.exceptions.MismatchedRestrictionException;
import it.polimi.ingsw.exceptions.OccupiedCellException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPrivateObjectiveCard  {

    @Test
    public void countScoreTest() {
        WindowFrame window = new WindowFrame();
        PatternDeck deck = new PatternDeck();
        //deck.createPatternDeck();
        PatternCard pattern = deck.getPatternDeck().get(10);
        window.setPatternCard(pattern);
        window.dump();
        Dice dice1 = new Dice(Color.ANSI_RED);
        Dice dice2 = new Dice(Color.ANSI_RED);
        Dice dice3 = new Dice(Color.ANSI_RED);
        Dice dice4 = new Dice(Color.ANSI_RED);

        PrivateObjectiveCard red = new PrivateObjectiveCard("red",4, Color.ANSI_RED);

        try {
            window.setDice(1,2, dice1);
            window.setDice(2,3, dice2);
            window.setDice(3,4, dice3);
            window.setDice(4,5, dice4);
        } catch (MismatchedRestrictionException e) {
            e.printStackTrace();
        } catch (InvalidNeighboursException e) {
            e.printStackTrace();
        } catch (InvalidFirstMoveException e) {
            e.printStackTrace();
        } catch (OccupiedCellException e) {
            e.printStackTrace();
        }

        window.dump();
        int score = dice1.valueOf()+dice2.valueOf()+dice3.valueOf()+dice4.valueOf();
        assertEquals(score, red.countScore(window));

    }
}