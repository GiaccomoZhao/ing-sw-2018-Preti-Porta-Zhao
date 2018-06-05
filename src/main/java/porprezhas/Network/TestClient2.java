package porprezhas.Network;



import porprezhas.Network.RMIClient;
import porprezhas.control.ServerControllerInterface;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TestClient2 {
    public static void main(String[] args) throws RemoteException, NotBoundException, AlreadyBoundException {

        RMIClient rmiClient = new RMIClient();

        Registry registry= LocateRegistry.getRegistry();


        rmiClient.run();



    }
}
