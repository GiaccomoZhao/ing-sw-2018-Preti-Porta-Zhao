package porprezhas.Network.rmi.common;

import porprezhas.model.SerializableGameInterface;
import porprezhas.model.dices.Dice;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteObserver extends Remote {
    void update( SerializableGameInterface arg) throws RemoteException;
    boolean checkState() throws RemoteException;
    void updateCardEffect(List<Dice> diceList) throws RemoteException;
}
