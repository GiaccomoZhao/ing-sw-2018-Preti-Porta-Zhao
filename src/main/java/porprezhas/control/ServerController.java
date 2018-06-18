package porprezhas.control;

import porprezhas.Network.rmi.common.ServerRMIInterface;
import porprezhas.exceptions.diceMove.*;
import porprezhas.Network.command.*;
import porprezhas.model.Game;
import porprezhas.model.GameConstants;
import porprezhas.model.GameInterface;
import porprezhas.model.cards.ToolCardParam;
import porprezhas.model.database.DatabaseInterface;
import porprezhas.model.Player;
import porprezhas.Network.socket.SocketServerClientHandler;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerController extends UnicastRemoteObject implements ServerControllerInterface, ServerRMIInterface, Runnable, ActionHandler {

    private final int port;

    private ServerSocket serverSocket;

    private List<GameControllerInterface> gameControllerList;

	private DatabaseInterface databaseInterface;

	private GameControllerFactory gameControllerFactory;

	private volatile List<Player> playerBuffer;

	private List<Player> loggedPlayer;

    public HashMap getSocketUsers() {
        return socketUsers;
    }

    private HashMap socketUsers;

    private  Registry registry;

    public ServerController(int port) throws RemoteException {
        super(port);
		this.port=port;
        playerBuffer = new LinkedList<>();
		gameControllerList = new LinkedList<>();
		loggedPlayer = new ArrayList<>();
		socketUsers = new HashMap();
        registry= LocateRegistry.getRegistry();


	}


    private GameControllerInterface getGameControllerByPlayer (Player player)  {
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
	public synchronized void join(Player newPlayer) {
        System.out.println("A new player has joined.  ID = " + newPlayer.getPlayerID() + "\t Name = " + newPlayer.getName());
        GameControllerInterface gameController = getGameControllerByPlayer(newPlayer);
        if(null != gameController) {
            // player is already in Game
            // TODO: send a message to client:
            // client.player.setGameController(gameController);
            System.out.println("But he is already inside a running game. Putting new player in that game!");

        } else {
            playerBuffer.add(newPlayer);

            if (playerBuffer.size() == GameConstants.MAX_PLAYER_QUANTITY) {
                try {
                    createNewGame();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                GameControllerInterface actualGameController = getGameControllerByPlayer(newPlayer);
                Game gamet= (Game) actualGameController.getGame();
                for (Player readyPlayer:
                        gamet.getPlayerList()) {
                    if(socketUsers.containsKey(readyPlayer.getName())){

                        gamet.addObserver((ObjectOutputStream) socketUsers.get(readyPlayer.getName()));

                    }
                    else
                    try {
                        gamet.addObserver(readyPlayer.getName());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

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
        int playerQuantity = (playerBuffer.size() >= GameConstants.MAX_PLAYER_QUANTITY) ?
                GameConstants.MAX_PLAYER_QUANTITY :
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
    public Boolean insertedDice(long diceID, int rowBoard, int columnBoard, String username)
            throws RemoteException,
            IndexOutOfBoundsException, NotYourTurnException, AlreadyPickedException,
            BoardCellOccupiedException, EdgeRestrictionException, PatternColorRestrictionException, PatternNumericRestrictionException, AdjacentRestrictionException
    {
        String currentPlayerName = this.getGameControllerByUsername(username).getGame().getCurrentPlayer().getName();
        if(!username.equals(currentPlayerName)) {
            throw new NotYourTurnException(
                    "\n" +
                    "This is not your turn!\n" +
                    "Current player is: " + currentPlayerName);
        } else {
            if (this.getGameControllerByUsername(username).getGame().
                    insertDice(diceID, rowBoard, columnBoard))
                return true;
            else
                return false;
        }
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
    public Boolean usedToolCard(String username, int toolCardID, ArrayList<Integer> paramList) throws RemoteException {
        GameInterface playerGame= this.getGameControllerByUsername(username).getGame();
        if(username.equals(playerGame.getCurrentPlayer().getName())){
           ToolCardParam toolCardParam = new ToolCardParam(
                   playerGame.getRoundTrack(),
                    playerGame.getDraftPool(),
                    playerGame.getDiceBag(),
                    playerGame.getCurrentPlayer().getBoard(),
                    paramList
            );
           if( this.getGameControllerByUsername(username).useToolCard(toolCardID, toolCardParam))
               return true;
           else
               return false;
        }
        else
            return false;

    }



    private GameControllerInterface getGameControllerByUsername (String username)  {
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

    @Override
    public void run() {
        ExecutorService executor = Executors.newCachedThreadPool();

        try {
            serverSocket = new ServerSocket(port+1);
        } catch (IOException e) {
            System.err.println(e.getMessage()); // The port is not available
            return;
        }
        System.out.println("server socket ready");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                executor.submit(new
                        SocketServerClientHandler(socket, this));
            } catch(IOException e) {
                break; // entrerei qui se serverSocket venisse chiuso
            }
        }
        executor.shutdown();
    }

    @Override
    public synchronized Answer handle(LoginAction loginAction) {
        String username= loginAction.username;
        for (Player findPlayer:
                loggedPlayer) {
            if(findPlayer.getName().equals(username))
                return new LoginActionAnswer(false, username);
        }
        loggedPlayer.add(new Player(username));
        this.socketUsers.put(username, loginAction.getObjectOutputStream());

        return  new LoginActionAnswer(true, username);
    }

    @Override
    public synchronized Answer handle(JoinAction joinAction) {

        String username= joinAction.username;
        for (Player findPlayer:
                loggedPlayer) {
            if(findPlayer.getName().equals(username)) {
                this.join(findPlayer);
                return new JoinActionAnswer(true);

            }
        }
        return new JoinActionAnswer(false);
    }

    @Override
    public synchronized Answer handle(InsertDiceAction insertDiceAction) {

        String username= insertDiceAction.username;
        if(username.equals(this.getGameControllerByUsername(username).getGame().getCurrentPlayer().getName()))
            if(this.getGameControllerByUsername(username).getGame().insertDice(insertDiceAction.diceId, insertDiceAction.row, insertDiceAction.col))
                return new DiceInsertedAnswer(true);
        return new DiceInsertedAnswer(false);
    }

    @Override
    public synchronized Answer handle(ChoosePatternAction choosePatternAction) {
        return null;
    }

    @Override
    public synchronized Answer handle(UseToolCardAction useToolCardAction) {

        String username= useToolCardAction.username;
        GameInterface playerGame=this.getGameControllerByUsername(username).getGame();
        if(username.equals(playerGame.getCurrentPlayer().getName())){
            ToolCardParam toolCardParam = new ToolCardParam(
                    playerGame.getRoundTrack(),
                    playerGame.getDraftPool(),
                    playerGame.getDiceBag(),
                    playerGame.getCurrentPlayer().getBoard(),
                    useToolCardAction.paramList
            );
            try {
                if( this.getGameControllerByUsername(username).useToolCard(useToolCardAction.toolCardID, toolCardParam));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return new DiceInsertedAnswer(true);
    }

    @Override
    public synchronized Answer handle(PassAction passAction) {

        String username= passAction.username;
        if(username.equals(this.getGameControllerByUsername(username).getGame().getCurrentPlayer().getName()))
            this.getGameControllerByUsername(username).pass();
        else
            return new PassActionAnswer(false) ;
        return new PassActionAnswer(true);
    }
}
