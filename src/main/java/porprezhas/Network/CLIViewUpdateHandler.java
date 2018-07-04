package porprezhas.Network;


import porprezhas.model.Game;
import porprezhas.model.Player;
import porprezhas.model.SerializableGameInterface;
import porprezhas.model.cards.Card;
import porprezhas.model.dices.Box;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.DraftPool;
import porprezhas.model.dices.Pattern;
import porprezhas.view.fx.gameScene.GuiSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static porprezhas.model.Game.NotifyState.ALT_GAME;

/**
 * This class receives all the game updates from the server and print those informations.
 */
public class CLIViewUpdateHandler implements ViewUpdateHandlerInterface {
    boolean bFixedFont = GuiSettings.bFixedFont;

    private String username;
    private final int HEIGHT = 4;
    private final int WIDTH = 5;
    private Boolean gameStarted;
    private List<Long> idList;

    public CLIViewUpdateHandler(String username) {
        this.username = username;
        gameStarted= false;
        idList = new ArrayList<>();
    }


    public int getHEIGHT() {
        return HEIGHT;
    }

    public int getWIDTH() {
        return WIDTH;
    }


    public void setGameStarted(Boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    /**
     * This method is called when a not current user tries to make a pass move
     */
    @Override
    public void invalidAction() {
        System.out.println("It's not your turn");
    }

    /**
     * This method prints the cause of the wrong answer to Dice insert.
     * @param e the exception raised by the insert Dice action
     */
    @Override
    public void invalidDiceInsert(Exception e) {
        System.out.println(e.getMessage());
    }
    /**
     * This method prints the cause of the wrong answer to Use ToolCard.
     * @param e the exception raised by the use ToolCard action.
     */
    @Override
    public void invalidUseToolCard(Exception e) {
        System.out.println(e.getMessage());
    }

    @Override
    public void toolCardUsed() {

    }


    /** This method prints the board of all players and their names
     *
     * @param bFixedFont    Boolean used to change the font
     * @param numberOfPlayer    Number of the players
     * @param players   List of the players
     * @param currentPlayer Current Player of this turn
     * @return true if the the print is successfully
     */
    public boolean printAll(boolean bFixedFont, int numberOfPlayer, List<Player> players, Player currentPlayer) {

        Pattern pattern;
        char color;
        char number;
        int counter=0;

        for (Player player :
                players) {
            if(player.getName().equals(currentPlayer.getName()))
                System.out.printf("▶ ");
            else System.out.printf("  ");
            System.out.printf("%-25s\t\t",player.getName() );
        }
        System.out.println();
        for (Player player :
                players) {


           // pattern = player.getBoard().getPattern();



            // print TOP border
            if (bFixedFont) {
                System.out.print("╔══");
            } else {
                System.out.print("╔══");
            }
            for (int i = 0; i < this.getWIDTH() - 1; i++) {
                if (bFixedFont) {
                    System.out.print("══╤══");
                } else {
                    System.out.print("═══╤══");
                }
            }
            if (bFixedFont)
                System.out.print("══╗ \t\t");
            else
                System.out.print("═══╗ \t\t");
        }
        System.out.println("");


        // print all board and pattern LINEs
        for (int y = 0; y < this.getHEIGHT(); y++) {

            for (Player player :
                    players) {


                // FIRST Line
                // print a part of LEFT border
                System.out.print("║");

                //       *****************
                // print **>>> BOARD <<<**
                for (int x = 0; x < this.getWIDTH(); x++) {
                    // print DICE
                    Dice dice = player.getBoard().getDice(y, x);
                    number = (char) ('0' + dice.getDiceNumber());
                    color = dice.getColorDice().name().charAt(0);
                    if (number == '0')
                        number = ' ';
                    if (color == 'W')
                        color = ' ';
                    System.out.format(" %c%C ", number, color);

                    // print mid column separator
                    if (x != this.getWIDTH() - 1) {
                        // switching between large and small separator to adapt the width size to 2 normal char + a space
                        if (x % 2 == 0) {
                            System.out.print("|");
                        } else {
                            System.out.print("│");
                        }
                    }
                }
                System.out.print("║ \t\t");
            }
            System.out.println("");

            for (Player player :
                    players) {
                // SECOND Line
                // print a part of LEFT border
                System.out.print("║");

                // print **>>> PATTERN <<<**
                //       *******************
                for (int x = 0; x < getWIDTH(); x++) {
                    //
                    Box box = player.getBoard().getPattern().getBox(y, x);
                    number = (char) ('0' + box.getNumber());
                    color = box.getColor().name().toLowerCase().charAt(0);
                    if (number == '0')
                        number = ' ';
                    if (color == 'w')
                        color = ' ';
                    System.out.format(" %c%c ", number, color);

                    // print mid column separator
                    if (x != this.getWIDTH() - 1) {
                        // switching between large and small separator to adapt the width size to 2 normal char + a space
                        if (x % 2 == 0) {
                            System.out.print("|");
                        } else {
                            System.out.print("│");
                        }
                    }
                }


                // print a part of RIGHT border
                System.out.print("║ \t\t");

            }
            if(  counter<3 ){
                System.out.println("");
                counter++;
            }

            for (Player player :
                    players) {
                // print horizontal separator + left and right border
                if (y != this.getHEIGHT() - 1) {
                    System.out.print("╟──");
                    for (int x = 0; x < this.getWIDTH() - 1; x++) {
                        if (bFixedFont) {
                            System.out.print("──┼──");
                        } else {
                            System.out.print("───┼──");
                        }
                    }
                    if (bFixedFont) {
                        System.out.print("──╢ \t\t");
                    } else {
                        System.out.print("───╢ \t\t");
                    }
                }
            }
            System.out.println("");


        }

        for (Player player :
                players) {
            // print BOTTOM border
            if (bFixedFont)
                System.out.print("╚══");
            else
                System.out.print("╚══");
            for (int i = 0; i < this.getWIDTH() - 1; i++) {
                if (bFixedFont)
                    System.out.print("══╧══");
                else
                    System.out.print("═══╧══");
            }
            if (bFixedFont)
                System.out.print("══╝ \t\t");
            else
                System.out.print("═══╝ \t\t");



        }
        System.out.println("");
        return true; // printed successfully
    }

    /**
     * This method takes the notify from the server and use all his informations.
     * It analyzes the type of NotifyStates and decides what to print.
     * @param game This is the serializable and only-readable version of game
     */
    public void update(SerializableGameInterface game){


        List<Player> players = game.getPlayerList();
        Game.NotifyState state = game.getGameNotifyState();

        if (state.equals(Game.NotifyState.RANKING)){
            System.out.println("***** >>> GAME OVER <<< *****");
            System.out.println("********** RANKING **********");
            HashMap ranking= game.getRanking();
            for (Player player:
                   players ) {
                System.out.println(player.getName() + ":   " + ranking.get(player));
            }
            System.out.println(" ");
            System.out.println("THE WINNER IS " + game.getWinner().getName() );
            return;
        }
        if (state.equals(ALT_GAME)){
            System.out.println("****** >>> GAME OVER <<< ******");
            System.out.println("You are the only player in game");
            System.out.println("*********** YOU WON! **********");
            return;
        }
        if(gameStarted) {
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
            for (Player player:
                 game.getPlayerList()) {
                if (player.getName().equals(username)){
                    System.out.println("Your favor tokens are " + player.getFavorToken());
                    System.out.println("Private card: " + player.getPrivateObjectiveCardList().get(0));
                }
            }
            for (Player player:
                 players) {
                if (!player.getName().equals(username))
                System.out.printf(player.getName() + " has " + player.getFavorToken() + " token.  ");
            }
            System.out.println(" ");
           System.out.println("Public cards:");
            for (Card card:
                 game.getPublicObjectiveCardList()) {
                System.out.println(card.toString());
            }
            System.out.println("Tool cards:");
            for (Card card:
                    game.getToolCardList()) {
                System.out.println(card.toString());
            }
            System.out.println();
            System.out.println();
            if (game.getCurrentPlayer().getName().equals(this.username))
                System.out.println("This is your turn!");
            else
                System.out.println("Now is playing: " + game.getCurrentPlayer().getName());
//            System.out.println("Round Track: ");
            System.out.println(game.getRoundTrack());


            System.out.println();
            System.out.println("Draft Pool: ");
            for(int i=0; i< game.getDraftPool().diceList().size(); i++){
                System.out.printf("    (%d)      ", i+1);

            }
            System.out.println();
            for (Dice dice :
                    game.getDraftPool().diceList()) {
                String colorForm=  dice.getColorDice().toString();
                colorForm=colorForm.concat("]");
                System.out.printf(" [%d %-6s   ", dice.getDiceNumber(), colorForm);
            }

            System.out.println("\n");
            printAll(bFixedFont, 4, game.getPlayerList(), game.getCurrentPlayer());

        }

        switch(state){

            case NEW_TURN:
               // if(!firstPlayer || !nextPlayer.getName().equals(game.getFirstPlayer().getName()) ) {
                 //   nextPlayer=game.getFirstPlayer();
//
  //                  System.out.println("Now the first player is: " + game.getFirstPlayer().getName());
    //                firstPlayer=true;
      //          }
                break;

            case CHOOSE_PATTERN:

                System.out.println("You have to choose your pattern");

                for (Pattern.TypePattern pattern:
                        game.getUsernamePlayer(username).getPatternsToChoose()) {
                    System.out.println(pattern.name() );

                }
                System.out.println("Type the number of the pattern");
                break;

            case GAME_STARTED:

                System.out.println("Game started!");
                this.gameStarted=true;
                this.printAll(bFixedFont, 4, game.getPlayerList(), game.getCurrentPlayer());

                break;

            case NEXT_ROUND:

                System.out.println("Next Round");

                saveDiceID(game.getDraftPool().diceList());
                break;

            case DICE_INSERTED:


                System.out.println(game.getCurrentPlayer().getName() + " inserted a dice:");

                // refresh dices
                saveDiceID(game.getDraftPool().diceList());

                break;

            case BOARD_CREATED:
                System.out.println("Pattern inserted in the board");
                for (Player player : players) {
                    if (player.getName().equals(username)) {
                        System.out.println(player.getBoard().getPattern());
                    }
                }
                break;

            case PLAYER_QUIT:
                System.out.println(game.getQuitPlayer().getName() + " quit the game");

                break;

            case PLAYER_BACK:
                if (!this.username.equals(game.getResumePlayer()))
                    System.out.println(game.getResumePlayer() + " is now back in game");
                else
                    saveDiceID(game.getDraftPool().diceList());
                break;

        }
    }


    public long getID(int index) {
        return idList.get(index);
    }


    public void saveDiceID(List<Dice>  diceList) {
        idList.clear();
        for (int i = 0; i < diceList.size(); i++) {
            idList.add(diceList.get(i).getId());
        }
    }
}
