package porprezhas.Network;

import porprezhas.Network.command.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class SocketClientAction implements ClientActionInterface {

    private String username;
    private Socket socket;

    private ObjectInputStream socketIn;
    private ObjectOutputStream socketOut ;
    private ClientAnswerHandler clientAnswerHandler;


    //TO_DO DELETE THIS
    public SocketClientAction(String username,ObjectOutputStream objectOutputStream) {
        this.username = username;
        try {
            socketOut = new ObjectOutputStream(objectOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SocketClientAction(InetAddress ip, int port) {

        try {
            this.socket = new Socket(ip, port);
            socketOut = new ObjectOutputStream(socket.getOutputStream());
            socketIn = new ObjectInputStream(socket.getInputStream());
            clientAnswerHandler = new ClientAnswerHandler();
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("Connection established");

    }

    @Override
    public boolean isConnected() {
        return null != socket;
    }

    @Override
    public boolean login(String username) {

        try {

            socketOut.writeObject(new LoginAction(username));
            socketOut.flush();
           if( ((Answer) socketIn.readObject()).handle(clientAnswerHandler)) {
               this.username=username;

               return true;
           }

        } catch (IOException e) {
            System.err.println("Exception on network: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    return false;
    }

    @Override
    public boolean join(ViewUpdateHandlerInterface viewUpdateHandlerInterface) {
        this.clientAnswerHandler.setViewUpdateHandlerInterface(viewUpdateHandlerInterface);
        try {

            socketOut.writeObject(new JoinAction(username));
            socketOut.flush();
            ((Answer) socketIn.readObject()).handle(clientAnswerHandler);
           Thread thread = new Thread(){
                public void run() {
                    Boolean bool=true;
                    while(bool){
                        try {
                            ((Answer) socketIn.readObject()).handle(clientAnswerHandler);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }}
            };
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    return true;
    }

    @Override
    public boolean moveDice(int fromIdContainer, long diceID, int toIdContainer, int row, int col) {
        try {
             socketOut.writeObject(new InsertDiceAction(username, diceID, row, col ));
             socketOut.flush();


        }  catch (Exception e) {

            System.err.println(e.getMessage());     // print Invalid Move Message
//            if(bDebug)
//                e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean pass() {
        try {
            socketOut.writeObject(new PassAction(username));
            socketOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean useToolCard(String username, int toolCardID, ArrayList<Integer> paramList) {
        try {
            socketOut.writeObject(new UseToolCardAction(username, toolCardID, paramList));
            socketOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    public void closeConnection(){
        try {
            socketIn.close();
            socketOut.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
