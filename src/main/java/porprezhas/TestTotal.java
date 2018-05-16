package porprezhas;

import com.github.javafaker.Faker;
import porprezhas.control.GameController;
import porprezhas.control.GameControllerInterface;
import porprezhas.control.ServerController;
import porprezhas.model.Game;
import porprezhas.model.Player;

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

        gameThread.sleep(3000);

//        while(gameThread.isAlive()) {
//            newGameController.insertDice(0, 0, 3);
//            newGameController.pass();
//        }
    }
}
