package porprezhas.RMI;

import porprezhas.control.GameController;
import porprezhas.control.GameControllerInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MainViewInterface extends Remote {
    Boolean addGameController(int indexGameController) throws RemoteException;

}
