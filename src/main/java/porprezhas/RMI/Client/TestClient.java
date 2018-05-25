package porprezhas.RMI.Client;

import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TestClient {
    public static void main(String[] args) throws RemoteException, NotBoundException, AlreadyBoundException {

        //System.setSecurityManager(new RMISecurityManager());
        ViewClient viewClient= new ViewClient(0);
        ClientObserver testObserver= new ClientObserver( viewClient, 0);
        Registry registry= LocateRegistry.getRegistry();
       registry.rebind("view", testObserver);




    }
}
