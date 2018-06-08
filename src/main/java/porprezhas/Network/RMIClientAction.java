package porprezhas.Network;

import porprezhas.control.ServerRMIInterface;

import java.rmi.RemoteException;

public class RMIClientAction implements ClientActionInterface{

    ServerRMIInterface server;
    String username;

    public RMIClientAction(ServerRMIInterface server, String username) {
        this.server = server;
        this.username = username;
    }

    @Override
    public boolean moveDice(int fromIdContainer, int index, int toIdContainer, int row, int col) {
        try {
            if( server.insertedDice(index-1, row, col, username))
                return true;
            else
                return false;
        } catch (RemoteException e) {
            e.printStackTrace();
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
