package porprezhas.control;

import porprezhas.model.Game;
import porprezhas.model.database.DatabaseInterface;
import porprezhas.model.Player;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

public class ServerController extends UnicastRemoteObject implements ServerControllerInterface {

	private List<GameControllerInterface> gameControllerInterfaceList;

	private DatabaseInterface databaseInterface;

	private GameControllerFactory gameControllerFactory;

	private volatile List<Player> playerBuffer;

	public ServerController(int port) throws RemoteException {
		super(port);
		playerBuffer = new LinkedList<>();
	}

	@Override
	public void join(Player newPlayer) {
		playerBuffer.add(newPlayer);
	}


	public GameControllerInterface createNewGame() {
	    if(playerBuffer.size() > 0) {
	        // create a new game with 1-4 players
            int playerQuantity = (playerBuffer.size() >= Game.GameConstants.MAX_PLAYER_QUANTITY) ?
                    Game.GameConstants.MAX_PLAYER_QUANTITY :
                    playerBuffer.size();
            // cut and past the Players to factory
            gameControllerInterfaceList.add(
                    gameControllerFactory.create(
                            playerBuffer.subList(0, playerQuantity - 1),
                            Game.SolitaireDifficulty.NORMAL));
            playerBuffer = playerBuffer.subList(playerQuantity, playerBuffer.size() - 1);
        }
        return gameControllerInterfaceList.get(gameControllerInterfaceList.size() - 1);
	}
}
