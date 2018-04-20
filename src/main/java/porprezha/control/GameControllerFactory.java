package porprezha.control;

import porprezha.model.Game;
import porprezha.model.Player;

import java.util.List;

public class GameControllerFactory {

	public static GameControllerInterface create(List<Player> playerList, Game.SolitaireDifficulty difficulty) {
	    Game game = new Game(playerList, difficulty);
		GameController gameController = new GameController(game);
		return gameController;
	}

}
