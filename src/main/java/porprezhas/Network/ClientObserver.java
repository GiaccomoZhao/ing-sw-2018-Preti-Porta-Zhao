package porprezhas.Network;



import porprezhas.Network.ViewUpdateHandlerInterface;
import porprezhas.RMI.RemoteObservable;
import porprezhas.RMI.RemoteObserver;
import porprezhas.model.Game;
import porprezhas.model.GameInterface;
import porprezhas.model.Player;
import porprezhas.model.SerializableGameInterface;

import javax.swing.text.View;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


public class ClientObserver extends UnicastRemoteObject implements RemoteObserver {


    private ViewUpdateHandlerInterface viewUpdateHandlerInterface;
    private String username;

    public ClientObserver( ViewUpdateHandlerInterface viewUpdateHandlerInterface, String username) throws RemoteException, NotBoundException {
        super();
        Registry registry = LocateRegistry.getRegistry();
        registry.rebind(username,this );
        this.viewUpdateHandlerInterface=viewUpdateHandlerInterface;

    }

    @Override
    public void update( SerializableGameInterface arg) throws RemoteException {

        viewUpdateHandlerInterface.update( arg);


        }




}
