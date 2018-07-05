package porprezhas.view.terminal;

import porprezhas.model.Game;
import porprezhas.model.Player;
import porprezhas.model.dices.Board;
import porprezhas.model.dices.Box;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.Pattern;

public class ClientTerminal {
    Game game;
    Player player;

    public ClientTerminal() {
    }

    public void setup(Game g, Player p) {
        game = g;
        player = p;
    }

    public boolean print() {
        // get information
        Board board = player.getBoard();
        Pattern pattern = board.getPattern();
        if (board == null || pattern == null) {
            return false;   // NOTE: throw Exception instead?
        }
        // start printing
//        ╔╗╚════║║║╝
//        ╧ ╤ ╟┼╢──│
        System.out.println("\nPlayer name: " + player.getName());

        // print TOP border
        System.out.print("╔══");
        for (int i = 0; i < board.getWidth() - 1; i++) {
            System.out.print("═══╤══");
        }
        System.out.println("═══╗");

        // print all board and pattern LINEs
        for (int y = 0; y < board.getHeight(); y++) {
            // print a part of LEFT border
            System.out.print("║");

            // print BOARD
            for (int x = 0; x < board.getWidth(); x++) {
                // print DICE
                Dice dice = board.getDice(y, x);
                System.out.format(" %d%C ", dice.getDiceNumber(), dice.getDiceColor().name().charAt(0));

                // print mid column separator
                if (x != board.getWidth() - 1) {
                    // switching between large and small separator to adapt the width size to 2 normal char + a space
                    if (x % 2 == 0) {
                        System.out.print("|");
                    } else {
                        System.out.print("│");
                    }
                }
            }

            // print PATTERN
            for (int x = 0; x < pattern.getWidth(); x++) {
                //
                Box box = pattern.getBox(y, x);
                System.out.format(" %d%c ", box.getNumber(), box.getColor().name().charAt(0));

                // print mid column separator
                if (x != board.getWidth() - 1) {
                    // switching between large and small separator to adapt the width size to 2 normal char + a space
                    if (x % 2 == 0) {
                        System.out.print("|");
                    } else {
                        System.out.print("│");
                    }
                }
            }

            // print a part of RIGHT border
            System.out.println("║");
            System.out.println("║");

            // print horizontal separator + left and right border
            if (y != board.getHeight() - 1) {
                System.out.print("╟──");
                for (int x = 0; x < board.getWidth() - 1; x++) {
                    System.out.print("───┼──");
                }
                System.out.println("───╢");
            }
        }

        // print BOTTOM border
        System.out.print("╚══");
        for (int i = 0; i < board.getWidth() - 1; i++) {
            System.out.print("═══╧══");
        }
        System.out.println("═══╝");

        return true; // printed successfully
    }
}
