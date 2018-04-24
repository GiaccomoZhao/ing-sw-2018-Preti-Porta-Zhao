package porprezhas.control;

import porprezhas.model.Game;
import porprezhas.model.GameInterface;
import porprezhas.model.Player;

import java.util.List;

public class GameControllerFactory {

	public static GameControllerInterface create(List<Player> playerList, Game.SolitaireDifficulty difficulty) {
	    GameInterface game = new Game(playerList, difficulty);
		GameController gameController = new GameController(game);
		return gameController;
	}

}
