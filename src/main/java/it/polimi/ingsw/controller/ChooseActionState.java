package it.polimi.ingsw.controller;

import java.util.ArrayList;

import static it.polimi.ingsw.controller.Controller.*;

public class ChooseActionState extends State {

    private static final String TOOLCARD_MESSAGE = "TOOLCARD";
    private static final String TOOLCARD = "toolcard";
    private static final String SKIP = "skip";
    private static final String MOVE = "move";

    /**
     * creates a new {@link ChooseActionState}.
     */
    public ChooseActionState(Controller controller) {
        super(controller);
    }

    /**
     *handles the given commands for the given user.
     */
    @Override
    public void handleEvent(String username, ArrayList<String> commands) {
        if (!game.isGameEnded() && game.isCurrentPlayer(username)) {
            if (commands.size() == CHOOSE_ACTION_DIM) {
                switch (commands.remove(0)) {
                    case MOVE:
                        if(game.moveAllowed()) {
                            game.createMove();
                            controller.sendActiveTableElement(username);
                            controller.setState(controller.getMoveState());
                        } else {
                            controller.itsYourTurn();
                        }
                        break;
                    case SKIP:
                        controller.skipTurn();
                        break;
                    case TOOLCARD:
                        if (!game.isToolCardUsed()) {
                            controller.sendMessage(username, CHOOSE_TOOL_CARD);
                            controller.setState(controller.getBuyToolCardState());
                            controller.sendActiveTableElement(username, TOOLCARD_MESSAGE);
                        } else {
                            controller.itsYourTurn();
                        }
                        break;
                    default:
                        controller.sendMessage(username, INVALID_FORMAT);
                        break;
                }
            } else {
                controller.sendMessage(username, INVALID_FORMAT);
            }
        } else {
            controller.sendMessage(username, WAIT_YOUR_TURN);
        }
    }

    /**
     *exits the given username for the game
     */
    @Override
    public void exitGame(String username) {
        super.exitGame(username);
        if (!game.isGameEnded() && game.getCurrentPlayer().equals(username)) {
            controller.skipTurn();
        }
    }
}
