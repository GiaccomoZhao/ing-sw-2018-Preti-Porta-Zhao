package porprezhas.Network;

import java.io.IOException;
import java.util.ArrayList;

public interface ClientActionInterface {
    boolean isConnected();
    int login(String username);
    boolean join(ViewUpdateHandlerInterface viewUpdateHandlerInterface);
    boolean resumeGame(ViewUpdateHandlerInterface viewUpdateHandlerInterface);
//    boolean moveDice(int idBoardFrom, long diceID, int idBoardTo, int toRow, int toCol);
//    boolean moveDice(int indexDiceDraftPool, int idBoardTo, int toRow, int toCol);
    void insertDice(long diceID, int toRow, int toCol);
//    boolean moveDice(int idBoardFrom, int fromRow, int fromCol, int idBoardTo, int toRow, int toCol);
    void pass();
    boolean useToolCard(String username, int toolCardID, ArrayList<Integer> paramList);
    void choosePattern(int patternIndex);
}
