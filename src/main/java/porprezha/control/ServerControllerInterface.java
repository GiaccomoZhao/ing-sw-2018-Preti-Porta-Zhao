package porprezha.control;

import porprezha.model.Player;

import java.rmi.Remote;

public interface ServerControllerInterface extends Remote {
    void join(Player newPlayer);
    GameControllerInterface createNewGame();
}
