package PorPreZha.control;

import org.junit.Test;
import porprezha.control.GameControllerInterface;
import porprezha.control.ServerController;
import porprezha.control.ServerControllerInterface;
import porprezha.model.Game;
import porprezha.model.Player;


public class GameTest {
    @Test
    public static void main(String[] args) throws Exception {
        ServerControllerInterface server = new ServerController(0);
        server.join(new Player());
        GameControllerInterface game = server.createNewGame();
        game.start();
    }
}
