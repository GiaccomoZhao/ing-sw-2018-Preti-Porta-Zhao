package porprezhas.RMI.Client;


import porprezhas.RMI.RemoteObservable;
import porprezhas.model.Game;
import porprezhas.model.GameInterface;
import porprezhas.model.Player;
import porprezhas.model.dices.Pattern;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class ViewClient {

    private Player player;
    private Boolean first;
    private Boolean firstPlayer;
    private final int numberPlayer;
    private Player nextPlayer;


    public ViewClient(int numberPlayer) {

        first = false;
        firstPlayer= false;
        this.numberPlayer= numberPlayer;
    }

    public void updateClient(RemoteObservable game) throws RemoteException {

        Game.NotifyState state = game.getGameNotifyState();
        ArrayList<Player> players= ((RemoteObservable) game).getPlayers();


        switch(state){

            case NEW_FIRST_PLAYER:
                if(!firstPlayer || !nextPlayer.getName().equals(game.getFirstPlayer().getName()) ) {
                    nextPlayer=game.getFirstPlayer();

                    System.out.println("Now the first player is: " + game.getFirstPlayer().getName());
                    firstPlayer=true;
                }
                break;

            case CHOOSE_PATTERN:

                System.out.println("You have to choose your pattern");
                Pattern.TypePattern pattern1 = game.getPlayers().get(numberPlayer).getPatternsToChoose().get(0);
                Pattern.TypePattern pattern2 = game.getPlayers().get(numberPlayer).getPatternsToChoose().get(1);
                System.out.println(pattern1.name() + "    o    " + pattern2.name());
                break;

            case GAME_STARTED:

                System.out.println("Game started!");
                break;

            case NEXT_ROUND:

                System.out.println("Next Round");
                break;

            case DICE_INSERTED:
                if(!first ||  players.get(numberPlayer).getBoard().getDiceQuantity()!=player.getBoard().getDiceQuantity()){
                    player= players.get(numberPlayer);
                    System.out.println(player.getName() + " inserted a dice:");
                    player.getBoard().print(false);
                    first=true;
                }
                break;
        }




    }
}
