package porprezhas.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteObserver extends Remote {
    void update(RemoteObservable ob, Object arg) throws RemoteException;
}
