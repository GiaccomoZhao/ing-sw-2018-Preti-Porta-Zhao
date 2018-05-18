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


            server = new ServerController(0);

        players = new ArrayList<>();
        for (int i = 0; i < NUM_PLAYER; i++) {
            Faker faker = new Faker();


            String firstName = faker.name().firstName();


            String playerName = firstName; //"P" + i;
            players.add(new Player(playerName));
            server.join(players.get(i));
        }

        newGameController = (GameController) server.getGameControllers().get(0);
        Game game = (Game) newGameController.getGame();

        Registry registry = LocateRegistry.getRegistry();
        registry.rebind("game", game);

        (gameThread = new Thread((Runnable) newGameController)).start();

//        Thread.sleep((long) Game.GameConstants.secondsToMillis(
//                Game.GameConstants.TIMEOUT_PREPARING_SEC));  // this solves "main" java.lang.NullPointerException because thread of new game controller hasn't setup yet

        while(newGameController.getState() == null ||
                !newGameController.getState().equals(GameControllerInterface.StateMachine.PLAYING))
            Thread.sleep(100);
        System.out.println("Server start to receive commands");

        while(newGameController.getState().isGameRunning()) {
            Thread.sleep(10);
            for (int x = 0; x < game.getCurrentPlayer().getBoard().getHeight(); x++) {
                for (int y = 0; y < game.getCurrentPlayer().getBoard().getWidth(); y++) {
                    Dice dice = game.getDraftPool().diceList().get(0);
                    if( newGameController.insertDice(0, x, y) )
                        System.out.printf("\t\tPlace dice %C%d at {%d,%d}\n\n", dice.getColorDice().name().charAt(0), dice.getDiceNumber(), x, y);
                }
            }
            newGameController.pass();
        }
    }
}
