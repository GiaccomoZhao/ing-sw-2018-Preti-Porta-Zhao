package porprezhas.RMI;

import porprezhas.model.Game;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteObserver extends Remote {
    void update(RemoteObservable ob, Object arg) throws RemoteException;
}
