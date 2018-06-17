package porprezhas.Network.rmi.common;

import porprezhas.model.SerializableGameInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteObserver extends Remote {
    void update( SerializableGameInterface arg) throws RemoteException;
}
