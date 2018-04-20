package porprezha.model;

import porprezha.model.cards.PrivateObjectiveCard;
import porprezha.model.dices.Dice;

import java.util.Random;

public class Player {
    class PatternCard{}
    class GameBoard {
        public void takePatternCard(PatternCard patterCard) {
            ;
        }
    }
    class ScoreMark {}

    private Long playerID;
    private String name;
    private int position;
    private GameBoard gameBoard;
    private ScoreMark scoreMark;    // thinking to move this to
    private int favorToken;    // or list of class FavoreToken{Image i} ?
    private PrivateObjectiveCard privateObjectiveCard;

    private boolean bPass;

    public Player() {
        favorToken = Game.GameConstants.FAVOR_TOKEN_QUANTITY;
        playerID = new Random().nextLong();     // TODO: this should be player's unique (String)username or (Long)ID
    }


    public Long getPlayerID() {
        return playerID;
    }

    public void setPlayerID(Long playerID) {
        this.playerID = playerID;
    }

    public String getName (){
        return new String(name);
    }

    public void setName(String name) {
        name = new String(name);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public int getFavorToken() {
        return favorToken;
    }

    public void setFavorToken(int favorToken) {
        this.favorToken = favorToken;
    }

    public void takePrivateObjectCard(PrivateObjectiveCard privateObjectiveCard) {
        this.privateObjectiveCard = privateObjectiveCard;
    }

    public void takeBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void choosePatternCard (PatternCard patternCard) {
        this.gameBoard.takePatternCard(patternCard);
    }

    public void takeDice(Dice dice) {
    }

    /* 3 optional actions:
     *   1. choose a Dice from Draft Pool
     *   2. use a Tool Card
     *   3. pass/finish
     */
    public void play() {
        // set flag to unlock view actions
    }

}
