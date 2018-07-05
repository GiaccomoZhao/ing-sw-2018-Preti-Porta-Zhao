package porprezhas.Network;

import porprezhas.model.Game;

import java.io.IOException;
import java.util.ArrayList;


public interface ClientActionInterface {
    boolean isConnected();
    int login(String username);
    boolean join(ViewUpdateHandlerInterface viewUpdateHandlerInterface);
    boolean joinSinglePlayer(ViewUpdateHandlerInterface viewUpdateHandlerInterface, Game.SolitaireDifficulty solitaireDifficulty);
    boolean resumeGame(ViewUpdateHandlerInterface viewUpdateHandlerInterface);
    void insertDice(long diceID, int toRow, int toCol);
    void pass();
    boolean useToolCard(String username, int toolCardID, ArrayList<Integer> paramList);
    void choosePattern(int patternIndex);
}
