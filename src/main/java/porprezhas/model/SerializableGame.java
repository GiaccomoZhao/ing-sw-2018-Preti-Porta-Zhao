package porprezhas.model;

import porprezhas.model.cards.Card;
import porprezhas.model.dices.DraftPool;
import porprezhas.model.track.RoundTrack;

import java.util.List;

//This class is a serializable class that contains all the informations
// and the classes that the model has to send to the view

public class SerializableGame implements SerializableGameInterface {

    private RoundTrack roundTrack;
    private DraftPool draftPool;
    private Game.NotifyState gameNotifyState;
    private List<Player> playerList;
    private Player currentPlayer;

    public SerializableGame(GameInterface game) {
        this.roundTrack=game.getRoundTrack() ;
        this.draftPool= game.getDraftPool();
        this.gameNotifyState= game.getGameState();
        this.playerList= game.getPlayerList();
        this.currentPlayer= game.getCurrentPlayer();
    }

    public RoundTrack getRoundTrack() {
        return roundTrack;
    }

    public DraftPool getDraftPool() {
        return draftPool;
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

    @Override
    public Player getUsernamePlayer(String username) {
        for (Player player:
                this.playerList) {
            if(player.getName().equals(username))
                return player;
        }
    return null;
    }
}
