package porprezhas.model;

import porprezhas.model.cards.Card;
import porprezhas.model.dices.DraftPool;
import porprezhas.model.dices.RoundTrack;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public interface SerializableGameInterface extends Serializable {

     RoundTrack getRoundTrack();
     DraftPool getDraftPool();
     Game.NotifyState getGameNotifyState();
     List<Player> getPlayerList();
     Player getCurrentPlayer();
     Player getUsernamePlayer(String username);
     Player getQuitPlayer();
     String getResumePlayer();
     List<Card> getPublicObjectiveCardList();
     List<Card> getToolCardList();
     HashMap getRanking();
     Player getWinner();
//    public Player getFirstPlayer();

}
