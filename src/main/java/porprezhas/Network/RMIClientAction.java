package porprezhas.Network;

import porprezhas.Network.rmi.common.ServerRMIInterface;
import porprezhas.exceptions.GenericInvalidActionException;
import porprezhas.exceptions.toolCard.ToolCardParameterException;
import porprezhas.model.Game;
import porprezhas.view.fx.gameScene.state.DiceContainer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

/**
 * This class implements all RMI actions
 */
public class RMIClientAction implements ClientActionInterface{

    private ServerRMIInterface server;
    private String username;
    private Registry registry;
    private ViewUpdateHandlerInterface viewUpdateHandlerInterface;
    private String ip;
    private int port;
    private int myport;

    public RMIClientAction() throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry();
        server = (ServerRMIInterface) registry.lookup("serverController");
    }
    public RMIClientAction(String ip, int port) throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry(ip, port);
        server = (ServerRMIInterface) registry.lookup("serverController");
    }

    public void setMyport(int myport) {
        this.myport = myport;
    }

    public void setViewUpdateHandlerInterface(ViewUpdateHandlerInterface viewUpdateHandlerInterface) {
        this.viewUpdateHandlerInterface = viewUpdateHandlerInterface;
    }

    @Override
    public boolean isConnected() {
        return null != server;
    }

    /**
     * This method handles RMI login
     * @param username the username of the player that wants to try login
     * @return the answer of the request:
     * Username available
     * Username not_available
     * Username lost connection
     */
    @Override
    public int login(String username) {
        try {
           this.username=username;
           return server.login(username);


        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     *This method handles rmi Join
     * It saves the viewUpdateHandler and tries to join, giving to the server the ip and the port
     * of the registry of the client
     * @param viewUpdateHandlerInterface The handler of all the answer of the server( cli or gui)
     * @return
     */
    @Override
    public boolean join(ViewUpdateHandlerInterface viewUpdateHandlerInterface) {
        this.viewUpdateHandlerInterface=viewUpdateHandlerInterface;
        try {
           if(server.joinGame(username, InetAddress.getLocalHost().getHostAddress(), myport)){

                   return true;

           }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**This method handles rmi Join for single player mode
     *It saves the viewUpdateHandler and tries to join, giving to the server the ip and the port
     * of the registry of the client.
     * @param viewUpdateHandlerInterface The handler of all the answer of the server( cli or gui)
     * @param solitaireDifficulty The level of difficulty choosen by the user
     *
     *@return
     */
    @Override
    public boolean joinSinglePlayer(ViewUpdateHandlerInterface viewUpdateHandlerInterface, Game.SolitaireDifficulty solitaireDifficulty) {
        this.viewUpdateHandlerInterface=viewUpdateHandlerInterface;
        try {
            if(server.joinSinglePlayer(username, InetAddress.getLocalHost().getHostAddress(), myport, solitaireDifficulty)){

                return true;

            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method handles the case when an user that lost his connection tries to resume the match
     * after he logged with the old username.
     * It calls the setter setGameStarted(true) because he tells to the viewUpdateHandler that the game
     * is running so that it can skip the "setup" phase.
     *
     * @param viewUpdateHandlerInterface the reference of the server update handler
     * @return
     */
    @Override
    public boolean resumeGame(ViewUpdateHandlerInterface viewUpdateHandlerInterface) {
        this.viewUpdateHandlerInterface=viewUpdateHandlerInterface;
       viewUpdateHandlerInterface.setGameStarted(true);
        try {
            return server.resumeGame(username, InetAddress.getLocalHost().getHostAddress(), myport);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method handles the rmi Dice insert action
     *If the move isn't valid the server throws an exception and this method sends it to
     * the viewUpdateHandler that handles the cause of the wrong answer
     *
     * @param diceID Id of the dice that the user wants to insert
     * @param toRow Number of row
     * @param toCol Number of column
     */
    @Override
    public void insertDice(long diceID, int toRow, int toCol) {

        try {
            server.insertedDice(diceID, toRow, toCol, username);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            viewUpdateHandlerInterface.invalidDiceInsert(e);
        }


    }

    @Override
    public void pass() {
        try {
           if (!server.passUser(username))
               viewUpdateHandlerInterface.invalidAction();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean useToolCard(String username, int toolCardID, ArrayList<Integer> paramList) {
        try {
            if(server.usedToolCard(username,toolCardID , paramList)) {

                System.out.println("ToolCardUsed!");


            } else {
                System.out.println("Param error");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (GenericInvalidActionException e) {
            viewUpdateHandlerInterface.invalidUseToolCard(e);     // print Invalid Move Message
        } catch (Exception e) { // catch all to avoid missing exception
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void choosePattern(int patternIndex) {

        try {
          server.choosePattern(patternIndex, username);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


}
