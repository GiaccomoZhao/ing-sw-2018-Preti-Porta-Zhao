package porprezhas.Network;

import javafx.application.Platform;
import porprezhas.model.Game;
import porprezhas.model.Player;
import porprezhas.model.SerializableGameInterface;
import porprezhas.model.dices.Dice;
import porprezhas.view.fx.gameScene.controller.GameViewController;

import java.util.Arrays;
import java.util.List;

public class GUIViewUpdateHandler implements ViewUpdateHandlerInterface {
    private GameViewController gameViewController;
    private Boolean gameStarted = false;
    private Boolean updateGui=false;

    public GUIViewUpdateHandler(GameViewController gameViewController) {
        this.gameViewController = gameViewController;
    }

    @Override
    public void update(SerializableGameInterface game) {
        List<Player> players = game.getPlayerList();
        Game.NotifyState state = game.getGameNotifyState();

        if(gameStarted) {
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
            if (game.getCurrentPlayer().getPosition() == this.gameViewController.getPlayerPosition())
                System.out.println("This is your turn!");
            else
                System.out.println("Now is playing: " + game.getCurrentPlayer().getName());

            System.out.println(" ");

            System.out.println("Riserva:");
            for(int i=0; i< game.getDraftPool().diceList().size(); i++){
                System.out.printf("    (%d)      ", i+1);

            }
            System.out.println(" ");
            for (Dice dice :
                    game.getDraftPool().diceList()) {
                String colorForm=  dice.getColorDice().toString();
                colorForm=colorForm.concat("]");
                System.out.printf(" [%d %-6s   ", dice.getDiceNumber(), colorForm);
            }

            System.out.println("\n");
//            printAll(false, 4, game.getPlayerList(), game.getCurrentPlayer());
            if (updateGui) {
                Platform.runLater(() -> {
                    gameViewController.setupView(players, game.getToolCardList(), game.getPublicObjectiveCardList(), players.get(0).getPrivateObjectiveCardList());
                });
                gameViewController.updateDraftPool(game.getDraftPool());
            updateGui=false;
            }
        }
//①②③④⑤⑥⑦⑧⑨⑩
        switch(state){

            case CHOOSE_PATTERN:

                System.out.println("You have to choose your pattern");
//                Pattern.TypePattern pattern1 = game.getUsernamePlayer(username).getPatternsToChoose().get(0);
//                Pattern.TypePattern pattern2 = game.getUsernamePlayer(username).getPatternsToChoose().get(1);
//                System.out.println(pattern1.name() + "    o    " + pattern2.name());
                break;


            case GAME_STARTED:

                System.out.println("Game started!");
                this.gameStarted=true;
//                this.printAll(false, 4, game.getPlayerList(), game.getCurrentPlayer());
//                System.out.println(gameViewController);
                Platform.runLater(() -> {
                    gameViewController.setupView(players, game.getToolCardList(), game.getPublicObjectiveCardList(), players.get(0).getPrivateObjectiveCardList());
                } );
                break;


            case NEXT_ROUND:
                System.out.println("Next Round");

                Platform.runLater(() -> {
                    // change the bag position
                    gameViewController.updateFirstPlayer(game.getCurrentPlayer());

                    // re-roll dices of Draft pool
                    gameViewController.updateDraftPool(game.getDraftPool().diceList());

                    gameViewController.updateRoundTrack(game.getRoundTrack().getTrack());
                });

                // after next_round, update current player too

            case NEW_TURN:
                Platform.runLater(() -> {
                    gameViewController.updateTimer(game.getCurrentPlayer());
                });
                break;


            case DICE_INSERTED:
                System.out.println(game.getCurrentPlayer().getName() + " inserted a dice:");

                Platform.runLater(() -> {
                    for (Player player:
                            game.getPlayerList()) {

                        gameViewController.updateBoard(
                                player.getPosition(),
                                player.getBoard().getBoard());

                        // check different with new draft pool
                        // add / remove different dice
                        gameViewController.updateDraftPool(game.getDraftPool());
                    }

                });

                break;

            case BOARD_CREATED:
                System.out.println("Pattern inserted in the board");
                break;

        }

        // foreach player: update board
        //
    }

    @Override
    public void setGameStarted(Boolean gameStarted) {
        this.gameStarted=gameStarted;
        this.updateGui=true;
    }

    @Override
    public void invalidAction() {

    }

    @Override
    public void invalidDiceInsert(Exception e) {

    }
}
