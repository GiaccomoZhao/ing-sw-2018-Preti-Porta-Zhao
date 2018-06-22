package porprezhas.Network.rmi.server;

import porprezhas.model.Game;
import porprezhas.model.Player;
import porprezhas.model.dices.DraftPool;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface RemoteObservable extends Remote {
    void addObserver(String username)throws RemoteException ;




    Player getActualPlayer() throws RemoteException;
    ArrayList<Player> getPlayers() throws RemoteException;

    Game.NotifyState getGameNotifyState() throws  RemoteException;
    Player getFirstPlayer() throws RemoteException;
     int getIndexCurrentPlayer() throws RemoteException;
     DraftPool getDraftpoolRmi() throws RemoteException;


}
