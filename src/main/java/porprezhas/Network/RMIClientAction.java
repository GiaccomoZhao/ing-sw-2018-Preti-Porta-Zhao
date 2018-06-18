package porprezhas.Network;

import porprezhas.Network.rmi.common.ServerRMIInterface;
import porprezhas.view.fx.gameScene.state.DiceContainer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class RMIClientAction implements ClientActionInterface{

    private ServerRMIInterface server;
    private String username;
    private Registry registry;


    public RMIClientAction() throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry();
        server = (ServerRMIInterface) registry.lookup("serverController");
    }


    //TO_DO DELETE THIS
    public RMIClientAction(ServerRMIInterface server, String username) {
        this.server = server;
        this.username = username;
    }

    @Override
    public boolean isConnected() {
        return null != server;
    }

    @Override
    public boolean login(String username) {
        try {
            if (server.login(username)){
                this.username=username;
                return true;
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean join(ViewUpdateHandlerInterface viewUpdateHandlerInterface) {

        try {
           if(server.joinGame(username)){

                   return true;

           }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
//    public boolean moveDice(int idBoardFrom, long diceID, int idBoardTo, int toRow, int toCol) {
//    public boolean moveDice(int indexDiceDraftPool, int idBoardTo, int toRow, int toCol) {
    public boolean insertDice(long diceID, int toRow, int toCol) {

//        if(DiceContainer.fromPlayer(username)DiceContainer.fromInt(idBoardTo))
        try {
            if( server.insertedDice(diceID, toRow, toCol, username)){
//                if( server.insertedDice(indexDiceDraftPool, toRow, toCol, username)){
                System.out.println("Dice inserted");
                return true;
            }
            else{
                System.out.println("Not valid move");
                return false;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {

            System.err.println(e.getMessage());     // print Invalid Move Message

        }

        return false;
    }

    @Override
    public boolean pass() {
        try {
           return server.passUser(username);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
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
        } catch (Exception e) {
            System.err.println(e.getMessage());     // print Invalid Move Message
        }
        return true;
    }


}
