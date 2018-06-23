package porprezhas.Network.rmi.server;



import porprezhas.control.ServerControllerInterface;
import porprezhas.model.ProxyObserverRMI;
import porprezhas.model.ProxyObserverSocket;
import porprezhas.model.Game;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public abstract class ModelObservable extends Observable
        implements RemoteObservable, Serializable {

    HashMap observerMap;


    public ModelObservable() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
        observerMap=new HashMap();

    }


    public void addObserver(String username, ServerControllerInterface serverControllerInterface) throws RemoteException {
        ProxyObserverRMI po = null;

        try {
            po = new ProxyObserverRMI(username, serverControllerInterface);
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        observerMap.put(username, po);
        super.addObserver(po);
        System.out.println("Added observer");
    }

    public void addObserver(String username, ObjectOutputStream objectOutputStream) {

        ProxyObserverSocket po = null;

        po = new ProxyObserverSocket(objectOutputStream);
        this.observerMap.put(username, po);
        super.addObserver(po);
        System.out.println("Added observer");
    }

    public void removeObserver(String username){
        Observer observer=(Observer) this.observerMap.get(username);
        super.deleteObserver(observer);
    }






}
