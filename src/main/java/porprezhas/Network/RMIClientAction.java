package porprezhas.Network;

import porprezhas.control.ServerRMIInterface;

import java.rmi.RemoteException;

import static porprezhas.view.fx.gameScene.GuiSettings.bDebug;

public class RMIClientAction implements ClientActionInterface{

    ServerRMIInterface server;
    String username;

    public RMIClientAction(ServerRMIInterface server, String username) {
        this.server = server;
        this.username = username;
    }

    @Override
    public boolean moveDice(int fromIdContainer, long diceID, int toIdContainer, int row, int col) {

        try {
            if( server.insertedDice(diceID, row, col, username)){
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
//            if(bDebug)
//                e.printStackTrace();
        }

        return false;
    }

    @Override
    public void pass() {
        try {
            server.passUser(username);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
