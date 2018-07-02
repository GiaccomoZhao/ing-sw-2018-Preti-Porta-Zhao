package porprezhas.Network;

import porprezhas.Network.rmi.common.ServerRMIInterface;
import porprezhas.exceptions.toolCard.ToolCardParameterException;
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
    private ViewUpdateHandlerInterface viewUpdateHandlerInterface;

    public RMIClientAction() throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry();
        server = (ServerRMIInterface) registry.lookup("serverController");
    }


    public void setViewUpdateHandlerInterface(ViewUpdateHandlerInterface viewUpdateHandlerInterface) {
        this.viewUpdateHandlerInterface = viewUpdateHandlerInterface;
    }

    @Override
    public boolean isConnected() {
        return null != server;
    }

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

    @Override
    public boolean join(ViewUpdateHandlerInterface viewUpdateHandlerInterface) {
        this.viewUpdateHandlerInterface=viewUpdateHandlerInterface;
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
    public boolean resumeGame(ViewUpdateHandlerInterface viewUpdateHandlerInterface) {
        this.viewUpdateHandlerInterface=viewUpdateHandlerInterface;
       viewUpdateHandlerInterface.setGameStarted(true);
        try {
            return server.resumeGame(username);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

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
        } catch (ToolCardParameterException e) {
            System.err.println(e.getMessage());     // print Invalid Move Message
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
