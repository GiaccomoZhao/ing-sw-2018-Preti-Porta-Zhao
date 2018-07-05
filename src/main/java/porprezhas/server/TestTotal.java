package porprezhas.server;

import com.github.javafaker.Faker;
import porprezhas.control.GameController;
import porprezhas.control.GameControllerInterface;
import porprezhas.control.ServerController;
import porprezhas.model.Game;
import porprezhas.model.GameConstants;
import porprezhas.model.Player;
import porprezhas.model.dices.Dice;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;


/**
 * instruction:
 * 1. in terminal type:
 *      start rmiregistry -J-Djava.rmi.server.useCodebaseOnly=false
 * 2. run NanoHTTP
 * 3. run this -SERVER-
 * 4. run clients
 */
public class TestTotal {
    private static final int port = 58090;

    public static void main(String[] args) throws RemoteException, InterruptedException {

        if(args.length > 0)
            GameConstants.setTimeoutQueueSec( Double.parseDouble(args[0]) );
        if(args.length > 1) {
            GameConstants.setTimeoutRoundSec(Double.parseDouble(args[1]));
            GameConstants.setTimeoutRoundSolitaireSec(Double.parseDouble(args[1]));
        }


        ServerController server;
        GameControllerInterface newGameController;
        Thread gameThread;
        boolean bTearDown = false;   // this variable should be changed to true when the server has to be shut down

        // test parameter
        boolean bSimulation = false; // set to true for simulation
        List<Player> players;
        final int NUM_PLAYER = 0;          // set to >0 for simulation



      //  Registry registry = LocateRegistry.getRegistry();
        Registry registry= LocateRegistry.createRegistry(port-1);
        server = new ServerController(port);
        registry.rebind("serverController", server);
        Thread serverThread = new Thread(server);
        serverThread.start();


        while (!bTearDown) {

            if(!bSimulation) {
                serverThread.join();
                bTearDown = true;  // shut down server when the thread has terminated

            } else {
                // fake players for simulation
                players = new ArrayList<>();
                for (int i = 0; i < NUM_PLAYER; i++) {
                    Faker faker = new Faker();

                    String firstName = faker.name().firstName();


                    String playerName = firstName; //"P" + i;
                    players.add(new Player(playerName));
                    server.join(players.get(i));
                }

                while (server.getGameControllers().size() <= 0)
                    Thread.sleep(1000);

                newGameController = (GameController) server.getGameControllers().get(0);
                Game game = (Game) newGameController.getGame();

//        Thread.sleep((long) Game.GameConstants.secondsToMillis(
//                Game.GameConstants.TIMEOUT_PREPARING_SEC));  // this solves "main" java.lang.NullPointerException because thread of new game controller hasn't setup yet

                // Wait Game Controller be created
                while (newGameController.getState() == null)
                    Thread.sleep(10);
                System.out.println("-- server start to receive commands");  // NOTE: we simulate to receive command from client

                // Wait until Game ask for a choose to player
                while (!newGameController.getState().equals(GameControllerInterface.StateMachine.PLAYER_PREPARING))
                    Thread.sleep(30);

                // simulate all player choose a pattern, We'll see Game skip ChooseTimeOut
        /*for (Player player : game.getPlayerList()) {
            int choose = 3;
//            if(player.getName().toUpperCase().contains("ZX"))   // test use
//                choose = -666;
            if( newGameController.choosePattern(player, choose) ) {
                System.out.printf("Player: %-12s\tpattern = %s\n", player.getName(), player.getBoard().getPattern().getNamePattern());
            } else {
                System.out.printf("Player: %-12s\tCan not choose %s!!!\n", player.getPatternsToChoose().get(choose).toString());
            }
        }*/

                // Wait Game finish the Setup
                while (!newGameController.getState().equals(GameControllerInterface.StateMachine.PLAYING)) {
                    Thread.sleep(30);
                }

                // Give Game Controller Actions, We'll see Game skip all round timeout
        /*while(newGameController.getState().isGameRunning()) {


           /* if (game.getCurrentPlayer().hasPickedDice()) {
                boolean bPlaced = false;
                boolean bUsed = false;
                for (int x = 0; !bPlaced && x < game.getCurrentPlayer().getBoard().getHeight(); x++) {
                    for (int y = 0; !bPlaced && y < game.getCurrentPlayer().getBoard().getWidth(); y++) {
                        Dice dice = game.getDraftPool().diceList().get(0);
                        if (newGameController.insertDice(0, x, y)) {
                            System.out.printf("\t\tPlace dice %C%d at {%d,%d}\n\n", dice.getDiceColor().name().charAt(0), dice.getDiceNumber(), x, y);
                            bPlaced = true;
                        }
                    }
                }
                if (!bPlaced && !bUsed)
                    System.out.println("\t\tno Action\n");
            }

            Thread.sleep(1000);
            newGameController.pass();
        }*/
                server.getGameThreads().get(0).join(1000000);
                bTearDown = true;  // shut down server when the simulation has finished
            }
        }
        System.out.println("\n\n\n-- server tear down.");
    }
}
