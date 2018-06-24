package porprezhas.model;

import porprezhas.model.cards.Card;
import porprezhas.model.dices.DraftPool;
import porprezhas.model.dices.RoundTrack;

import java.io.Serializable;
import java.util.List;

public interface SerializableGameInterface extends Serializable {

    public RoundTrack getRoundTrack();
    public DraftPool getDraftPool();
    public Game.NotifyState getGameNotifyState();
    public List<Player> getPlayerList();
    public Player getCurrentPlayer();
    public Player getUsernamePlayer(String username);
    public Player getQuitPlayer();
    public List<Card> getPublicObjectiveCardList();
    public List<Card> getToolCardList();

//    public Player getFirstPlayer();

}
