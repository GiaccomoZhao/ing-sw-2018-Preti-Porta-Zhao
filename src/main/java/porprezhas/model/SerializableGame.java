package porprezhas.model;

import porprezhas.model.cards.Card;
import porprezhas.model.dices.DraftPool;
import porprezhas.model.dices.RoundTrack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//This class is a serializable class that contains all the informations
// and the classes that the model has to send to the view

public class SerializableGame implements SerializableGameInterface {

    private RoundTrack roundTrack;
    private DraftPool draftPool;
    private List<Card> toolCards;
    private List<Card> publicCards;
    private List<Player> playerList;
    private Player currentPlayer;
    private Game.NotifyState gameNotifyState;
    private Player quitPlayer;
    private HashMap ranking;
    private Player winner;


    public SerializableGame(GameInterface game) {
        this.roundTrack=game.getRoundTrack() ;
        this.draftPool= game.getDraftPool();
        this.toolCards = game.getToolCardList();
        this.publicCards = game.getPublicObjectiveCardList();
        this.playerList= game.getPlayerList();
        this.currentPlayer= game.getCurrentPlayer();
        this.gameNotifyState= game.getGameState();
        if (game.getFrozenPlayer().size()>0)
            this.quitPlayer= game.getFrozenPlayer().get(game.getFrozenPlayer().size()-1);
        if(this.gameNotifyState.equals(Game.NotifyState.RANKING)){
            this.winner= game.getWinner();
            this.ranking=game.getRanking();
        }

    }

    public RoundTrack getRoundTrack() {
        return roundTrack;
    }

    public DraftPool getDraftPool() {
        return draftPool;
    }


    public List<Card> getToolCardList() {
        return toolCards;
    }
    public List<Card> getPublicObjectiveCardList() {
        return publicCards;
    }


    public Game.NotifyState getGameNotifyState() {
        return gameNotifyState;
    }


    public List<Player> getPlayerList() {
        return playerList;
    }

    public Player getCurrentPlayer(){
        return currentPlayer;
    }


    public Player getQuitPlayer() {
        return quitPlayer;
    }

    @Override
    public Player getUsernamePlayer(String username) {
        for (Player player:
                this.playerList) {
            if(player.getName().equals(username))
                return player;
        }
    return null;
    }


    public HashMap getRanking() {
        return ranking;
    }

    public Player getWinner() {
        return winner;
    }
}
