package porprezhas.Network;

import porprezhas.model.SerializableGameInterface;
import porprezhas.model.dices.Dice;

import java.util.List;

/**
 * This interface handles all the possible informations received from the server.
 */
public interface ViewUpdateHandlerInterface {
    void update(SerializableGameInterface game);
    void setGameStarted(Boolean gameStarted);
    void invalidAction();
    void invalidDiceInsert(Exception e);
    void invalidUseToolCard(Exception e);
    void handleCardEffect(List<Dice> diceList);
    void toolCardUsed();
}
