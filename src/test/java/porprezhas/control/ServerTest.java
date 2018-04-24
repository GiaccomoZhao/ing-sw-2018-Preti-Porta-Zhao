package porprezhas.control;

import org.junit.Test;
import porprezhas.model.Player;

// test of integrity
public class ServerTest {
    @Test
    public static void main(String[] args) throws Exception {
        ServerControllerInterface server = new ServerController(0);
        server.join(new Player());
        GameControllerInterface game = server.createNewGame();
        game.start();
    }
}