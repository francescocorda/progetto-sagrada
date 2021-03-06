package it.polimi.ingsw.model.game;


import it.polimi.ingsw.exceptions.InvalidFaceException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestRoundTrack {
    @Test
    public void roundTrackTest() {
        Table table = new Table();
        table.setDrawPool(9);
        ArrayList<Dice> dices = new ArrayList<>();
        dices.add(new Dice(Color.PURPLE));
        try {
            dices.get(0).setFace(2);
        } catch (InvalidFaceException e) {
            e.printStackTrace();
        }
        table.getRoundTrack().setRoundDices(dices, 2);

        assertEquals(dices.get(0).getColor(), table.getRoundTrack().getRoundDices(2).get(0).getColor());
        assertEquals(dices.get(0).getFace(), table.getRoundTrack().getRoundDices(2).get(0).getFace());
    }
}
