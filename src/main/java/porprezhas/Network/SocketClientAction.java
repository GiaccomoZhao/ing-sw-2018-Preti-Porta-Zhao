package porprezhas.Network;

import porprezhas.Network.command.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import static porprezhas.control.ServerController.ALREADY_IN_GAME;
import static porprezhas.control.ServerController.USERNAME_ALREADY_TAKEN;
import static porprezhas.control.ServerController.USERNAME_AVAILABLE;

public class SocketClientAction implements ClientActionInterface, AnswerHandler {

    private String username;
    private int loginCase;
    private Boolean joinCase;
    private Socket socket;

    private ObjectInputStream socketIn;
    private ObjectOutputStream socketOut ;
    private ViewUpdateHandlerInterface viewUpdateHandlerInterface;


    public SocketClientAction(InetAddress ip, int port) {

        try {
            this.socket = new Socket(ip, port);
            socketOut = new ObjectOutputStream(socket.getOutputStream());
            socketIn = new ObjectInputStream(socket.getInputStream());

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
    public int login(String username) {
        this.username=username;
        try {

            socketOut.writeObject(new LoginAction(username));
            socketOut.flush();
            ((Answer) socketIn.readObject()).handle(this);
            return loginCase;
        } catch (IOException e) {
            System.err.println("Exception on network: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    return USERNAME_ALREADY_TAKEN;
    }

    @Override
    public boolean join(ViewUpdateHandlerInterface viewUpdateHandlerInterface)   {
        this.viewUpdateHandlerInterface=viewUpdateHandlerInterface;
      
        try {
            System.out.println(username);
            socketOut.writeObject(new JoinAction(username));
            socketOut.flush();
            ((Answer) socketIn.readObject()).handle(this);
            SocketClientAction socketClientAction=this;
           Thread thread = new Thread(){
                public void run() {
                    Boolean bool=true;
                    while(bool){
                        try {

                            ((Answer) socketIn.readObject()).handle(socketClientAction);
                        } catch (IOException e) {
                            if (e instanceof SocketException)
                                bool=false;
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
    public boolean insertDice(long diceID, int row, int col) {
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

    @Override
    public void choosePattern(int patternIndex, String username) {
        try {
            socketOut.writeObject(new ChoosePatternAction(patternIndex, username));
            socketOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
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


    public void setViewUpdateHandlerInterface(ViewUpdateHandlerInterface viewUpdateHandlerInterface) {
        this.viewUpdateHandlerInterface = viewUpdateHandlerInterface;
    }


    @Override
    public void handle(UpdateAnswer updateAnswer) {

        this.viewUpdateHandlerInterface.update(updateAnswer.serializableGameInterface);

    }

    @Override
    public void handle(LoginActionAnswer loginActionAnswer) {

        if (loginActionAnswer.answer == USERNAME_AVAILABLE )
            this.loginCase = USERNAME_AVAILABLE;

        else if (loginActionAnswer.answer == ALREADY_IN_GAME)
            this.loginCase = ALREADY_IN_GAME;
        else
            this.loginCase=USERNAME_ALREADY_TAKEN;


    }

    @Override
    public void handle(JoinActionAnswer joinActionAnswer) {

        this.joinCase=joinActionAnswer.answer;

    }

    @Override
    public void handle(PassActionAnswer passActionAnswer) {
        if (!passActionAnswer.answer.equals(true))
            System.out.println("It's not your turn!");

    }

    @Override
    public void handle(DiceInsertedAnswer diceInsertedAnswer)  {


        if (diceInsertedAnswer.answer.equals(false))
            try {
                throw diceInsertedAnswer.exception;
            } catch (Exception e) {
                System.out.println( e.getMessage());
            }

    }

    @Override
    public void handle(PatternAnswer patternAnswer) {
        System.out.println("Pattern ok");
    }


}
