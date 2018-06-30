package porprezhas.Network;

import porprezhas.model.SerializableGameInterface;

public interface ViewUpdateHandlerInterface {
    void update(SerializableGameInterface game);
    void setGameStarted(Boolean gameStarted);
    void invalidAction();
    void invalidDiceInsert(Exception e);
}
