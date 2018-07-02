package porprezhas.view.fx.gameScene.state;

import porprezhas.model.GameInterface;
import porprezhas.model.cards.Card;
import porprezhas.model.dices.Dice;

import java.rmi.RemoteException;

public interface GameViewStateInterface {
    Card.Effect usingCard = null;

    int playing = 0;                // choose dice from draft + use card + ...
    int watch = 1;                  // already picked
    int chooseDiceInDraft = 2;      // effect card 1, 6(I), 10, 11(I)
    int moveAnyDiceInBoard = 3;     // effect card 2-4, 12
    int exchangeDraftAndTrack = 4;  // effect card 5
    int moveDiceDraftToBoard = 5;   // effect card 8, 9
    int chooseDiceValue = 6;        // effect card 11(II)
    int moveSelectedDiceInBoard = 7;// effect card 6(II), 11(III)


    GameViewStateInterface[] list = {
            new Playing()
    };



    Dice chooseDiceInDraft(int indexDraft);
    boolean moveAnyDiceInBoard(int fromCol, int fromRow, int toCol, int toRow);
    boolean exchangeDraftAndTrack(int indexDraft, int round, int rowRound);
//    void watch();
    boolean moveDiceDraftToBoard(int indexDraft, int round, int rowRound);
    Dice chooseDiceValue(int value);
    boolean moveSelectedDiceInBoard(int toCol, int toRow);




//    boolean selectDice(DiceContainerType container, int pram1, int param2);
//    boolean moveDice(DiceContainerType containerFrom, int fromP1, int fromP2,
//                     DiceContainerType containerTo, int toP1, int toP2);
//    Dice newDice(DiceContainerType containerFrom, int fromP1, int fromP2);



    void pass() throws RemoteException;        // unblock thread
    int calcScore(int iPlayer) throws RemoteException;
    GameViewStateInterface getState() throws RemoteException;    // when server want to know what is game doing -- running or waiting player exit
    GameInterface getGame() throws RemoteException;

//    boolean choosePattern(Player player, int indexPatternType) throws RemoteException;
//    boolean insertDice(Integer indexDice, Integer xPose, Integer yPose) throws RemoteException;
    boolean useToolCard(int cardId) throws RemoteException;

}
