package porprezhas.RMI.Client;



import porprezhas.RMI.RemoteObservable;
import porprezhas.RMI.RemoteObserver;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class ClientObserver implements RemoteObserver {

    private RemoteObservable remoteObservable;

    public ClientObserver( String ObjectRmiName) throws RemoteException, NotBoundException {

        UnicastRemoteObject.exportObject(this, 0);
        Registry registry= LocateRegistry.getRegistry();
        System.out.print("RMI registry bindings: ");
        remoteObservable  = (RemoteObservable) registry.lookup("game");
        try
        {
            remoteObservable.addObserver(this);
        }
        catch (Exception e)
        {
            System.err.println("Subscription exception: " + e);
            e.printStackTrace();
        }

    }

    @Override
    public void update(RemoteObservable ob, Object arg) throws RemoteException {
        System.out.println("Aggiorno la view");
    }
}
