package porprezhas.Network.rmi.server;



import porprezhas.model.ProxyObserverRMI;
import porprezhas.model.ProxyObserverSocket;
import porprezhas.model.Game;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Observable;

public abstract class ModelObservable extends Observable
        implements RemoteObservable, Serializable {

    public ModelObservable() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public void addObserver(String username) throws RemoteException {
        ProxyObserverRMI po = null;

        try {
            po = new ProxyObserverRMI(username);
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

        super.addObserver(po);
        System.out.println("Added observer");
    }

    public void addObserver(ObjectOutputStream objectOutputStream) {

        ProxyObserverSocket po = null;

        po = new ProxyObserverSocket(objectOutputStream);

        super.addObserver(po);
        System.out.println("Added observer");
    }

    @Override
    public void printBoard() throws RemoteException {
        System.out.println("AAAAAAAAAAA");
    }

    @Override
    public Game GetThisGame() throws RemoteException {
        return null;
    }


}
