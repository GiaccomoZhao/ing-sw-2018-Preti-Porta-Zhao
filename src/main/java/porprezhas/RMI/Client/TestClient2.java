package porprezhas.RMI.Client;



import porprezhas.control.ServerControllerInterface;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TestClient2 {
    public static void main(String[] args) throws RemoteException, NotBoundException, AlreadyBoundException {


        MainView mainView = new MainView(0);
        Registry registry= LocateRegistry.getRegistry();

        mainView.setServerController((ServerControllerInterface) registry.lookup("serverController"));
        mainView.run();



    }
}
