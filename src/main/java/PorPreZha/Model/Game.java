package PorPreZha.Model;

import PorPreZha.Exceptions.GamePlayerFullException;
import PorPreZha.Exceptions.InvalidPlayerException;
import PorPreZha.Exceptions.PlayerAlreadyPresentException;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.*;

public class Game {
    public static class GameConstants {
        public static final int DICE_QUANTITY = 90;
        public static final int MAX_PLAYER_QUANTITY = 4;
        public static final int ROUND_NUM = 10;
        public static final int FAVOR_TOKEN_QUANTITY = 3;
        public static final int TIMEOUT_PREPARING_SEC = 60;
        public static final int TIMEOUT_ROUND_SEC = 33;             // this game should spends at max 45 min: 45*60 == 33(sec)*4(players)*2*10(round) + 60
        public static final int TIMEOUT_ROUND_SOLITAIRE_SEC = 90;   // solitaire should spend 30 min: 90sec * 2*10round == 30min
    }

    public enum StateMachine {
        WAITING_FOR_PLAYER, // INITIAL STATE, Waiting for host's Start
        STARTED,            // Flag, STARTED < state < FINISHED means a game is running
        PLAYER_PREPARING,  // Give player cards etc.
        GAME_PREPARING,     // Place toolCard and public object and decide the first player
        PLAYING,            // During round phase
        FINISHED,           // Flag, state > FINISHED means game has already been finished
        ENDING;             // LAST STATE, Player watching score and chatting

        private static StateMachine[] states = values();    // make private static copy of the values avoids copying each time it is used

        public StateMachine getState() {
            return this;
        }

        // we have a cycle sequence state machine
        public StateMachine getNextState() {
            return this.ordinal() != StateMachine.ENDING.ordinal() ?
                    states[this.ordinal() + 1] :
                    StateMachine.WAITING_FOR_PLAYER;
        }

        public boolean isGameRunning() {
            return hasGameStarted() && !hasGameFinished();
        }

        public boolean hasGameStarted() {
            return this.getState().ordinal() >= Game.StateMachine.STARTED.ordinal();
        }

        public boolean hasGameFinished() {
            return this.getState().ordinal() >= StateMachine.FINISHED.ordinal();
        }
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
        private int toToolCardsQuantity() {
            return size - this.ordinal() + 1;
        }
    }


    // *********************************
    // --- Declaration of Attributes ---

    private final Long gameID;      // for internet game, but we need a server
    private List<Player> playerList;
    private RoundTrack roundTrack;
    private ScoreTrack scoreTrack;
    private List<PublicObjectiveCard> publicObjectiveCardList;
    private List<ToolCard> toolCardList;
    private StateMachine state;
    private int diceQuantity;
    private int iFirstPlayer;    // index of first player in current round
    private int iCurrentPlayer;
    private boolean bCountClockwise;
    //    private boolean bSolitaire;     // not used if we don't let playerList change during game
    private Player currentPlayer;
    private SolitaireDifficulty solitaireDifficulty;    // in Multi-player we have Player.Pattern.Difficulty


    // *********************************
    // ------ Basic Class Methods ------

    public Game() {
        gameID = new Random().nextLong();   // senseless until we have a global server
        roundTrack = new RoundTrack();
        playerList = new ArrayList<>();
        state = StateMachine.WAITING_FOR_PLAYER;
        diceQuantity = GameConstants.DICE_QUANTITY;
        iCurrentPlayer = iFirstPlayer = 0;
        bCountClockwise = false;
//        bSolitaire = true;
        try {
            joinPlayer(new Player());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Player> getPlayerList() {
        return new ArrayList<Player>(playerList);
    }

    public int getRoundIndex() {
        return roundTrack.getRound();
    }

    public StateMachine getState() {
        return state;
    }

    public int getDiceQuantity() {
        return diceQuantity;
    }

    public Player getIndexFirstPlayer() {
        return playerList.get(iFirstPlayer);
    }

    /*
        private int getiCurrentPlayer() {
            return iCurrentPlayer;
        }

        private void setiCurrentPlayer(int iCurrentPlayer) {
            this.iCurrentPlayer = iCurrentPlayer;
        }
    */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    private void setCurrentPlayer() {
        this.currentPlayer = playerList.get(iCurrentPlayer);
    }

    private void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    // set Player to next one and return him
    public Player rotatePlayer() {
        if (bCountClockwise) {
            if (iCurrentPlayer == 0) {
                iCurrentPlayer = playerList.size() - 1;
            } else {
                iCurrentPlayer--;
            }
        } else {
            if (iCurrentPlayer == playerList.size() - 1) {
                iCurrentPlayer = 0;
            } else
                iCurrentPlayer++;
        }
        setCurrentPlayer(); // change currentPlayer
        return currentPlayer;
    }


    // *********************************
    // ---------- Game Logic -----------

    /* @requires playerList.size() > 0
       @ensure (*we have a loop of index that refers entire playerList*)
       @ */
    public void nextFirstPlayer() {
        if (iFirstPlayer < playerList.size() - 1)
            iFirstPlayer++;
        else
            iFirstPlayer = 0;
    }

    /* @ensures playerList.contain(player)
       @signals (GamePlayerFullException e) \old(playerList).size >= max
       @signals (InvalidPlayerException e) param == null
       @signals (PlayerAlreadyPresentException e) (*unique Player already present in game*)
       @ */
    public void joinPlayer(Player newPlayer) throws GamePlayerFullException, InvalidPlayerException, PlayerAlreadyPresentException {
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
    public void leavePlayer(Player player) {
        playerList.remove(player);
//        bSolitaire = playerList.size() == 1 ? true : false;
    }

    /* @requires playerList.size > 0
       @ensure (*a random order*) ||
               (*Order players based on the last time the player has been to the TEACHER's DESK*)
       @ */
    private void orderPlayers() {
        if (playerList.size() <= 1) {
            return;
        } else {
            Collections.shuffle(playerList);
        }
    }


    public void newGame() {
        state = StateMachine.WAITING_FOR_PLAYER;
    }

    public void start() {
        System.out.println("Game: Start");
        ;
        orderPlayers();
        state = StateMachine.STARTED;
    }

    /* @requires playerList.size() > 0
       @ensure (*in Solitaire*) &&
       @       (*Player choose a difficulty 1 to 5*) &&
       @       (*2 random PatternCard to choose one from 4 faces*) &&
       @       (*the Player receives 2 Private Objective Cards*) &&
       @       (*FavorTokens == 0*) ||
       @ensure (*in Multi-player game*) &&
       @       (*every Player receives: 2 random PatternCard to choose one from 4 faces*) &&
       @       (*a random hidden Private Objective Card*) &&
       @       (*a quantity of Favor Tokens equals to difficulty of Pattern Card*)
       @ */
    public void playerPrepare() {
        state = StateMachine.PLAYER_PREPARING;
        // wait playerList choose Pattern Card
        // with observer.update: player.choosePatternCard();
        if (playerList.size() == 0) {
            return;
        } else if (playerList.size() == 1) {
            Player player = playerList.get(0);
            player.setFavorToken(0);
            player.takePrivateObjectCard(null); // TODO: 2 random cards
        } else {
            // give a random private card
            // give 2 random PatternCard to choose one from 4 faces
            // give favorTokens based on difficulty of patternCard
        }
    }

    /* @requires playerList.size() > 0
       @ensure (*in Solitaire*) &&
       @       (*place 2 Public Objective Cards*) &&
       @       (*place 1(difficulty=extreme) to 5(easy) Tool Cards*) ||
       @ensure (*in Multi-player*) &&
       @       (*place 3 Public Objective Cards*) &&
       @       (*place 3 Tool Cards*)
       @ */
    public void gamePrepare() {
        state = StateMachine.GAME_PREPARING;
//        randToolCard();
//        View.update();
        if (playerList.size() == 0) {
            return;
        } else if (playerList.size() == 1) {
            publicObjectiveCardList = new ArrayList<PublicObjectiveCard>(2);        // TODO: give them a concrete value
            toolCardList = new ArrayList<ToolCard>(solitaireDifficulty.toToolCardsQuantity());
        } else {
            publicObjectiveCardList = new ArrayList<PublicObjectiveCard>(3);
            toolCardList = new ArrayList<ToolCard>(3);
        }
    }


    /* 3 optional actions:
        1. choose a Dice from Draft Pool
        2. use a Tool Card
        3. pass/finish
       @ensure (*in Solitaire*) &&
       @       (*a Pool of 4 Dice, 2 times at round*) ||
       @ensure (*in Multi-player*) &&
       @       (*a Pool of 2 * PlayerList.size() + 1 Dices*)&&
       @       (*rotate clockwise and counter-clockwise, so every player has 2 times at round*)
       @ */
    public void playRound() {
        state = StateMachine.PLAYING;
        int iPlayer = 0;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future =
                new FutureTask<String>(new Callable<String>() { // use interface Callable as construction parameter
                    public String call() {
                        playerList.get(0).play();
                        return new String();
                    }
                });
        for (int i = 0; i < 2 * playerList.size(); i++) {
// TODO:            draftPool.reroll(); // remove and put new dices
            //wait everybody for a timeout then pass or player pass
            executor.execute((Runnable) future);
            try {
                future.get(GameConstants.TIMEOUT_ROUND_SOLITAIRE_SEC * 1000, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                future.cancel(true);
            } finally {
                executor.shutdown();
            }
            rotatePlayer();
        }
    }

    private int calcScore() {
        return 0;
    }

    public void endGame() {
        calcScore();
        System.out.println("Game: End");
        ;
        System.out.println("Score: ...");
        state = StateMachine.ENDING;
    }

    @Test
    public static void main(String[] args) {
        Player player;
        Game game;

        game = new Game();
        player = new Player();


        game.newGame();
        try {
            game.joinPlayer(player);
        } catch (GamePlayerFullException e) {
            e.printStackTrace();
        } catch (InvalidPlayerException e) {
            e.printStackTrace();
        } catch (PlayerAlreadyPresentException e) {
            e.printStackTrace();
        }

        while (!game.getState().hasGameStarted()) {
            ;   // wait for game.joinPlayer(player);
            game.start(); // host call Game.start()
        }

        game.playerPrepare();
        game.gamePrepare();
        for (int iRound = 0; iRound < Game.GameConstants.ROUND_NUM; iRound++) {
            game.playRound();
        }
        game.endGame();
    }
}
