package porprezhas.RMI.Client;


import porprezhas.RMI.RemoteObservable;
import porprezhas.model.Game;
import porprezhas.model.GameInterface;
import porprezhas.model.Player;
import porprezhas.model.dices.Box;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.Pattern;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Observable;

public class ViewClient {

    private Player player;
    private Player currentPlayer;
    private Boolean first;
    private Boolean firstPlayer;
    private final int numberPlayer;
    private Player nextPlayer;
    private ArrayList<Player> localPlayers;
    private ArrayList<Player> players;
    private final int HEIGHT = 4;
    private final int WIDTH = 5;
    private String username;
    private Boolean gameStarted= false;
    public ViewClient(int numberPlayer, String username) {

        first = false;
        firstPlayer= false;
        this.numberPlayer= numberPlayer;
        this.username=username;
    }


    public int getHEIGHT() {
        return HEIGHT;
    }

    public int getWIDTH() {
        return WIDTH;
    }
    
    public boolean printAll(boolean bFixedFont, int numberOfPlayer) {

        Pattern pattern;
        char color;
        char number;
        int counter=0;

        for (Player player :
                players) {
            if(player.getName().equals(this.currentPlayer.getName()))
                System.out.printf("▶ ");
            else System.out.printf("  ");
            System.out.printf("%-25s\t\t",player.getName() );
        }
        System.out.println();
        for (Player player :
                players) {


            pattern = player.getBoard().getPattern();


            // start printing
//        ╔╗╚════║║║╝
//        ╧ ╤ ╟┼╢──│

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

        public void updateClient(Observable game) throws RemoteException {
          /*  game= (RemoteObservable) game;
         Game.NotifyState state = game.getGameNotifyState();
         players = game.getPlayers();
         this.currentPlayer= game.getActualPlayer();
         if(gameStarted) {
             System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
             if (game.getActualPlayer().getName().equals(this.username))
                 System.out.println("Questo è il tuo turno!");
             else
                 System.out.println("Ora sta giocando: " + game.getActualPlayer().getName());

             System.out.println(" ");

             System.out.println("Riserva:");
             for(int i=0; i< game.getDraftpoolRmi().diceList().size(); i++){
                 System.out.printf("    (%d)      ", i+1);

             }
             System.out.println(" ");
             for (Dice dice :
                     game.getDraftpoolRmi().diceList()) {
                 String colorForm=  dice.getColorDice().toString();
                 colorForm=colorForm.concat("]");
                 System.out.printf(" [%d %-6s   ", dice.getDiceNumber(), colorForm);
             }

             System.out.println("\n");
             printAll(false, 4);

         }
//①②③④⑤⑥⑦⑧⑨⑩
        switch(state){

            case NEW_TURN:
                if(!firstPlayer || !nextPlayer.getName().equals(game.getFirstPlayer().getName()) ) {
                    nextPlayer=game.getFirstPlayer();

                    System.out.println("Now the first player is: " + game.getFirstPlayer().getName());
                    firstPlayer=true;
                }
                break;

            case CHOOSE_PATTERN:

                System.out.println("You have to choose your pattern");
                Pattern.TypePattern pattern1 = game.getPlayers().get(numberPlayer).getPatternsToChoose().get(0);
                Pattern.TypePattern pattern2 = game.getPlayers().get(numberPlayer).getPatternsToChoose().get(1);
                System.out.println(pattern1.name() + "    o    " + pattern2.name());
                break;

            case GAME_STARTED:

                System.out.println("Game started!");
                this.gameStarted=true;
                this.printAll(false, 4);
                break;

            case NEXT_ROUND:

                System.out.println("Next Round");
                break;

            case DICE_INSERTED:

                    localPlayers= players;
                    player= players.get(game.getiCurrentPlayer());
                   // System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println(player.getName() + " inserted a dice:");
                    //this.printAll(false, 4);
                    first=true;

                break;

            case BOARD_CREATED:
                System.out.println("Pattern inserted in the board");
                break;
        }


*/

    }


    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
