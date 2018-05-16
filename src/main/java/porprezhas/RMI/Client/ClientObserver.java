package porprezhas.RMI.Client;



import porprezhas.RMI.RemoteObservable;
import porprezhas.RMI.RemoteObserver;
import porprezhas.model.Game;

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
        Game game = (Game) ob;  // TODO:
        System.out.println("Player: " + game.getCurrentPlayer().getName());
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(game.getCurrentPlayer().getBoard().getDice(i, j).getDiceNumber());
                System.out.print(game.getCurrentPlayer().getBoard().getDice(i, j).colorDice + " ");
            }
            System.out.println();
        }
    }
}
