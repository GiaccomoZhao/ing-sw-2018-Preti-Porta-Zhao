package porprezhas.model;

import porprezhas.RMI.Server.ModelObservable;
import porprezhas.Useful;
import porprezhas.control.GameController;
import porprezhas.exceptions.diceMove.*;
import porprezhas.model.dices.*;
import porprezhas.model.dices.RoundTrack;

import porprezhas.model.cards.*;

import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Logger;

import static porprezhas.model.Game.NotifyState.BOARD_CREATED;
import static porprezhas.model.Game.NotifyState.DICE_INSERTED;

public class Game extends ModelObservable implements GameInterface {
    static Logger logger = Logger.getLogger(GameController.class.getName());

    public DiceBag getDiceBag() {
        return diceBag;
    }

    public DraftPool getDraftPool() {
        return draftPool;
    }


    public enum SolitaireDifficulty {
        BEGINNER, EASY, NORMAL, HARD, EXTREME;              // we can use .toString() method to get the text

        private static final int size = values().length;    // this attribute is used to reduce work of methods

        // use values().length guaranty it is always correct
        // constant size is wrote at minuscule because it's not a compile-time constant and it is private attribute
        public static int getSize() {
            return size;
        }

        // convert difficulty to number of Tool Card to give at Solitaire player
        // EXTREME -> 1  and  BEGINNER -> 5
        public int toToolCardsQuantity() {
            return size - this.ordinal();
        }
    }

    public enum NotifyState{NEW_TURN, CHOOSE_PATTERN, GAME_STARTED, NEXT_ROUND , DICE_INSERTED, BOARD_CREATED}

    // *********************************
    // --- Declaration of Attributes ---

    private final Long gameID;
    private RoundTrack roundTrack;
    private DiceBag diceBag;
    private DraftPool draftPool;

    private NotifyState gameNotifyState;

    private PrivateObjectiveCardFactory privateObjectiveCardFactory;
    private PublicObjectiveCardFactory publicObjectiveCardFactory;
    private ToolCardFactory toolCardFactory;
    private List<Card> publicObjectiveCardList;
    private List<Card> toolCardList;

    // player list management attributes
    private List<Player> playerList;
    private Player currentPlayer;
    private int iCurrentPlayer;     // keep currentPlayer always == playerList.get(iCurrentPlayer)
    private int iFirstPlayer;       // index of first player in current round -- or index of next round when this round has already finished
    private int iLastPlayer;        // index of player that play 2 consecutive times, on this player we'll turn the rotation direction, toggling bCounterClockwise
    private boolean bCountClockwise;

    private boolean bSolitaire;
    private SolitaireDifficulty solitaireDifficulty;    // in Multi-player we have Player.Pattern.Difficulty
    private int diceQuantity;



    // *********************************
    // ------ Basic Class Methods ------

    public Game(Player player, SolitaireDifficulty difficulty)  throws RemoteException {

        gameID = new Random().nextLong();   // transfer this to super()
        bSolitaire = true;
        solitaireDifficulty = difficulty;
        playerList = new ArrayList<>();    // TODO: generalization
        playerList.add(player);
        privateObjectiveCardFactory = new PrivateObjectiveCardFactory(1);
        publicObjectiveCardFactory = new PublicObjectiveCardFactory(1);
        toolCardFactory = new ToolCardFactory(difficulty);
        reset();
    }

    public Game(List<Player> playerList) throws RemoteException  {

        gameID = new Random().nextLong();   // create a unique string or number
        bSolitaire = false;
        this.playerList = new ArrayList<>(playerList);
        privateObjectiveCardFactory = new PrivateObjectiveCardFactory(playerList.size());
        publicObjectiveCardFactory = new PublicObjectiveCardFactory(playerList.size());
        toolCardFactory = new ToolCardFactory(playerList.size());
        reset();
    }

    private void reset() {  // NOTE: will be super();
        roundTrack = new RoundTrack();
        diceBag = new DiceBag();
//        draftPool = new DraftPool(diceBag, playerList.size());    // this shouldn't extract dice from the bag here
        draftPool = new DraftPool();    // this will be created by controller before every round
        diceQuantity = GameConstants.DICE_QUANTITY;

        resetPlayerIndexes();
        setCurrentPlayerByIndex();    // can be commented because gameController always calls Game.OrderPlayers() that calls this method
    }

    /* @requires playerList.size() > 0
     * @ensure (* indexes have be set correctly as at starting *)
     */
    void resetPlayerIndexes() {
        iCurrentPlayer = 0;
        iFirstPlayer = 0;
        iLastPlayer = playerList.size() - 1;
        bCountClockwise = false;
    }

    public boolean isSolitaire() {
        return bSolitaire;
    }

    public SolitaireDifficulty getSolitaireDifficulty() {
        return solitaireDifficulty;
    }

    public int getRoundIndex() {
        return roundTrack.getActualRound();
    }

    public int getDiceQuantity() {
        return diceQuantity;
    }

    public RoundTrack getRoundTrack() {
        return roundTrack;
    }

    public int getIndexCurrentPlayer() throws RemoteException{
        return iCurrentPlayer;
    }

    @Override
    public DraftPool getDraftpoolRmi() throws RemoteException {
        return this.draftPool;
    }

    public Player getFirstPlayer() throws RemoteException{
        return this.playerList.get(iFirstPlayer);
    }


    public NotifyState getGameNotifyState() throws RemoteException {
        return gameNotifyState;
    }

    public NotifyState getGameState()  {
        return gameNotifyState;
    }


    public List<Card> getPublicObjectiveCardList() {
        return publicObjectiveCardList;
    }

    public List<Card> getToolCardList() {
        return toolCardList;
    }

    public long getRoundTimeOut() {
        return GameConstants.secondsToMillis(
                isSolitaire() ?
                        GameConstants.TIMEOUT_ROUND_SOLITAIRE_SEC :
                        GameConstants.TIMEOUT_ROUND_SEC);
    }

    // *********************************
    // ----- Player list management ----    //NOTE: should i put this in a new class file?

    // return a new object that has reference to same elements
    public List<Player> getPlayerList() {
        return new ArrayList<Player>(playerList);
    }

    public int getIndexFirstPlayer() {
        return iFirstPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    // these are unique 2 method that modify currentPlayer directly
    // and they are private methods
    private void setCurrentPlayerByIndex() {
        this.currentPlayer = playerList.get(iCurrentPlayer);
    }

//    private void setCurrentPlayer(Player currentPlayer) {
//        this.currentPlayer = currentPlayer;
//    }

    /* requires (playerList.size() > 0) &&
     * requires (0 <= iFirstPlayer < playerList.size) &&
     * requires (0 <= iLastPlayer < playerList.size) &&
     * requires (* counting from iFirstPlayer to iLastPlayer has playerList.size() 's elements *)
     * ensure (* this is UNIQUE public method that MODIFies currentPlayer attribute *) &&
     * ensure (* indexes are correctly set as the request said above *) &&
     * ensure (* iCurrentPlayer is the nextPlayer's index (see code comment below) *)
     * ensure (0 <= iCurrentPlayer < playerList.size) &&
     * ensure currentPlayer = playerList(iCurrentPlayer)
     */
    public Player rotatePlayer() {
        // case anti clock
        if (bCountClockwise) {
            if (iCurrentPlayer == iFirstPlayer) {
                // when first player has already played for second time
                // first player became last player in next round
                // and the player after old first player as new first player
                bCountClockwise = false;    // turn back in clockwise
                nextPlayer();       // change index of current player
                iLastPlayer = iFirstPlayer;
                iFirstPlayer = iCurrentPlayer;
            } else {
                nextPlayer();
            }
        } else {
            // case clock wise
            if (iCurrentPlayer == iLastPlayer) {
                // arrived at last player he'll play 2 time consecutively
                // so we do not call nextPlayer and just change rotation direction (clockwise)
                bCountClockwise = true;
            } else {
                nextPlayer();
            }
        }
        gameNotifyState = NotifyState.NEW_TURN;
        setChanged();

        notifyObservers(new SerializableGame(this));

//        logger.info("It is turn of player n." + iCurrentPlayer);
        return currentPlayer;   // nextPlayer() method changes this attribute
    }

    public void newTurn() {
        currentPlayer.resetPickableDice();
        currentPlayer.setUsedToolCard(false);
    }


    /* requires (playerList.size() > 0)
     * ensure (0 <= iCurrentPlayer < playerList.size) &&
     * ensure (currentPlayer = playerList(iCurrentPlayer)&&
     * ensure (* index of current player referee to the next one correctly *)
     */
    // a simply increasing/decreasing rotation algorithm
    private Player nextPlayer() {
        if (bCountClockwise) {
            // case anti clock: index decreases
            if (iCurrentPlayer == 0) {
                iCurrentPlayer = playerList.size() - 1;
            } else {
                iCurrentPlayer--;
            }
        } else {
            // case clock wise: index increases
            if (iCurrentPlayer == playerList.size() - 1) {
                iCurrentPlayer = 0;
            } else
                iCurrentPlayer++;
        }
        setCurrentPlayerByIndex(); // change currentPlayer

        return currentPlayer;
    }


    // TODO: this block of code should be transferred in serverController
    /* @ensures playerList.contains(player)
       @signals (GamePlayerFullException e) \old(playerList).size >= max
       @signals (InvalidPlayerException e) param == null
       @signals (PlayerAlreadyPresentException e) (*unique Player already present in game*)
       @ *//*

    public void addPlayer(Player newPlayer) throws GamePlayerFullException, InvalidPlayerException, PlayerAlreadyPresentException {
        if (playerList.size() >= GameConstants.MAX_PLAYER_QUANTITY)
            throw new GamePlayerFullException();
        for (Player p : playerList) {
            if (newPlayer.getPlayerID().equals(p.getPlayerID()))
                throw new PlayerAlreadyPresentException();
        }
        playerList.add(newPlayer);
//        bSolitaire = playerList.size() == 1 ? true : false;
    }


    // this method is called for leaving a game before it starts
    // we set Player.bDC to check a player leave during the game
    public void removePlayer(Player player) {
        playerList.remove(player);
//        bSolitaire = playerList.size() == 1 ? true : false;
    }
*/


    /* @requires playerList.size > 0
       @ensure (*a random order*) ||
               (*Order players based on the last time the player has been to the TEACHER's DESK*)
       @ */

    public synchronized void orderPlayers() {
        if (playerList.size() <= 1) {
            ;   // do nothing
        } else {
            Collections.shuffle(playerList);
        }
        setCurrentPlayerByIndex();

        System.out.println("\nPlayer list: ");
        for (int i = 0; i < playerList.size(); i++) {
            Player player = playerList.get(i);
            player.setPosition(i);
            System.out.printf("\t n.%-2d\t%s\n", player.getPosition() + 1, player.getName());
        }
        System.out.println();
    }


    // **********************************
    // ---------- Game Control ----------

    /* @requires playerList.size() > 0
       @ensure (*in Solitaire*) &&
       @       (*Player choose a difficulty 1 to 5*) &&
       @       (*the Player receives 2 Private Objective Cards*) &&
       @       (*2 random PatternCard to choose one from 4 faces*) &&
       @       (*FavorTokens == 0*) ||
       @ensure (*in Multi-player game*) &&
       @       (*every Player receives: 2 random PatternCard to choose one from 4 faces*) &&
       @       (*a random hidden Private Objective Card*) &&
       @       (*a quantity of Favor Tokens equals to difficulty of Pattern Card*)
       @ */
    public void playerPrePrepare() {
        if (this.isSolitaire()) {
            Player player = playerList.get(0);
//            player.setFavorToken(0);
            player.setPrivateObjectCardList(
                    privateObjectiveCardFactory.createCard() );
        } else {

            // give a random private card
            for (int i = 0; i < playerList.size(); i++) {
                Player player = playerList.get(i);
                player.setPrivateObjectCardList(
                        privateObjectiveCardFactory.createCard() );
            }

            // give 2 random PatternCard to choose one from 4 faces
            List<Pattern.TypePattern> patternTypes = Arrays.asList(Pattern.TypePattern.values())
                    .subList(1, Pattern.TypePattern.values().length);   // skip test-use void pattern
            Collections.shuffle(patternTypes);
            ArrayList<Pattern.TypePattern> list;
            for (int i = 0; i < playerList.size(); i++) {
                list = new ArrayList<Pattern.TypePattern>(patternTypes.subList(4 * i, 4 * (i + 1)));
                playerList.get(i).
                        setPatternsToChoose(list);
            }

            // notify clients, now they should choose a pattern
            gameNotifyState = NotifyState.CHOOSE_PATTERN;
            setChanged();

            notifyObservers(new SerializableGame(this));
            // the caller will wait player choose() for a certain time
        }
    }

    public void playerPostPrepare() {
        if (!this.isSolitaire()) {
            System.out.println();
            for (Player player : playerList) {
                Board board = player.getBoard();
                // set default(first) pattern value when not chosen
                if (board == null) {    // this control is done by setPattern() too, but i put this here to emphasize that the pattern can be chosen only one time
                    this.setPattern(player, 0);
                }
                // Give a quantity of favorTokens based on the difficulty of patternCard
                Pattern pattern = player.getBoard().getPattern();
                player.setFavorTokenByDifficulty( pattern.getDifficulty() );
            }
            System.out.println();
        }
    }

    public void gamePrepare() {
        toolCardList = toolCardFactory.createCard();
        publicObjectiveCardList = publicObjectiveCardFactory.createCard();
        this.orderPlayers();

        // notify all players that the Game is ready to play
        gameNotifyState= NotifyState.GAME_STARTED;
        setChanged();

        notifyObservers(new SerializableGame(this));
    }

    public void nextRound() {
        if(null != draftPool)
            roundTrack.addDice(draftPool);
        getDraftPool().setDraftPool(getDiceBag(), playerList.size());

        gameNotifyState = NotifyState.NEXT_ROUND;
        setChanged();

        notifyObservers(new SerializableGame(this));
    }

    public int calcScore(Player player) {
        int scorePublic = 0;
        int scorePrivate = 0;
        Board board = player.getBoard();

        // sum of private objectives
        List<Card> privateObjectiveCardList = player.getPrivateObjectiveCardList();
        if (null != privateObjectiveCardList) {
            for (int i = 0; i < privateObjectiveCardList.size(); i++) {
                PrivateObjectiveCard privateObjectiveCard = (PrivateObjectiveCard) privateObjectiveCardList.get(i);

                scorePrivate += privateObjectiveCard.apply(board);
            }
        }
//        System.out.println(player.getName() + ": sum of private objectives = " + scorePrivate);
//        logger.info("" + Player.getName() + "\tSum of private objectives = " + scorePrivate);

        // sum of public objectives
        List<Card> publicObjectiveCardList = this.getPublicObjectiveCardList();
        if (null != publicObjectiveCardList) {
            for (int i = 0; i < publicObjectiveCardList.size(); i++) {
                PublicObjectiveCard publicObjectiveCard = (PublicObjectiveCard) publicObjectiveCardList.get(i);

                scorePublic += publicObjectiveCard.apply(board);
            }
        }
//        System.out.println(this + ": sum of public objectives = " + scorePublic);
//        logger.info("" + Player.getName() + "\tSum of public objectives = " + scorePublic);

        return scorePrivate + scorePublic;
    }


    // **********************************
    // ---------- Game Action -----------

//    public int calcScore(Player player) {


    public synchronized boolean insertDice(long diceID, int row, int col)
            throws IndexOutOfBoundsException, AlreadyPickedException,
            BoardCellOccupiedException, EdgeRestrictionException, PatternColorRestrictionException, PatternNumericRestrictionException, AdjacentRestrictionException
    {
        int indexDice = draftPool.getDiceIndexByID(diceID);
        if (!Useful.isValueBetweenInclusive(indexDice, 0, draftPool.diceList().size() -1))
            throw new IndexOutOfBoundsException("\nIndex in draft pool should be: 0 <= " + indexDice + " <= " + (draftPool.diceList().size() -1));

        Dice dice = draftPool.diceList().get(indexDice);

        if (!currentPlayer.isDicePickable()) {  // check that there is only one insert at turn
            throw new AlreadyPickedException(
                    "You have already Placed a Dice!\n" +
                    "Use Tool Card or Pass your turn please!"
            );
        } else {
            if (currentPlayer.getBoard().validMove(dice, row, col)) {

                dice = draftPool.chooseDice(diceID);
                if(null != dice) {
                    currentPlayer.placeDice(dice, row, col);

                    gameNotifyState = DICE_INSERTED;
                    setChanged();

                    notifyObservers(new SerializableGame(this));
                    return true;
                }
            }
        }
        return false;   // not valid
    }

    public boolean setPattern(Player p, int indexPatternType) {
        if (p.getBoard() == null) {    // A board is always associate with a pattern, if pattern hasn't be chosen it should be nul (not yet created)
            boolean bChosen = p.choosePatternCard(indexPatternType);   // here we create a new board associating it to the passed pattern
            if(bChosen) {
                gameNotifyState = BOARD_CREATED;
                setChanged();

                notifyObservers(new SerializableGame(this));
                return bChosen;
            }
        }
        return false;
    }

    @Override
    public Player getActualPlayer() throws RemoteException {
        return currentPlayer;
    }

    @Override
    public ArrayList<Player> getPlayers() throws RemoteException {
        return new ArrayList<Player>(playerList);
    }
}
