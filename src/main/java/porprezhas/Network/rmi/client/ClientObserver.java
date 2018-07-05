package porprezhas.Network.rmi.client;



import porprezhas.Network.ViewUpdateHandlerInterface;
import porprezhas.Network.rmi.common.RemoteObserver;
import porprezhas.model.SerializableGameInterface;
import porprezhas.model.dices.Dice;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;


public class ClientObserver extends UnicastRemoteObject implements RemoteObserver {


    private ViewUpdateHandlerInterface viewUpdateHandlerInterface;
    private String username;
    private Boolean state = true;
    private int port;
    private Registry registry;
    public ClientObserver( ViewUpdateHandlerInterface viewUpdateHandlerInterface, String username, int port) throws RemoteException {
        super();
        this.port=port;
            try {
                registry = LocateRegistry.createRegistry( port);
            }catch (ExportException e){
                registry = LocateRegistry.getRegistry(port);
                e.printStackTrace();
            }


        registry.rebind(username,this );
        this.viewUpdateHandlerInterface=viewUpdateHandlerInterface;

    }


    @Override
    public void update( SerializableGameInterface arg) throws RemoteException {

        viewUpdateHandlerInterface.update( arg);


        }

    @Override
    public boolean checkState() throws RemoteException {
        return state;
    }

    @Override
    public void updateCardEffect(List<Dice> diceList) throws RemoteException {
        viewUpdateHandlerInterface.handleCardEffect(diceList);
    }
}
