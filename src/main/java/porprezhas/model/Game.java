package porprezhas.model;

import porprezhas.Network.rmi.common.RemoteObserver;
import porprezhas.Network.rmi.server.ModelObservable;
import porprezhas.Useful;
import porprezhas.control.GameController;
import porprezhas.exceptions.GameAbnormalException;
import porprezhas.exceptions.diceMove.*;
import porprezhas.exceptions.toolCard.AlreadyUsedExceptions;
import porprezhas.exceptions.toolCard.NotEnoughTokenException;
import porprezhas.model.dices.*;
import porprezhas.model.dices.RoundTrack;

import porprezhas.model.cards.*;

import java.rmi.ConnectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import static porprezhas.model.Game.NotifyState.*;
import static porprezhas.model.GameConstants.BOARD_BOXES;

public class Game extends ModelObservable implements GameInterface {
   // static Logger logger = Logger.getLogger(GameController.class.getName());

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

    public enum NotifyState{NEW_TURN, CHOOSE_PATTERN, GAME_STARTED, NEXT_ROUND , DICE_INSERTED, BOARD_CREATED, PLAYER_QUIT, PLAYER_BACK , RANKING, TOOL_CARD, ALT_GAME, CHOOSE_PRIVATE}

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
    private CopyOnWriteArrayList<Player> frozenPlayer; // list of the players that had lost the connection
    private int iCurrentPlayer;     // keep currentPlayer always == playerList.get(iCurrentPlayer)
    private int iFirstPlayer;       // index of first player in current round -- or index of next round when this round has already finished
    private int iLastPlayer;        // index of player that play 2 consecutive times, on this player we'll turn the rotation direction, toggling bCounterClockwise
    private boolean bCountClockwise;
    private HashMap ranking;
    private Player winner;
    private List<Player> lastRoundPlayers;

    private boolean bSolitaire;
    private SolitaireDifficulty solitaireDifficulty;    // in Multi-player we have Player.Pattern.Difficulty
    private int diceQuantity;



    // *********************************
    // ------ Basic Class Methods ------

    public Game(Player player, SolitaireDifficulty difficulty)  throws RemoteException {

        gameID = new Random().nextLong();   // transfer this to super()
        bSolitaire = true;
        solitaireDifficulty = difficulty;
        playerList = new ArrayList<>();
        frozenPlayer= new CopyOnWriteArrayList();
        playerList.add(player);
        privateObjectiveCardFactory = new PrivateObjectiveCardFactory(1);
        publicObjectiveCardFactory = new PublicObjectiveCardFactory(1);
        toolCardFactory = new ToolCardFactory(difficulty);
        ranking=new HashMap();
        lastRoundPlayers=new ArrayList<>();
        reset();
    }

    public Game(List<Player> playerList) throws RemoteException  {

        gameID = new Random().nextLong();   // create a unique string or number
        bSolitaire = false;
        this.playerList = new ArrayList<>(playerList);
        frozenPlayer= new CopyOnWriteArrayList();
        privateObjectiveCardFactory = new PrivateObjectiveCardFactory(playerList.size());
        publicObjectiveCardFactory = new PublicObjectiveCardFactory(playerList.size());
        toolCardFactory = new ToolCardFactory(playerList.size());
        ranking=new HashMap();
        lastRoundPlayers=new ArrayList<>();
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



    public Player getFirstPlayer() throws RemoteException{
        return this.playerList.get(iFirstPlayer);
    }


    public List<Player> getFrozenPlayer() {
        return frozenPlayer;
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

    public HashMap getRanking() {
        return ranking;
    }

    public Player getWinner() {
        return winner;
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
        if (this.getRoundTrack().getActualRound()==10 && lastRoundPlayers.size()<playerList.size())
            lastRoundPlayers.add(playerList.get(iCurrentPlayer));
        // case anti clock
        // second turn of round
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
            // first turn of round
            if (iCurrentPlayer == iLastPlayer) {
                // arrived at last player he'll play 2 time consecutively
                // so we do not call nextPlayer and just change rotation direction (clockwise)
                bCountClockwise = true;
            } else {
                nextPlayer();
            }
        }
//        logger.info("It is turn of player n." + iCurrentPlayer);
        return currentPlayer;   // nextPlayer() method changes this attribute
    }

    public void newTurn() {
        currentPlayer.resetPickableDice();
        currentPlayer.setUsedToolCard(false);
        this.resetCard();

        gameNotifyState = NotifyState.NEW_TURN;
        setChanged();

        notifyObservers(new SerializableGame(this));
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


    // TODO: this block of code should be transferred in serverController -- check that server controller has this
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
                        privateObjectiveCardFactory.createCard());
            }
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

    public void playerPostPrepare() {
        if (/*!this.isSolitaire()*/ true) {
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

            // Notify Clients that Board are initialized
            gameNotifyState = BOARD_CREATED;
            setChanged();

            notifyObservers(new SerializableGame(this));
        }
       /* Board board = playerList.get(0).getBoard();
        if (board == null) {    // this control is done by setPattern() too, but i put this here to emphasize that the pattern can be chosen only one time
            this.setPattern(playerList.get(0), 0);
        }
        // Notify Clients that Board are initialized
        gameNotifyState = BOARD_CREATED;
        setChanged();

        notifyObservers(new SerializableGame(this));*/
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

    public void newRound(int indexRound) {
        boolean bFirstRound = 0 == indexRound;
        if(!bFirstRound)
            roundTrack.addDice(draftPool);
        draftPool.setDraftPool(getDiceBag(), playerList.size());

        gameNotifyState = NotifyState.NEXT_ROUND;
        setChanged();

        notifyObservers(new SerializableGame(this));
    }

    public int calcScore(Player player) {
        int scorePublic = 0;
        int scorePrivate = 0;
        int holesNumber;
        int nFavorToken;
        int finalscore;
        Board board = player.getBoard();


        //Number of free spaces on the board
        holesNumber= BOARD_BOXES - board.getDiceQuantity();

        //Number of remaining favor token
        nFavorToken = player.getFavorToken();

        // sum of private objectives
        List<Card> privateObjectiveCardList = player.getPrivateObjectiveCardList();
        if (null != privateObjectiveCardList) {
            for (int i = 0; i < privateObjectiveCardList.size(); i++) {
                PrivateObjectiveCard privateObjectiveCard = (PrivateObjectiveCard) privateObjectiveCardList.get(i);

                scorePrivate += privateObjectiveCard.apply(board);
            }
        }
        player.setPrivateScore(scorePrivate);
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

        finalscore = scorePrivate + scorePublic - holesNumber + nFavorToken;

        if (finalscore<0)
            finalscore=0;

        return finalscore;
    }


    // **********************************
    // ---------- Game Action -----------

//    public int calcScore(Player player) {


    public synchronized boolean insertDice(long diceID, int row, int col)
//    public synchronized boolean insertDice(int indexDiceDraftPool, int row, int col)
            throws IndexOutOfBoundsException, AlreadyPickedException,
            BoardCellOccupiedException, EdgeRestrictionException, PatternColorRestrictionException, PatternNumericRestrictionException, AdjacentRestrictionException
    {
        int indexDiceDraftPool = draftPool.getDiceIndexByID(diceID);

        if (!Useful.isValueBetweenInclusive(indexDiceDraftPool, 0, draftPool.diceList().size() -1))
            throw new IndexOutOfBoundsException("\nIndex in draft pool should be: 0 <= " + indexDiceDraftPool + " <= " + (draftPool.diceList().size() -1));

        Dice dice = draftPool.diceList().get(indexDiceDraftPool);

        if (!currentPlayer.hasPickedDice()) {  // check that there is only one insert at turn
            throw new AlreadyPickedException(
                    "You have already Placed a Dice!\n" +
                    "Use Tool Card or Pass your turn please!"
            );
        } else {
            if (currentPlayer.getBoard().validMove(dice, row, col)) {

                dice = draftPool.chooseDice(indexDiceDraftPool);
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

    /**
     * Initialize the board with the give pattern type
     * return false if the board has already be set
     *
     * @param p the player that chooses the pattern
     * @param indexPatternType  the index of pattern in the pattern's list of this player can choose
     * @return  boolean of success operation
     */
    public boolean setPattern(Player p, int indexPatternType) {
        if (p.getBoard() == null) {    // A board is always associate with a pattern, if pattern hasn't be chosen it should be nul (not yet created)
            boolean bChosen = p.choosePatternCard(indexPatternType);   // here we create a new board associating it to the passed pattern
            if(bChosen) {
                return bChosen;
            }
        }
        return false;
    }



    public boolean freezePlayer(String username){
        Player player;
       for (int i=0; i< this.playerList.size(); i++)
           if (playerList.get(i).getName().equals(username)) {
                player=playerList.get(i);
                this.frozenPlayer.add(player);
                gameNotifyState = NotifyState.PLAYER_QUIT;
                setChanged();

               notifyObservers(new SerializableGame(this));
               //If there is only one player in game
               if (this.frozenPlayer.size() == playerList.size()-1) {
                   return false;
               }
               if (currentPlayer.equals(player))
                   player.passes(true);
               if (currentPlayer.equals(player))
                   player.passes(true);
               return true;
           }
           return true;
    }

    @Override
    public Boolean isfreeze(Player player) {
        if (this.frozenPlayer.contains(player))
            return true;
        else
            return false;
    }

    public  void  resumePlayer(String username){

        for (Player player:
             this.frozenPlayer) {
            if (player.getName().equals(username)) {
                this.frozenPlayer.remove(player);
            }
        }


    }
    public void updateAfterResumePlayer(String username){

        gameNotifyState = NotifyState.PLAYER_BACK;
        setChanged();
        notifyObservers(new SerializableGame(this, username));

    }

    public HashMap calcAllScore(){
       if (!isSolitaire()) {
           int points = 0;
           int max = -1;


           for (Player player :
                   this.playerList) {
               points = calcScore(player);
               if (points > max) {
                   max = points;
                   winner = player;

               } else if (points == max) {

                   handleDraw(player);
               }
               ranking.put(player, points);

           }

           gameNotifyState = NotifyState.RANKING;
           setChanged();
           notifyObservers(new SerializableGame(this));

       }
        return ranking;
    }

    public void handleDraw(Player player){

        if (player.getPrivateScore()>winner.getPrivateScore()) {
           winner = player;
           return;
       }
       else
           if (winner.getPrivateScore()>player.getPrivateScore())
                return;

       if (player.getFavorToken()>winner.getFavorToken()){
           winner = player;
           return;
       }
       else
           if(winner.getFavorToken()>player.getFavorToken())
                return;
       //To-do the winner is the last who picked the dice in the first turn in the last round
        int  winnerPosition=0;
        int  playerPosition=0;
        for (int i=0; i< this.playerList.size(); i++){
            if (lastRoundPlayers.get(i).getName().equals(winner.getName())) {
                winnerPosition = i;
                break;
            }

        }
        for (int i=0; i< this.playerList.size(); i++) {
            if (lastRoundPlayers.get(i).getName().equals(player.getName())){
                playerPosition=i;
                break;
            }
        }

        if (playerPosition>winnerPosition)
            winner=player;

    }


    public boolean isFirstTurn() {
        // during the first turn the rotation is clock wise
        return !bCountClockwise;
    }

/*
    public boolean useToolCard(int cardIndex, ToolCardParam param) {
        ToolCard toolCard = (ToolCard) toolCardList.get(cardIndex);
        boolean bSuccess;

        bSuccess = toolCard.getStrategy().use(param);

        if(bSuccess) {

            toolCard.addTokens();

            if (ToolCard.hasEffect[cardIndex]) {
                currentPlayer.addEffect(Card.Effect.getToolCardEffect(cardIndex));
            }
        }

        return bSuccess;
    }*/


    /**
     * convert tool card id to index
     *
     * @param id the id of tool card
     * @return  the index of tool card in the available card list
     *          -1, it does not exist in the list
     */
    private int getToolCardIndexByID(int id) {
        for (int i = 0; i < toolCardList.size(); i++) {
            if (id == toolCardList.get(i).effect.ID) {
                return i;
            }
        }
        return -1;
    }

    private ToolCard getToolCardByID (int id) {
        for (Card tc : toolCardList) {
            if (id == tc.effect.ID) {
                return (ToolCard) tc;
            }
        }
        return null;
    }
/*
    private ToolCard getToolCardByIndex (int cardIndex) {
        ToolCard toolCard = null;
        if(Useful.isValueBetweenInclusive(cardIndex, 0, toolCardList.size()-1))
            toolCard = (ToolCard) toolCardList.get(cardIndex);
        return toolCard;
    }
*/
    /**
     * Check that it exists inside the available card list of actual game
     * and then apply the tool card effect
     *
     * @param cardID ID of tool card
     * @param paramList the build tool card Integer parameters
     * @return is successful
     */
    public Boolean useToolCard(int cardID,  ArrayList<Integer> paramList) {
        // check that user uses an available tool card
        ToolCard toolCard = getToolCardByID(cardID);
        if(null == toolCard)    // user has not permission to use this tool card
            return false;

        else {
            ToolCardParam param = new ToolCardParam(
                    this.getRoundTrack(),
                    this.getDraftPool(),
                    this.getDiceBag(),
                    this.getCurrentPlayer().getBoard(),
                    this.isFirstTurn(),
                    !this.getCurrentPlayer().hasPickedDice(),   // required can pick dice
                    paramList
            );
            boolean bResult = this.useToolCard(toolCard, param);
            return bResult;
        }
    }

    /**
     * apply the effect of a tool card
     * if successful, save the card effect to the player, if it has
     *
     * @param toolCard  a ToolCard
     * @param toolCardParam the built tool card parameter, including Game information and Integers
     * @return is successful
     */
    private Boolean useToolCard(ToolCard toolCard, ToolCardParam toolCardParam){
        if (currentPlayer.isbUsedToolCard()) {  // check that there is only one insert at turn
            throw new AlreadyUsedExceptions();
        }

        if (toolCard.getTokensQuantity()+1> currentPlayer.getFavorToken())
            throw new NotEnoughTokenException();
        Boolean result = toolCard.getStrategy().use(toolCardParam);

        // check that this tool card need to return a dice list
        // it needs when it has multiple steps
        if(ToolCardParamBuilder.getStep(toolCard.effect.ID) > 1) {
            Observer currentObserver = (Observer) super.getObserverMap().get(currentPlayer.getName());
            if (currentObserver instanceof ProxyObserverSocket) {
                ((ProxyObserverSocket) currentObserver).handleCardUpdate((List<Dice>) toolCard.getStrategy().getReturn());
            }
            if (currentObserver instanceof ProxyObserverRMI) {
                ((ProxyObserverRMI) currentObserver).handleCardUpdate((List<Dice>) toolCard.getStrategy().getReturn());
            }
        }

        if (result){

            // add side Effect, the constraint, to the user player
            if(ToolCard.hasEffect[toolCard.effect.ID -1])
                currentPlayer.addEffect(toolCard.effect);

//                if(ToolCardParamBuilder.getStep(toolCard.effect.ID) <= 1  ||
            if(toolCard.getStrategy().getActualStep() == 0) {   // multi step card, resets to step = 0 when it finishes
                // for 2 step card like tc6 and tc11, there is just after the second step
                currentPlayer.setUsedToolCard(true);
                // remove insert constraint sideEffect, because the second move has already be done
                currentPlayer.removeInsertEffect();

                // pay favor tokens
                int tokenToRemove = 1 + toolCard.getTokensQuantity();
                int currentToken = currentPlayer.getFavorToken();

                currentPlayer.setFavorToken(currentToken - tokenToRemove);
                if (toolCard.getTokensQuantity() == 0) {
                    toolCard.addTokens();
                }
            }

                // notify that a new
                this.gameNotifyState=  TOOL_CARD;
                setChanged();
                notifyObservers(new SerializableGame(this));
        }

        return result;
    }

    public void resetCard() {
        // reset tool card's return
        toolCardList.forEach(card -> {
            ((ToolCard) card).getStrategy().reset();
        });
    }

    public void resetCardEffects() {
        // remove all player's all effects
        playerList.forEach(player -> {
            player.removeAllEffect();
        });
    }

    public void applyInsertEffect() {
        Card.Effect effect = currentPlayer.getInsertEffect();
        if(null != effect) {
            List<Dice> diceList = (List) getToolCardByID(effect.ID).getStrategy().getReturn();  // I'm sure that all dice with Insert Effect has return as ArrayList<Dice>
            for (int i = 0; i < diceList.size(); i++) {
                Dice dice = diceList.get(i);
                CellPosition cell = currentPlayer.getBoard().canBePlaced(dice);
                if(null != cell) {
                    try {
                        ToolCardParamBuilder builder = new ToolCardParamBuilder(effect.ID, 1);
                        builder.build(ToolCardParamType.DIALOG_BOX, i);
                        builder.build(ToolCardParamType.BOARD, cell.getRow(), cell.getCol());
                        boolean bResult = useToolCard( effect.ID , builder.getParams());  // it will reset the effect, counter, inside the strategy
//                        currentPlayer.getBoard().insertDice(dice, cell.getRow(), cell.getCol(), Board.Restriction.ALL);
                        if (bResult) {
                            break; // insert only one dice
                        }
                    } catch (Exception e) {
                        System.err.println("Can not insert Dice when it is possible in applyInsertEffect() with tool card n." + effect.ID);
                    }
                }
            }
            currentPlayer.removeInsertEffect();
        }
    }

    /**
     * This method handles the score of the solitaire game
     * @param privateObjectiveNumber number of the private objective choosen by the user
     */
    public void calcSolitaireScore(int privateObjectiveNumber){
        int scoreObjective=0;
        int playerPoint;
        for (List<Dice> list:
             this.getRoundTrack().getTrack()) {
            for (Dice dice:
                 list) {
                scoreObjective += dice.getDiceNumber();
            }
        }


        int scorePublic = 0;
        int scorePrivate = 0;
        int holesNumber;
        int nFavorToken;
        int finalscore;
        Player player =this.playerList.get(0);
        Board board = player.getBoard();


        //Number of free spaces on the board
        holesNumber= BOARD_BOXES - board.getDiceQuantity();



        // sum of private objectives
        List<Card> privateObjectiveCardList = player.getPrivateObjectiveCardList();
        if (null != privateObjectiveCardList) {

                PrivateObjectiveCard privateObjectiveCard = (PrivateObjectiveCard) privateObjectiveCardList.get(privateObjectiveNumber);

                scorePrivate += privateObjectiveCard.apply(board);
        }
        player.setPrivateScore(scorePrivate);

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

        finalscore = scorePrivate + scorePublic - 3*holesNumber ;

        if (finalscore<0)
            finalscore=0;
        ranking.put(player, finalscore);
        if (finalscore>scoreObjective)
            winner=player;
        else winner=new Player(" the cpu: \n your finalscore is "
                + finalscore + "\n and the objective score is: " + scoreObjective);


        gameNotifyState = NotifyState.RANKING;
        setChanged();
        notifyObservers(new SerializableGame(this));
    }

    public void endGame(){
        for (Player player:
             playerList) {
            if (!isfreeze(player))
                winner=player;
        }
        gameNotifyState=ALT_GAME;
        setChanged();
        notifyObservers(new SerializableGame(this));
    }

}
