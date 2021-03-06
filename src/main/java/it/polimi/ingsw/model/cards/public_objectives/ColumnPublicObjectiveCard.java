package it.polimi.ingsw.model.cards.public_objectives;

import it.polimi.ingsw.model.game.WindowFrame;
import java.util.HashSet;
import static it.polimi.ingsw.model.cards.patterns.PatternCard.COLUMN;
import static it.polimi.ingsw.model.cards.patterns.PatternCard.ROW;

public class ColumnPublicObjectiveCard extends PublicObjectiveCard {

    private String restriction;

    /**
     * creates a new {@link ColumnPublicObjectiveCard} from the given parameters.
     * @param name : the given {@link String} name
     * @param ID : the given {@link Integer} ID
     * @param points : the given {@link Integer} points
     * @param restriction : the given {@link String} restriction
     */
    public ColumnPublicObjectiveCard(String name, int ID, int points, String restriction) {
        super(name, ID, points);
        this.restriction = restriction;
    }

    /**
     * @param windowFrame : the given {@link WindowFrame}
     * @return the resulting score of the given {@link WindowFrame}
     */
    @Override
    public int countScore(WindowFrame windowFrame) {
        int score = 0;

        for (int i = 1; i <= COLUMN; i++) {
            HashSet<String> set = new HashSet<String>();
            for (int j = 1; j <= ROW; j++) {
                if(windowFrame.getDice(j, i) != null) {
                    if (restriction.equals("color")) {
                        set.add(windowFrame.getDice(j, i).getColor().escape());
                    } else if(restriction.equals("face")) {
                        set.add(windowFrame.getDice(j, i).getFace());
                    }
                }
            }
            if (set.size()==ROW) {
                score += points;
            }
        }
        return score;
    }
}
