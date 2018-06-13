package porprezhas.Network;

import porprezhas.control.ServerRMIInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static porprezhas.view.fx.gameScene.GuiSettings.bDebug;

public class RMIClientAction implements ClientActionInterface{

    private ServerRMIInterface server;
    private String username;
    private Registry registry;


    public RMIClientAction() {
        try {
            registry = LocateRegistry.getRegistry();
            server = (ServerRMIInterface) registry.lookup("serverController");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

    }


    //TO_DO DELETE THIS
    public RMIClientAction(ServerRMIInterface server, String username) {
        this.server = server;
        this.username = username;
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
    public boolean join() {

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
