package it.polimi.ingsw.Model.Game;

import it.polimi.ingsw.Model.Game.*;
import it.polimi.ingsw.exceptions.NotValidInputException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestPlayerTurn {

    @Test
    void myTest() {
        Player player = new Player("fra");
        ArrayList<PlayerTurn> playerTurns = new ArrayList<>();
        RoundTrack roundTrack = new RoundTrack();
        DiceBag diceBag = new DiceBag();

        try {
            player.setNumOfTokens(6);
        } catch (NotValidInputException e) {
            e.printStackTrace();
        }

        ArrayList<Dice> draftpool = new ArrayList<>();
        for(int i=0; i<9;i++) {
            draftpool.add(new Dice(Color.ANSI_BLUE));
            draftpool.get(i).roll();
        }
        PlayerTurn turn = new PlayerTurn(player, draftpool, roundTrack, diceBag);

        assertEquals(player, turn.getPlayer());


        try {
            turn.payTokens(6);
        } catch (NotValidInputException e) {
            e.printStackTrace();
        }

        assertEquals(0, turn.getPlayer().getNumOfTokens());

        //turn.dump();

        assertThrows(NotValidInputException.class,()->turn.payTokens(1));
    }
}
