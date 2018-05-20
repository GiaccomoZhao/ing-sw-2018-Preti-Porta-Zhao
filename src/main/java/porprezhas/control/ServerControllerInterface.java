package porprezhas.control;

import porprezhas.model.Game;
import porprezhas.model.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerControllerInterface extends Remote {
    void join(Player newPlayer) throws RemoteException;
    void leave(Player player) throws RemoteException;
    GameControllerInterface createNewGame() throws RemoteException;
    GameControllerInterface createNewGame(Player player, Game.SolitaireDifficulty difficulty) throws RemoteException;
    boolean isAlreadyInGame(Player player) throws RemoteException;
    GameControllerInterface getGameController(Player player) throws RemoteException;
}
