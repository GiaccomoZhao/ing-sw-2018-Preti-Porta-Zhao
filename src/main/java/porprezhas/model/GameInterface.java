package porprezhas.model;

import porprezhas.exceptions.diceMove.*;
import porprezhas.model.cards.Card;
import porprezhas.model.cards.ToolCard;
import porprezhas.model.cards.ToolCardParam;
import porprezhas.model.dices.DiceBag;
import porprezhas.model.dices.DraftPool;
import porprezhas.model.dices.RoundTrack;

import java.util.HashMap;
import java.util.List;

// To create a new Game needs a PlayerList as parameter for construction method
public interface GameInterface {
    // Player List Management -- 2 modifier methods
    List<Player> getPlayerList();
    Player getCurrentPlayer();
    Player rotatePlayer();  // modify current player, index to the list
    void orderPlayers();    // modify entire list
    void freezePlayer(String username);
    Boolean isfreeze(Player player);
    void resumePlayer(String username);
    void updateAfterResumePlayer(String username);
    DiceBag getDiceBag();
    DraftPool getDraftPool();
    RoundTrack getRoundTrack();
    List<Card> getToolCardList();
    List<Card> getPublicObjectiveCardList();
    List<Player> getFrozenPlayer();
//    List<Card> getPrivateCards(Player player);

    boolean isSolitaire();
    boolean isFirstTurn();
    long getRoundTimeOut();

    Game.NotifyState getGameState();

    //    boolean insertDice(int indexDiceDraftPool, int toRow, int toColumn)
    boolean insertDice(long diceID, int toRow, int toColumn)
            throws IndexOutOfBoundsException, // NotYourTurnException, AlreadyPickedException,
            BoardCellOccupiedException, EdgeRestrictionException, PatternColorRestrictionException, PatternNumericRestrictionException, AdjacentRestrictionException;
    boolean useToolCard(int cardIndex, ToolCardParam param);
    Boolean useToolCard(ToolCard toolcard, ToolCardParam toolCardParam);
    boolean setPattern(Player player, int indexPatternType);

    void newRound(int indexRound);
    int calcScore(Player player);
    void newTurn();

    void playerPrePrepare();
    void playerPostPrepare();
    void gamePrepare();
    HashMap getRanking();
    Player getWinner();
    HashMap calcAllScore();





}
