package porprezhas.control;

import porprezhas.Network.ProxyObserverRMI;
import porprezhas.RMI.MainViewInterface;
import porprezhas.model.Game;
import porprezhas.model.database.DatabaseInterface;
import porprezhas.model.Player;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.Logger;

public class ServerController extends UnicastRemoteObject implements ServerControllerInterface, ServerRMIInterface {

	private List<GameControllerInterface> gameControllerList;

	private DatabaseInterface databaseInterface;

	private GameControllerFactory gameControllerFactory;

	private volatile List<Player> playerBuffer;

	private List<Player> loggedPlayer;

    private HashMap viewClient;

    private  Registry registry;

    public ServerController(int port) throws RemoteException {
		super(port);
		playerBuffer = new LinkedList<>();
		gameControllerList = new LinkedList<>();
		loggedPlayer = new ArrayList<>();
		viewClient = new HashMap();
        registry= LocateRegistry.getRegistry();
	}


    private GameControllerInterface getGameControllerByPlayer (Player player) throws RemoteException {
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

                GameControllerInterface actualGameController = getGameControllerByPlayer(newPlayer);
                Game gamet= (Game) actualGameController.getGame();
                for (Player readyPlayer:
                        gamet.getPlayerList()) {
                        gamet.addObserver(readyPlayer.getName());

                }


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
        //send to clients gameController
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

    @Override
    public Boolean joinGame(String username) throws RemoteException {

        for (Player findPlayer:
                loggedPlayer) {
            if(findPlayer.getName().equals(username)) {
                this.join(findPlayer);
                return true;

            }
        }
        return false;
    }

    @Override
    public Boolean login(String username) throws RemoteException {
        for (Player findPlayer:
             loggedPlayer) {
            if(findPlayer.getName().equals(username))
                return false;
        }
        loggedPlayer.add(new Player(username));


        return true;
    }

    @Override
    public Boolean logout(String username) throws RemoteException {
        for (Player findPlayer:
                loggedPlayer) {
            if(findPlayer.getName().equals(username)){
                loggedPlayer.remove(findPlayer);
                return true;
            }

        }
        return false;
    }

    /*@Override
    public int getGameControllerIndex(String username) throws RemoteException {
        for (Player findPlayer:
                loggedPlayer) {
            if(findPlayer.getName().equals(username)){
                GameController  gameController=(GameController) this.getGameControllerByPlayer(findPlayer);
                return gameControllerList.indexOf(gameController);
            }

        }
        return -1;
    }*/
    @Override
    public Boolean insertedDice(int dicePosition, int xBoard, int yBoard, String username) throws RemoteException {
        if(username.equals(this.getGameControllerByUsername(username).getGame().getCurrentPlayer().getName()))
            if(this.getGameControllerByUsername(username).getGame().InsertDice(dicePosition, xBoard, yBoard))
                return true;
        return false;
        //TO_DO FIX

    }

    @Override
    public Boolean chooseDPattern(String namePattern) throws RemoteException {
        return null;
    }

    @Override
    public Boolean passUser(String username) throws RemoteException {
        if(username.equals(this.getGameControllerByUsername(username).getGame().getCurrentPlayer().getName()))
            this.getGameControllerByUsername(username).pass();
        else
            return false;
        return true;
    }

    @Override
    public Boolean usedToolCard() throws RemoteException {
        return null;
    }



    private GameControllerInterface getGameControllerByUsername (String username) throws RemoteException {
/*
*/
        for (GameControllerInterface gameController :
                gameControllerList) {
            List<Player> players = gameController.getGame().getPlayerList();
            for (Player p : players) {
                if(p.getName().equals(username)) {
                    return  gameController;
                }
            }
        }
        return null;    // throw game not found ?
    }

}
