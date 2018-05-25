package porprezhas.RMI.Client;



import porprezhas.RMI.RemoteObservable;
import porprezhas.RMI.RemoteObserver;
import porprezhas.model.Game;
import porprezhas.model.GameInterface;
import porprezhas.model.Player;

import javax.swing.text.View;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;


public class ClientObserver implements RemoteObserver {

    private RemoteObservable remoteObservable;
    private ViewClient viewClient;

    public ClientObserver( ViewClient viewClient, int indexGame) throws RemoteException, NotBoundException {
        this.viewClient=viewClient;
        UnicastRemoteObject.exportObject(this, 0);
        Registry registry= LocateRegistry.getRegistry();
        System.out.print("RMI registry bindings: ");
        remoteObservable  = (RemoteObservable) registry.lookup("game".concat(String.valueOf(indexGame)));
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

        viewClient.updateClient((RemoteObservable) arg);


        /*System.out.println( "\n\n\n" +
                "  ***** >>> GAME OVER <<< *****  \n\n" +
                "              Score              \n");
        for (Player playera : players) {
            System.out.format("    %-15s \t\n",
                    player.getName());*/



    }




}
