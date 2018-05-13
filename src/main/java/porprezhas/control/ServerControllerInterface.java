package porprezhas.control;

import porprezhas.model.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerControllerInterface extends Remote {
    void join(Player newPlayer) throws RemoteException;
    GameControllerInterface createNewGame() throws RemoteException;
    boolean isAlreadyInGame(Player player) throws RemoteException;
    GameControllerInterface getGameController(Player player) throws RemoteException;
}
