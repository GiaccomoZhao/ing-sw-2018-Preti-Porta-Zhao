package porprezhas.RMI;

import porprezhas.model.Game;
import porprezhas.model.Player;
import porprezhas.model.dices.DraftPool;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface RemoteObservable extends Remote {
    void addObserver(RemoteObserver ro) throws RemoteException;
    void deleteObserver(RemoteObserver ro) throws RemoteException;
    void printBoard() throws RemoteException;

    Game GetThisGame() throws RemoteException;
    Player getActualPlayer() throws RemoteException;
    ArrayList<Player> getPlayers() throws RemoteException;

    Game.NotifyState getGameNotifyState() throws  RemoteException;
    Player getFirstPlayer() throws RemoteException;
     int getiCurrentPlayer() throws RemoteException;
     DraftPool getDraftpoolRmi() throws RemoteException;


}
