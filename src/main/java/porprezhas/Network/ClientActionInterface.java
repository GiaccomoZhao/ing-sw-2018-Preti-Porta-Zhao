package porprezhas.Network;

import java.util.ArrayList;

public interface ClientActionInterface {
    boolean isConnected();
    boolean login(String username);
    boolean join(ViewUpdateHandlerInterface viewUpdateHandlerInterface);
//    boolean moveDice(int idBoardFrom, long diceID, int idBoardTo, int toRow, int toCol);
//    boolean moveDice(int indexDiceDraftPool, int idBoardTo, int toRow, int toCol);
    boolean insertDice(long diceID, int toRow, int toCol);
//    boolean moveDice(int idBoardFrom, int fromRow, int fromCol, int idBoardTo, int toRow, int toCol);
    boolean pass();
    boolean useToolCard(String username, int toolCardID, ArrayList<Integer> paramList);
}
