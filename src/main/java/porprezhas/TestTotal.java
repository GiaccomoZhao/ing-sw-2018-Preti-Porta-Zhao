package porprezhas;

import com.github.javafaker.Faker;
import porprezhas.control.GameController;
import porprezhas.control.GameControllerInterface;
import porprezhas.control.ServerController;
import porprezhas.model.Game;
import porprezhas.model.Player;
import porprezhas.model.dices.Dice;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class TestTotal {
    public static void main(String[] args) throws RemoteException, InterruptedException {


        ServerController server ;
        GameControllerInterface newGameController;
        List<Player> players;
        Thread gameThread;

        // test parameter
        final int NUM_PLAYER = 4;


            server = new ServerController(1456);

        Registry registry= LocateRegistry.getRegistry();
        registry.rebind("serverController", server);
        Thread myThread = new Thread(server);
        myThread.start();
        players = new ArrayList<>();

        for (int i = 0; i < 0; i++) {
            Faker faker = new Faker();


            String firstName = faker.name().firstName();


            String playerName = firstName; //"P" + i;
            players.add(new Player(playerName));
            server.join(players.get(i));
        }
        while(server.getGameControllers().size()<=0)
            Thread.sleep(2000);

        newGameController = (GameController) server.getGameControllers().get(0);
        Game game = (Game) newGameController.getGame();

        (gameThread = new Thread((Runnable) newGameController)).start();

//        Thread.sleep((long) Game.GameConstants.secondsToMillis(
//                Game.GameConstants.TIMEOUT_PREPARING_SEC));  // this solves "main" java.lang.NullPointerException because thread of new game controller hasn't setup yet

        // Wait Game Controller be created
        while(newGameController.getState() == null)
            Thread.sleep(10);
        System.out.println("-- Server start to receive commands");  // NOTE: we simulate to receive command from client

        // Wait until Game ask for a choose to player
        while(!newGameController.getState().equals(GameControllerInterface.StateMachine.PLAYER_PREPARING))
            Thread.sleep(10);

        // simulate all player choose a pattern, We'll see Game skip ChooseTimeOut
        for (Player player : game.getPlayerList()) {
            int choose = 3;
            if( newGameController.choosePattern(player, choose) ) {
                System.out.printf("Player: %-12s\tpattern = %s\n", player.getName(), player.getBoard().getPattern().getNamePattern());
            } else {
                System.out.printf("Player: %-12s\tCan not choose %s!!!\n", player.getPatternsToChoose().get(choose).toString());
            }
        }

        // Wait Game finish the Setup
        while(!newGameController.getState().equals(GameControllerInterface.StateMachine.PLAYING) ) {
            Thread.sleep(10);
        }

        // Give Game Controller Actions, We'll see Game skip all round timeout
        /*while(newGameController.getState().isGameRunning()) {


           /* if (game.getCurrentPlayer().isDicePickable()) {
                boolean bPlaced = false;
                boolean bUsed = false;
                for (int x = 0; !bPlaced && x < game.getCurrentPlayer().getBoard().getHeight(); x++) {
                    for (int y = 0; !bPlaced && y < game.getCurrentPlayer().getBoard().getWidth(); y++) {
                        Dice dice = game.getDraftPool().diceList().get(0);
                        if (newGameController.insertDice(0, x, y)) {
                            System.out.printf("\t\tPlace dice %C%d at {%d,%d}\n\n", dice.getColorDice().name().charAt(0), dice.getDiceNumber(), x, y);
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
        gameThread.join(1000000);
        System.out.println("\n\n\n-- Server tear down.");
    }
}
