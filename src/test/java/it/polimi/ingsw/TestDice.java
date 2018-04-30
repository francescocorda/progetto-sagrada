package it.polimi.ingsw;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class TestDice {
    @Test
    public void colorTest(){
        Dice dice = new Dice(Color.ANSI_RED);
        System.out.println(dice.valueOf());
        assertEquals(Color.ANSI_RED, dice.getColor());
        dice.setColor(Color.ANSI_BLUE);
        assertEquals(Color.ANSI_BLUE, dice.getColor());
        dice.setColor(Color.ANSI_PURPLE);
        assertEquals(Color.ANSI_PURPLE, dice.getColor());
        dice.setColor(Color.ANSI_GREEN);
        assertEquals(Color.ANSI_GREEN, dice.getColor());
        dice.setColor(Color.ANSI_YELLOW);
        assertEquals(Color.ANSI_YELLOW, dice.getColor());
    }

    @Test
    public void numbersTest(){
        Dice dice = new Dice(Color.ANSI_RED);
        assertTrue((dice.valueOf()>0)&&(dice.valueOf()<7));
    }

    @Test
    public void oppositeFaceTest(){
        Dice dice = new Dice(Color.ANSI_YELLOW);
        for(int i =1; i<7; i++){
            dice.setFace(i);
            assertEquals(6-dice.valueOf()+1, dice.getOppositeFace().compareTo("\u2680")+1);
        }
    }

    @Test
    public void setFaceTest(){
        Dice dice = new Dice(Color.ANSI_YELLOW);
        assertFalse(dice.setFace(7));
        assertFalse(dice.setFace(-1));
    }




}
