package porprezhas.model;

import porprezhas.model.cards.PublicObjectiveCard;
import porprezhas.model.dices.DiceBag;
import porprezhas.model.dices.DraftPool;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

// To create a new Game needs a PlayerList as parameter for construction method
public interface GameInterface {
    List<PublicObjectiveCard> getPublicObjectiveCardList();

    // Player List Management -- 2 modifier methods
    List<Player> getPlayerList();
    Player getCurrentPlayer();
    Player rotatePlayer();
    void orderPlayers();
    public DiceBag getDiceBag();
    public DraftPool getDraftPool();
    public Boolean InsertDice(int indexDice, int xPose, int yPose);
    boolean isSolitaire();
}
