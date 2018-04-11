package PorPreZha.Model;

import java.util.ArrayList;
import java.util.List;

public class Game {
    class ScoreTrack {;}

    private List<Player> players;
    private RoundTrack roundTrack;
    private ScoreTrack scoreTrack;
    private State state;
    private int diceQuantity;
    private int firstPlayer;

    class GameConstants {
        public static final int DIDE_QUANTITY = 90;
        public static final int MAX_PLAYER_QUANTITY = 4;
        public static final int ROUND_NUM = 10;
    }

    Game() {
        roundTrack = new RoundTrack();
        state = new State();
        firstPlayer = 0;
        diceQuantity = GameConstants.DIDE_QUANTITY;
    }

    public List<Player> getPlayerList() {
        return new ArrayList<Player>(players);
    }

    public int getRoundIndex(){
        return roundTrack.getRound();
    }

    public int getDiceQuantity() {
        return  diceQuantity;
    }

    public Player getFirstPlayer() {
        return players.get(firstPlayer);
    }

    public State getState() {
        return state;
    }

    public void joinPlayer() {
        ;
    }
    public void quitPlayer() {
        ;
    }

    private void orderPlayers() {
        ;
    }

    public void start() {
        orderPlayers();
    }
    public void playerPrepare() {
        ;
    }
    public void gamePrepare() {
        ;
    }
    public void round() {
        ;
    }
    public int calcScore() {
        return 0;
    }
    public void endGame() {
        ;
    }
}
