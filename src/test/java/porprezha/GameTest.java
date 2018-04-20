package porprezha;

import porprezha.control.GameControllerInterface;
import porprezha.control.ServerController;
import porprezha.control.ServerControllerInterface;
import porprezha.model.Game;
import porprezha.model.Player;

public class GameTest {
    public static void main(String[] args) throws Exception {
        ServerControllerInterface server = new ServerController(0);
        server.join(new Player());
        GameControllerInterface game = server.createNewGame();
        game .start();

        game.playerPrepare();
        game.gamePrepare();
        for (int iRound = 0; iRound < Game.GameConstants.ROUND_NUM; iRound++) {
            game.playRound();
        }
        game.endGame();
    }
}
