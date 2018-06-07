package porprezhas.control.state;

import porprezhas.control.GameControllerInterface;
import porprezhas.model.GameInterface;
import porprezhas.model.Player;
import porprezhas.model.dices.Dice;

import java.rmi.RemoteException;

public interface GameState {
    int playing = 1;
    int State3 = 3;
    int State4 = 4;
    int usingToolCard1 = 5;
    int usingToolCard2 = usingToolCard1 + 1;
    int usingToolCard3 = usingToolCard1 + 2;
    int usingToolCard4 = usingToolCard1 + 3;
    int usingToolCard5 = usingToolCard1 + 4;
    int usingToolCard6 = usingToolCard1 + 5;
    int usingToolCard7 = usingToolCard1 + 6;
    int usingToolCard8 = usingToolCard1 + 7;
    int usingToolCard9 = usingToolCard1 + 8;
    int usingToolCard10 = usingToolCard1 + 9;
    int usingToolCard11 = usingToolCard1 + 10;
    int usingToolCard12 = usingToolCard1 + 11;


    boolean selectDice(DiceContainer container, int pram1, int param2);
    boolean moveDice(DiceContainer containerFrom, int fromP1, int fromP2,
                     DiceContainer containerTo, int toP1, int toP2);
    Dice newDice(DiceContainer containerFrom, int fromP1, int fromP2);



    void pass() throws RemoteException;        // unblock thread
    int calcScore(int iPlayer) throws RemoteException;
    GameState getState() throws RemoteException;    // when server want to know what is game doing -- running or waiting player exit
    GameInterface getGame() throws RemoteException;

    boolean choosePattern(Player player, int indexPatternType) throws RemoteException;
    boolean insertDice(Integer indexDice, Integer xPose, Integer yPose) throws RemoteException;
    boolean useToolCard() throws RemoteException;

}
