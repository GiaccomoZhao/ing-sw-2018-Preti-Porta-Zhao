package porprezhas.Network;

public interface ClientActionInterface {
    boolean moveDice(int fromIdContainer, long diceID, int toIdContainer, int row, int col);
    void pass();
}
