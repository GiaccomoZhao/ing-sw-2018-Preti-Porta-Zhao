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

import static porprezhas.model.GameConstants.*;


public class ServerController extends UnicastRemoteObject implements ServerControllerInterface, ServerRMIInterface, Runnable, ActionHandler {

    private final int port;

    private ServerSocket serverSocket;

    private List<GameControllerInterface> gameControllerList;

	private DatabaseInterface databaseInterface;

	private GameControllerFactory gameControllerFactory;

	private  List<Player> playerBuffer;

	private List<Player> loggedPlayer;

    public HashMap getSocketUsers() {
        return socketUsers;
    }

    private HashMap socketUsers;

    private  Registry registry;

    private HashMap inGameLostConnection;

    Timer queueTimeOut;

    public final static int ALREADY_IN_GAME = 1;
    public final static int USERNAME_AVAILABLE = 0;
    public final static int USERNAME_ALREADY_TAKEN = -1;


    public ServerController(int port) throws RemoteException {
        super(port);
		this.port=port;
        playerBuffer = new LinkedList<>();
		gameControllerList = new LinkedList<>();
		loggedPlayer = new ArrayList<>();
		socketUsers = new HashMap();
        registry= LocateRegistry.getRegistry();
        inGameLostConnection=new HashMap();


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
            // Create a timer to auto Start a new game
            if( 0 == playerBuffer.size() ) {
                queueTimeOut = new Timer();
                queueTimeOut.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            createNewGame();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }, secondsToMillis(TIMEOUT_QUEUE_SEC));
            }

            playerBuffer.add(newPlayer);

            if (playerBuffer.size() == GameConstants.MAX_PLAYER_QUANTITY) {
                // Create a game before timeout
                if(queueTimeOut != null) {
                    queueTimeOut.cancel();
                    queueTimeOut = null;
                }
                try {
                    createNewGame();
                } catch (RemoteException e) {
                    e.printStackTrace();
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

        //send to clients gameController
        Game game = (Game) gameController.getGame();
        for (Player readyPlayer:
                game.getPlayerList()) {
            if(socketUsers.containsKey(readyPlayer.getName())){

                game.addObserver(readyPlayer.getName(), (ObjectOutputStream) socketUsers.get(readyPlayer.getName()), this);

            }
            else
                try {
                    game.addObserver(readyPlayer.getName(), this);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

        }

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

        //send to clients gameController
        Game game = (Game) gameController.getGame();
        for (Player readyPlayer:
                game.getPlayerList()) {
            if(socketUsers.containsKey(readyPlayer.getName())){

                game.addObserver(readyPlayer.getName(), (ObjectOutputStream) socketUsers.get(readyPlayer.getName()), this);

            }
            else
                try {
                    game.addObserver(readyPlayer.getName(), this);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

        }

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


    //This method handles RMI join action.
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
    //This method handles RMI login action. It return 3 possible states:
    // 1) USERNAME_ALREADY_TAKEN
    // 2) ALREADY_IN_GAME if the username is frozen in a game and the game isn't finished yet
    // 3) USERNAME_AVAILABLE
    @Override
    public int login(String username) throws RemoteException {
        for (Player findPlayer:
             loggedPlayer) {
            if(findPlayer.getName().equals(username)){
                if(!this.inGameLostConnection.containsKey(username))
                    return USERNAME_ALREADY_TAKEN;
                else
                if (!((GameControllerInterface)this.inGameLostConnection.get(username)).getState().hasGameFinished())
                    return ALREADY_IN_GAME;
            }
        }
        loggedPlayer.add(new Player(username));


        return USERNAME_AVAILABLE;
    }



    //This method handles RMI logout action.
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
    public Boolean choosePattern(int indexPattern, String username) throws RemoteException {
        for (Player player :
                this.getGameControllerByUsername(username).getGame().getPlayerList()) {

            if(username.equals(player.getName())) {
                this.getGameControllerByUsername(username).choosePattern(player, indexPattern);
                return true;
            }
        }
            return false;
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

    // ******************************************************************************
    //
    // -------------------------------- SOCKET handler ------------------------------
    //
    //*******************************************************************************


    /*This method accepts new connections and pass those connections to SocketServerClientHandler
    *
    * @see SocketServerClientHandler
    *
    */
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
        Boolean ready=true;
        while (ready) {
            try {
                Socket socket = serverSocket.accept();
                executor.submit(new
                        SocketServerClientHandler(socket, this));
            } catch(IOException e) {
                break;
            }
        }
        executor.shutdown();
    }

    /* This method handles socket login action. It creates a new Answer with 3 possible state.
    *  It saves for every correct login the ObjectOutputStream of the user
    *
    * @param loginAction  a request of login from the socket client that asks the login
    *
    * @return the LoginActionanswer with 3 possible answers of Login request:
    *                                       1) USERNAME_ALREADY_TAKEN
    *                                       2) ALREADY_IN_GAME if the username is frozen in a game and the game isn't finished yet
    *                                       3) USERNAME_AVAILABLE
    */
    @Override
    public synchronized Answer handle(LoginAction loginAction) {
        String username= loginAction.username;
        for (Player findPlayer:
                loggedPlayer) {

            if(findPlayer.getName().equals(username)){

                if(!this.inGameLostConnection.containsKey(username))
                    return new LoginActionAnswer(USERNAME_ALREADY_TAKEN, username);
                else{
                     if (!((GameControllerInterface)this.inGameLostConnection.get(username)).getState().hasGameFinished()) {

                         this.socketUsers.put(username, loginAction.getObjectOutputStream());
                         return new LoginActionAnswer(ALREADY_IN_GAME, username);
                     }
                }
            }

        }
        loggedPlayer.add(new Player(username));
        this.socketUsers.put(username, loginAction.getObjectOutputStream());

        return  new LoginActionAnswer(USERNAME_AVAILABLE, username);
    }

    /* This method handles socket Join action.
     *  It calls join method if the user is logged.
     *
     * @param joinAction  a request of join from the socket client that asks the join
     * @return the JoinActionAnswer with the boolean answer of the operation
     *
     */
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

    /* This method handles socket InsertDiceAction action.
     *
     *
     * @param insertDiceAction  a request of insertDice from a socket client
     *
     * @return the DiceInsertedAnswer with the answer of the operation:
     *         True if the insert dice is operation was right.
     *         The exception with the details of the wrong move.
     *
     */
    @Override
    public synchronized Answer handle(InsertDiceAction insertDiceAction) {

        String username= insertDiceAction.username;
        if(username.equals(this.getGameControllerByUsername(username).getGame().getCurrentPlayer().getName()))
            try {
                if (this.getGameControllerByUsername(username).getGame().insertDice(insertDiceAction.diceId, insertDiceAction.row, insertDiceAction.col))
                    return new DiceInsertedAnswer(true);
            } catch (Exception e) {
                return new DiceInsertedAnswer(false, e);
        }
        return new DiceInsertedAnswer(false);
    }



    /* This method handles socket ChoosePattern action.
     *  It calls choosePattern of gameController
     *
     * @param choosePatternAction contains the id of the pattern choosen by the user
     * @return the PatternAnswer with the boolean answer of the operation
     *
     */
    @Override
    public synchronized Answer handle(ChoosePatternAction choosePatternAction) {
        String username= choosePatternAction.username;
        int indexPattern=choosePatternAction.choosenPattern;

        for (Player player :
                this.getGameControllerByUsername(username).getGame().getPlayerList()) {

            if(username.equals(player.getName())) {
                this.getGameControllerByUsername(username).choosePattern(player, indexPattern);
                return new PatternAnswer(true);
            }
        }
        return new PatternAnswer(false);
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


    /* This method handles socket Pass action.
     *  It calls pass of gameController
     *
     * @param PassAction a request of pass from a socket client
     * @return the answer of pass request
     *
     */
    @Override
    public synchronized Answer handle(PassAction passAction) {

        String username= passAction.username;
        if(username.equals(this.getGameControllerByUsername(username).getGame().getCurrentPlayer().getName()))
            this.getGameControllerByUsername(username).pass();
        else
            return new PassActionAnswer(false) ;
        return new PassActionAnswer(true);
    }


    // ******************************************************************************
    //
    // ----------------- Lost connection and resume game handler --------------------
    //
    //*******************************************************************************



    /* This method is called when an inGameUser close or lose his connection (both RMI and Socket).
    *  If the game isn't started yet, the player is removed from buffer list (UN-JOIN).
    *  If there is a current gameController related to username the method saves his username
    *  in inGameLostConnection and "freezes" the player in the game.
    *  Closedconnection calls also passUser() so if the player is the current player other players don't have to wait.
    *
    * @param username   The username of the player that closed the connection.
    */
    @Override
    public void closedConnection(String username) {

        //If the player isn't in game but he did join, he is removed from the buffer
        for (Player player:
                this.playerBuffer) {
            if (player.getName().equals(username)) {
                playerBuffer.remove(player);
                this.loggedPlayer.remove(player);
                return;
            }
        }

        //If the player was in game, he is freeze;
        System.out.println(username + " lost the connection");
        try {
            this.passUser(username);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        GameControllerInterface gameControllerInterface= getGameControllerByUsername(username);
        Game game= (Game) gameControllerInterface.getGame();

        //If the user was using socket, he is removed from the map(username - ObjectOutputStream)
        if(this.socketUsers.containsKey(username))
            socketUsers.remove(username);


        //Remove the player from the list of Observer
        game.removeObserver(username);

        game.freezePlayer(username);

        this.inGameLostConnection.put(username, gameControllerInterface);


    }

    /*resumeGame() method handles return in game for a user that lost his connection
    * and chooses to use RMI as new type of connection.
    * if the answer is true, this method re-adds to the list of observers this player.
    * The username is removed from the map of the player that lost the connection.
    *
    * @param username  The username of the player that wants to resume the game.
    * @return          The boolean answer of the attempt to reconnect.
    */
    @Override
    public Boolean resumeGame(String username) {
        Boolean answer=false;
        if (this.inGameLostConnection.containsKey(username)) {
            answer = ((GameControllerInterface)this.inGameLostConnection.get(username)).resumeGame(username);
            if (answer){
                GameInterface game =((GameControllerInterface)this.inGameLostConnection.get(username)).getGame();
                try {
                    ((Game)game).addObserver(username,  this);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
            this.inGameLostConnection.remove(username);
        }
        return answer;
    }

    /*resumeGame() method handles return in game for a user that lost his connection
     * and chooses to use SOCKET as new type of connection.
     * if the answer is true, this method re-adds to the list of observers this player.
     * The username is removed from the map of the player that lost the connection.
     *
     * @param username  The username of the player that wants to resume the game.
     * @return          The JoinActionanswer with the answer of the attempt to reconnect.
     */

    @Override
    public synchronized Answer handle(ResumeGameAction resumeGameAction) {
        String username= resumeGameAction.username;
        Boolean answer=false;

        if (this.inGameLostConnection.containsKey(username)) {
            answer = ((GameControllerInterface)this.inGameLostConnection.get(username)).resumeGame(username);
            if (answer){
                GameInterface game =((GameControllerInterface)this.inGameLostConnection.get(username)).getGame();
                ((Game)game).addObserver(username, (ObjectOutputStream) socketUsers.get(username), this);

            }
            this.inGameLostConnection.remove(username);
        }

        return new JoinActionAnswer(answer);
    }
}
