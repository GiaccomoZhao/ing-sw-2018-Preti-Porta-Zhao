package porprezhas.Network;

import javafx.application.Platform;
import porprezhas.model.Game;
import porprezhas.model.Player;
import porprezhas.model.SerializableGameInterface;
import porprezhas.model.dices.Dice;
import porprezhas.view.fx.gameScene.controller.GameViewController;

import java.util.List;

public class GUIViewUpdateHandler implements ViewUpdateHandlerInterface {
    private GameViewController gameViewController;
    private Boolean gameStarted = false;

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
                System.out.println("Questo è il tuo turno!");
            else
                System.out.println("Ora sta giocando: " + game.getCurrentPlayer().getName());

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

        }
//①②③④⑤⑥⑦⑧⑨⑩
        switch(state){

            case NEW_FIRST_PLAYER:
                // TODO: this is not first player... but the current player.
                // if(!firstPlayer || !nextPlayer.getName().equals(game.getFirstPlayer().getName()) ) {
                //   nextPlayer=game.getFirstPlayer();
//
                //                  System.out.println("Now the first player is: " + game.getFirstPlayer().getName());
                //                firstPlayer=true;
                //          }
                System.out.println();
                gameViewController.updateFirstPlayer(game.getCurrentPlayer());
                break;

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
                System.out.println(gameViewController);
                gameViewController.updatePlayerInfo(players);
                break;

            case NEXT_ROUND:

                gameViewController.updateDraftPool(game.getDraftPool().diceList());
                System.out.println("Next Round");
                break;

            case DICE_INSERTED:


                System.out.println(game.getCurrentPlayer().getName() + " inserted a dice:");

                Platform.runLater(new Runnable() {
                    public void run() {
                        for (Player player:
                                game.getPlayerList()) {
                            gameViewController.updateBoard(
                                    player.getPosition(),
                                    player.getBoard().getBoard());
                            gameViewController.updateDraftPool(game.getDraftPool().diceList());
                        }

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
}
