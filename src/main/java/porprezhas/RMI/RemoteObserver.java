package porprezhas.RMI;

import porprezhas.model.Game;
import porprezhas.model.SerializableGameInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Observable;

public interface RemoteObserver extends Remote {
    void update( SerializableGameInterface arg) throws RemoteException;
}
