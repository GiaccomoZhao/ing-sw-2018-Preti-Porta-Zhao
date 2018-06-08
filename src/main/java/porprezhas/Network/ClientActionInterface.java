package porprezhas.Network;

public interface ClientActionInterface {
    boolean moveDice(int fromIdContainer, int index, int toIdContainer, int row, int col);
    void pass();
}
