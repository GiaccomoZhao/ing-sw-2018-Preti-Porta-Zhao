package porprezhas.model;

import porprezhas.model.dices.Board;
import porprezhas.model.dices.Dice;
import porprezhas.model.cards.*;
import porprezhas.model.dices.Pattern;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player implements Serializable {

    // player identity attribute
    private Long playerID;
    private String name;
    private int position;
    private int iconId;

    // Game play attribute
    private List<Pattern.TypePattern> patternsToChoose;
    private Board board;
    private List<Card> privateObjectiveCardList;
//    private ScoreMark scoreMark;    // thinking to move this to
    private int nFavorToken;

    // Game control attribute
    private boolean bPass;
    private boolean bUsedToolCard;
    private int pickableDice;


    public Player(String name) {
        playerID = new Random().nextLong();     // TODO: this should be player's unique (String)username or (Long)ID
        this.name = name;
        nFavorToken = 0;
        privateObjectiveCardList = new ArrayList<>();
        patternsToChoose = new ArrayList<>();

        bUsedToolCard = false;
        pickableDice = 1;
    }

    public Long getPlayerID() {
        return playerID;
    }

    public boolean isbUsedToolCard() {
        return bUsedToolCard;
    }

    public int getPickableDice() {
        return pickableDice;
    }

    public void setPlayerID(Long playerID) {
        this.playerID = playerID;
    }

    public String getName() {
        return name;
    }

//    public void setName(String name) {
//        this.name = name;
//    }

    public int getPosition() {
        return position;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIcon(int iconId) {
        this.iconId = iconId;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Board getBoard() {
        return board;
    }

    public int getFavorToken() {
        return nFavorToken;
    }

    private void setFavorToken(int favorToken) {
        this.nFavorToken = favorToken;
    }

// converts from pattern difficulty to nFavorToken quantity
    public void setFavorTokenByDifficulty(int difficulty) {
        int nFavorToken = difficulty;
        setFavorToken(nFavorToken);
    }

    public List<Pattern.TypePattern> getPatternsToChoose() {
        return new ArrayList<>(patternsToChoose);
    }

    public void setPatternsToChoose(List<Pattern.TypePattern> patternsToChoose) {
        this.patternsToChoose = patternsToChoose;
    }

    public List<Card> getPrivateObjectiveCardList() {
        return new ArrayList<Card>(privateObjectiveCardList);
    }   // return a new list to protect the list being not modified by external class

    public void setPrivateObjectCardList(List<Card> privateObjectiveCardList) {
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

    public boolean isDicePickable() {
        return pickableDice > 0;
    }

    public void resetPickableDice() {
        pickableDice = Game.GameConstants.DICE_PICK_PER_TURN;
    }

    public void addPickableDice() {
        pickableDice ++;
    }

    public boolean choosePatternCard (int indexPatternType) {
        if(indexPatternType < patternsToChoose.size()) {
            if(indexPatternType == -666)        // NOTE: for test use... do not forget to remove it on release! (not TO-DO)
                this.board = new Board(Pattern.TypePattern.VOID);
            else {
                this.board = new Board(
                        patternsToChoose.get(indexPatternType));
            }
            return true;
        }
        return false;
    }

    public void placeDice(Dice dice, int row, int col) {
        board.insertDice(dice, row, col);
        pickableDice--;
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
