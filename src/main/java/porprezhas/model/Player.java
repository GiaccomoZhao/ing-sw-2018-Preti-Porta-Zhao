package porprezhas.model;

import porprezhas.model.dices.Board;
import porprezhas.model.dices.Dice;
import porprezhas.model.cards.*;
import porprezhas.model.dices.Pattern;

import java.util.List;
import java.util.Random;

public class Player {


    private Long playerID;
    private String name;
    private int position;

    private Board board;
    private List<PrivateObjectiveCard> privateObjectiveCardList;
//    private ScoreMark scoreMark;    // thinking to move this to
    private int favorToken;    // or list of class FavoreToken{Image i} ?

    private boolean bPass;

    private boolean bUsedToolCard;
    private int pickableDice;


    public Player(String name) {
        favorToken = Game.GameConstants.FAVOR_TOKEN_QUANTITY;
        playerID = new Random().nextLong();     // TODO: this should be player's unique (String)username or (Long)ID
        this.name = name;
        bUsedToolCard = false;
        pickableDice = 1;
    }
/*
    public Player() {
        favorToken = Game.GameConstants.FAVOR_TOKEN_QUANTITY;
        playerID = new Random().nextLong();     // TODO: this should be player's unique (String)username or (Long)ID
        name = new String("noName");
    }
*/

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

    public Board getBoard() {
        return board;
    }

    public int getFavorToken() {
        return favorToken;
    }

    public void setFavorToken(int favorToken) {
        this.favorToken = favorToken;
    }

    public List<PrivateObjectiveCard> getPrivateObjectiveCardList() {
        return privateObjectiveCardList;
    }

    public void setPrivateObjectCardList(List<PrivateObjectiveCard> privateObjectiveCardList) {
        this.privateObjectiveCardList = privateObjectiveCardList;
    }

    public boolean hasPassed() {    // TODO: can someone help me to make a better name for this method?
        return bPass;
    }

    public void passes(boolean bPass) {
        this.bPass = bPass;
    }

    public boolean hasUsedToolCard() {
        return bUsedToolCard;
    }

    public void setUsedToolCard(boolean bUsedToolCard) {
        this.bUsedToolCard = bUsedToolCard;
    }

    public int getPickableDice() {
        return pickableDice;
    }

    public void setPickableDice(int pickableDice) {
        this.pickableDice = pickableDice;
    }

    public void choosePatternCard (Pattern pattern) {
        this.board = new Board(pattern);
    }

    public void placeDice(Dice dice, int x, int y) {
        board.insertDice(dice, x, y);
    }

    /* 3 optional actions:
     *   1. choose a Dice from Draft Pool
     *   2. use a Tool Card
     *   3. pass/finish
     */
    public void play() {
        bPass = false;
        // set flag to unlock view actions
    }

}
