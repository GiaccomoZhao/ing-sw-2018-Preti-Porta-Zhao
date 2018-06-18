package porprezhas.Network;

import java.util.ArrayList;

public interface ClientActionInterface {
    boolean isConnected();
    boolean login(String username);
    boolean join(ViewUpdateHandlerInterface viewUpdateHandlerInterface);
    boolean moveDice(int fromIdContainer, long diceID, int toIdContainer, int row, int col);
    boolean pass();
    boolean useToolCard(String username, int toolCardID, ArrayList<Integer> paramList);
}
