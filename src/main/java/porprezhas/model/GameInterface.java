package porprezhas.model;

import porprezhas.model.dices.DiceBag;
import porprezhas.model.dices.DraftPool;

import java.util.List;

// To create a new Game needs a PlayerList as parameter for construction method
public interface GameInterface {
    // Player List Management -- 2 modifier methods
    List<Player> getPlayerList();
    Player getCurrentPlayer();
    Player rotatePlayer();  // modify current player, index to the list
    void orderPlayers();    // modify entire list

    DiceBag getDiceBag();
    DraftPool getDraftPool();

    boolean InsertDice(int indexDice, int xPose, int yPose);
    boolean isSolitaire();
    long getRoundTimeOut();
    boolean setPattern(Player player, int indexPatternType);
    void nextRound();
    int calcScore(Player player);
    void newTurn();

    void playerPrePrepare();
    void playerPostPrepare();
    void gamePrepare();



}
