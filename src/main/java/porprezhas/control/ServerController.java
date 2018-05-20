package porprezhas.control;

import porprezhas.model.Game;
import porprezhas.model.database.DatabaseInterface;
import porprezhas.model.Player;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class ServerController extends UnicastRemoteObject implements ServerControllerInterface {

	private List<GameControllerInterface> gameControllerList;

	private DatabaseInterface databaseInterface;

	private GameControllerFactory gameControllerFactory;

	private volatile List<Player> playerBuffer;

	public ServerController(int port) throws RemoteException {
		super(port);
		playerBuffer = new LinkedList<>();
		gameControllerList = new LinkedList<>();
	}


    private GameControllerInterface getGameControllerByPlayer (Player player) {
/*        for (Player p :
             playerBuffer) {
            if(p.equals(player))
                return null;
        }
*/
        for (GameControllerInterface gameController :
                gameControllerList) {
            List<Player> players = gameController.getGame().getPlayerList();
            for (Player p : players) {
                if(p.getPlayerID().equals(player.getPlayerID())) {
                    return  gameController;
                }
            }
        }
        return null;    // throw game not found ?
    }

    public List<GameControllerInterface> getGameControllers() {
        return gameControllerList;
    }


	@Override
	public synchronized void join(Player newPlayer) throws RemoteException {
        System.out.println("A new player has joined.  ID = " + newPlayer.getPlayerID() + "\t Name = " + newPlayer.getName());
        GameControllerInterface gameController = getGameControllerByPlayer(newPlayer);
        if(null != gameController) {
            // player is already in Game
            // TODO: send a message to client:
            // client.player.setGameController(gameController);
            System.out.println("But he is already inside a running game. Putting new player in that game!");

        } else {
            playerBuffer.add(newPlayer);

            if (playerBuffer.size() == Game.GameConstants.MAX_PLAYER_QUANTITY) {
                createNewGame();
            }
        }
	}

    @Override
    public synchronized void leave(Player player) throws RemoteException {
        playerBuffer.remove(player);
    }

    // TODO: a test
	// create a new single game
	public GameControllerInterface createNewGame(Player player, Game.SolitaireDifficulty difficulty) throws RemoteException {
	    GameControllerInterface gameController; // save return value

        System.out.println("Creating a single game from player " + player.getName() + ".");

        // cut and past the Players to factory
        gameController =
                new GameController(
                        new Game(player, difficulty));
        gameControllerList.add( gameController );
        // NOTE: make player leave from the queue before creating single game
        playerBuffer.remove(player);    // if player has joined but want play single game
        // TODO: send a message to client:  like token
        // for(Player player : subBuffer) player.getClient().player.setGameController(gameController);
        return gameController;
	}


	// create a new multi-player game
	public GameControllerInterface createNewGame() throws RemoteException {
	    GameControllerInterface gameController; // to save return value
	    // before start: check all player on line
        // create a new game with 1-4 players
        int playerQuantity = (playerBuffer.size() >= Game.GameConstants.MAX_PLAYER_QUANTITY) ?
                Game.GameConstants.MAX_PLAYER_QUANTITY :
                playerBuffer.size();
        List<Player> subBuffer = new ArrayList<>(playerBuffer.subList(0, playerQuantity));

        System.out.println("Creating a new game of " + playerQuantity + " players");

        // cut and past the Players to factory
        gameController =
                new GameController(
                        new Game(subBuffer));
/*                gameControllerFactory.create(
                        subBuffer,
                        Game.SolitaireDifficulty.NORMAL);
*/
        gameControllerList.add( gameController );
        playerBuffer.removeAll(subBuffer);
        // TODO: send a message to client:  like token
        // for(Player player : subBuffer) player.getClient().player.setGameController(gameController);
        return gameController;
	}


	@Override
	public boolean isAlreadyInGame(Player player) throws RemoteException {
	    GameControllerInterface gameController = getGameControllerByPlayer(player);
	    if(null != gameController)
            return true;
	    else
    	    return false;   // throw game not found ?
	}

    @Override
    public GameControllerInterface getGameController(Player player) throws RemoteException {
	    // A reverted counter duo to the players usually call this when a new game has just started
        for (int i = gameControllerList.size() - 1; i >= 0; i--) {
            List<Player> playerList = new ArrayList<>(gameControllerList.get(i).getGame().getPlayerList());
            for (Player p : playerList) {
                if(p.equals(player)) {
                    return  gameControllerList.get(i);
                }
            }
        }
        return null;    // NOTE: throw game not found ?
    }


}
