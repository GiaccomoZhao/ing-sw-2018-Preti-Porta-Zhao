package porprezhas.RMI.Server;



import porprezhas.RMI.RemoteObservable;
import porprezhas.RMI.RemoteObserver;
import porprezhas.model.Game;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Observable;

public abstract class ModelObservable extends Observable
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
    @Override
    public void printBoard() throws RemoteException {
        System.out.println("AAAAAAAAAAA");
    }

    @Override
    public Game GetThisGame() throws RemoteException {
        return null;
    }

    public void test(){
        setChanged();
        notifyObservers();
    }
}
