package porprezhas.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteObservable extends Remote {
    void addObserver(RemoteObserver ro) throws RemoteException;
    void deleteObserver(RemoteObserver ro) throws RemoteException;
}
