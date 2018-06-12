package porprezhas.Network;

import porprezhas.Network.Command.InsertDiceGuiAction;
import porprezhas.Network.Command.PassAction;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class SocketClientAction implements ClientActionInterface {

    String username;
    ObjectOutputStream objectOutputStream;

    public SocketClientAction(String username,ObjectOutputStream objectOutputStream) {
        this.username = username;
       this.objectOutputStream=objectOutputStream;
    }

    @Override
    public boolean moveDice(int fromIdContainer, long diceID, int toIdContainer, int row, int col) {
        try {
             objectOutputStream.writeObject(new InsertDiceGuiAction(username, toIdContainer, row, col ));
                return false;


        }  catch (Exception e) {

            System.err.println(e.getMessage());     // print Invalid Move Message
//            if(bDebug)
//                e.printStackTrace();
        }

        return false;
    }

    @Override
    public void pass() {
        try {
            objectOutputStream.writeObject(new PassAction(username));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
