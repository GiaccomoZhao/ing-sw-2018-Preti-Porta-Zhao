package porprezhas.model;

import porprezhas.model.cards.ToolCard;
import porprezhas.model.track.RoundTrack;
import porprezhas.model.track.ScoreTrack;
import porprezhas.model.cards.*;

import java.util.*;

public class Game implements GameInterface {
    public static class GameConstants {
        private GameConstants() {
        }

        public static final int DICE_QUANTITY = 90;
        public static final int MAX_PLAYER_QUANTITY = 4;
        public static final int ROUND_NUM = 10;
        public static final int FAVOR_TOKEN_QUANTITY = 3;
        public static final int TIMEOUT_PREPARING_SEC = 60;
        public static final int TIMEOUT_ROUND_SEC = 33;             // this game should spends at max 45 min: 45*60 == 33(sec)*4(players)*2*10(round) + 60
        public static final int TIMEOUT_ROUND_SOLITAIRE_SEC = 90;   // solitaire should spend 30 min: 90sec * 2*10round == 30min

        public static int secondsToMillis(int seconds) {
            return  seconds * 1000;
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
    private RoundTrack roundTrack;
    private ScoreTrack scoreTrack;
    private List<PublicObjectiveCard> publicObjectiveCardList;
    private List<ToolCard> toolCardList;

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

    public Game(List<Player> playerList, SolitaireDifficulty difficulty) {
        gameID = new Random().nextLong();   // senseless until we have a global server
        roundTrack = new RoundTrack();

        this.playerList = playerList;
        resetPlayerIndexes();

        if(playerList.size() == 1) {
            bSolitaire = true;
            solitaireDifficulty = difficulty;
        } else {
            bSolitaire = false;
        }
        diceQuantity = Game.GameConstants.DICE_QUANTITY;
    }

    /* @requires playerList.size() > 0
     * @ensure (* indexes have be set correctly as at starting *)
     */
    void resetPlayerIndexes() {
        iCurrentPlayer = 0;
        iFirstPlayer = 0;
        iLastPlayer = playerList.size()-1;
        bCountClockwise = false;
    }

    public boolean isSolitaire() {
        return bSolitaire;
    }

    public SolitaireDifficulty getSolitaireDifficulty() {
        return solitaireDifficulty;
    }

    public int getRoundIndex() {
        return roundTrack.getRound();
    }

    public int getDiceQuantity() {
        return diceQuantity;
    }

    public List<PublicObjectiveCard> getPublicObjectiveCardList() {
        return publicObjectiveCardList;
    }

    public List<ToolCard> getToolCardList() {
        return toolCardList;
    }


    // *********************************
    // ----- Player list management ----    // should i put this in a new class file?

    // return a new object that has reference to same elements
    public List<Player> getPlayerList() {
        return new ArrayList<Player>(playerList);
    }

    public Player getIndexFirstPlayer() {
        return playerList.get(iFirstPlayer);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    // these are unique 2 method that modify currentPlayer directly
    // and they are private methods
    private void setCurrentPlayerByIndex() {
        this.currentPlayer = playerList.get(iCurrentPlayer);
    }

    private void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

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
            if(iCurrentPlayer == iFirstPlayer) {
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
            if(iCurrentPlayer == iLastPlayer) {
                // arrived at last player he'll play 2 time consecutively
                // so we do not call nextPlayer and just change rotation direction (clockwise)
                bCountClockwise = true;
            } else {
                nextPlayer();
            }
        }
        return currentPlayer;   // nextPlayer() method changes this attribute
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
        if (playerList.size() >= Game.GameConstants.MAX_PLAYER_QUANTITY)
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

    public void orderPlayers() {
        if (playerList.size() <= 1) {
            return;
        } else {
            Collections.shuffle(playerList);
        }
    }

}
