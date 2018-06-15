package porprezhas.Network;

public interface ClientActionInterface {
    boolean isConnected();
    boolean login(String username);
    boolean join(ViewUpdateHandlerInterface viewUpdateHandlerInterface);
    boolean moveDice(int fromIdContainer, long diceID, int toIdContainer, int row, int col);
    void pass();
}
