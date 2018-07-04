package porprezhas.Network.rmi.client;



import porprezhas.Network.ViewUpdateHandlerInterface;
import porprezhas.Network.rmi.common.RemoteObserver;
import porprezhas.model.SerializableGameInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class ClientObserver extends UnicastRemoteObject implements RemoteObserver {


    private ViewUpdateHandlerInterface viewUpdateHandlerInterface;
    private String username;
    private Boolean state = true;
    private int port;

    public ClientObserver( ViewUpdateHandlerInterface viewUpdateHandlerInterface, String username, int port) throws RemoteException {
        super();
        this.port=port;
        Registry registry = LocateRegistry.createRegistry( port);
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
    public void updateCardEffect(Object object) throws RemoteException {
        viewUpdateHandlerInterface.handleCardEffect(object);
    }
}
