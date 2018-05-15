package porprezhas.RMI.Server;



import porprezhas.RMI.RemoteObservable;
import porprezhas.RMI.RemoteObserver;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Observable;

public class ModelObservable extends Observable
        implements RemoteObservable, Serializable {

    public ModelObservable() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public void addObserver(RemoteObserver ro) throws RemoteException {
    ProxyObserver po = new ProxyObserver(ro);
        super.addObserver(po);
        System.out.println("Added observer");
    }

    @Override
    public void deleteObserver(RemoteObserver ro) throws RemoteException {
    ;
    }

    public void test(){
        setChanged();
        notifyObservers();
    }
}
