package porprezha.model;

import porprezha.model.cards.PublicObjectiveCard;

import java.rmi.Remote;
import java.util.List;

// To create a new Game needs a PlayerList as parameter for construction method
public interface GameInterface extends Remote {
    List<PublicObjectiveCard> getPublicObjectiveCardList();

    // Player List Management -- 2 modifier methods
    List<Player> getPlayerList();
    Player getCurrentPlayer();
    Player rotatePlayer();
    void orderPlayers();

    boolean isSolitaire();
}
