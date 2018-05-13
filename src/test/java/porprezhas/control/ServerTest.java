package porprezhas.control;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import porprezhas.model.Game;
import porprezhas.model.Player;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// test of integrity
public class ServerTest {
    // subjects to be tested
    ServerController server;
    GameControllerInterface newGame;
    List<Player> players;

    // test parameter
    final int NUM_PLAYER = 5;

    @Before
    public void setUp() throws Exception {
        // setup
        server = new ServerController(0);
        players = new ArrayList<>();
        for (int i = 0; i < NUM_PLAYER; i++) {
            String playerName = "P" + i;
            players.add(new Player(playerName));
            server.join(players.get(i));
        }
        if(NUM_PLAYER < Game.GameConstants.MAX_PLAYER_QUANTITY) {
            newGame = (GameController) server.createNewGame();
        } else {
            newGame = server.getGameController(players.get(0));
        }
    }


    @After
    public void tearDown() throws Exception {
        System.out.println("server tear down");
    }


    @Test
    public void test() {
        Thread gameThread;

        // Check server contains the new Game
        boolean bContain = false;
        List<GameControllerInterface> gameControllers = server.getGameControllers();
        for (GameControllerInterface gameController:
             gameControllers) {
            if(gameController.equals(newGame)){
                bContain = true;
            }
        }
        assertTrue(bContain);

        // Check server contains all players
        for (int i = 0; i < NUM_PLAYER; i++) {
            try {
                assertTrue(server.isAlreadyInGame(players.get(0)));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }


        (gameThread = new Thread((Runnable) newGame)).start();

/*      Game.orderPlayers() already prints it
        //Check player list Order
        synchronized (System.out) {   // to avoid the thread prints something inside this block of message
            for (GameControllerInterface gameController :
                    gameControllers) {
                List<Player> playerList = gameController.getGame().getPlayerList();
                System.out.println("\n  Order       Name          Order of join  ");
                for (int i = 0; i < playerList.size(); i++) {
                                        //    12345678901234   12345678901234
                    System.out.printf("Player %2d.  %-14s" + "\t%-14s\n", i, playerList.get(i).getName(), players.get(i).getName());
                }
                System.out.println();
            }
            System.out.println();
        }
*/

        //Check player play order. GameController already prints it. Just wait the game ends.
        try {
            gameThread.join(60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}