package porprezhas.Network;

import porprezhas.Network.command.*;
import porprezhas.model.Game;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import static porprezhas.control.ServerController.ALREADY_IN_GAME;
import static porprezhas.control.ServerController.USERNAME_ALREADY_TAKEN;
import static porprezhas.control.ServerController.USERNAME_AVAILABLE;

/**
 * This class handles the actions from the client to the server when it uses Socket
 */
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

        }catch (ConnectException e){
            System.out.println("The server is not ready");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("Connection established");

    }

    @Override
    public boolean isConnected() {
        return null != socket;
    }

    /**
     * This method handles login action with Socket.
     * It waits for the answer from the answer and sends it to the handler,
     * that handles the answer of the login
     * @param username username that the user wants
     * @return the answer of the login
     */
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

    /**
     * This method handles the join action for Socket and creates a new Thread that receives
     * all the answers and updates from the server
     * @param viewUpdateHandlerInterface The handler(CLI or GUI) of the updates from the server
     * @return
     */
    @Override
    public boolean join(ViewUpdateHandlerInterface viewUpdateHandlerInterface)   {
        this.viewUpdateHandlerInterface=viewUpdateHandlerInterface;
      
        try {

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
    /**
     * This method handles the join action for single player for Socket and creates a new Thread that receives
     * all the answers and updates from the server
     * @param viewUpdateHandlerInterface The handler(CLI or GUI) of the updates from the server
     * @return
     */
    @Override
    public boolean joinSinglePlayer(ViewUpdateHandlerInterface viewUpdateHandlerInterface, Game.SolitaireDifficulty solitaireDifficulty) {
        this.viewUpdateHandlerInterface=viewUpdateHandlerInterface;

        try {

            socketOut.writeObject(new JoinSinglePlayerAction(username, solitaireDifficulty));
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

    /**
     * This method handles the resume of a game after a lost connection for socket
     * @param viewUpdateHandlerInterface he handler(CLI or GUI) of the updates from the server
     * @return
     */
    @Override
    public boolean resumeGame(ViewUpdateHandlerInterface viewUpdateHandlerInterface) {
        this.viewUpdateHandlerInterface=viewUpdateHandlerInterface;
        viewUpdateHandlerInterface.setGameStarted(true);
        try {

            socketOut.writeObject(new ResumeGameAction(username));
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

    /**
     * This method write a socket insert dice action
     * @param diceID id of the dice
     * @param row row of the board
     * @param col column of the board
     */
    @Override
    public void insertDice(long diceID, int row, int col) {
        try {
             socketOut.writeObject(new InsertDiceAction(username, diceID, row, col ));
             socketOut.flush();


        }  catch (Exception e) {

            System.err.println(e.getMessage());     // print Invalid Move Message
//            if(bDebug)
//                e.printStackTrace();
        }


    }

    /**
     * This method writes a pass action for socket
     */
    @Override
    public void pass() {
        try {
            socketOut.writeObject(new PassAction(username));
            socketOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method writes a use tool card action for socket
     * @param username username of the user
     * @param toolCardID ID of the toolCard
     * @param paramList List of param that the card needs
     * @return
     */
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

    /**
     * This method writes a choose pattern action for socket
     * @param patternIndex Index of the choosen pattern
     */
    @Override
    public void choosePattern(int patternIndex) {
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

    /**
     * This method handles an update answer from the server and it passes it to the ViewUpdateHandler
     * @param updateAnswer
     */
    @Override
    public void handle(UpdateAnswer updateAnswer) {

        this.viewUpdateHandlerInterface.update(updateAnswer.serializableGameInterface);

    }

    /**
     * This method handles the Login action Answer from the server
     * @param loginActionAnswer
     */
    @Override
    public void handle(LoginActionAnswer loginActionAnswer) {

        if (loginActionAnswer.answer == USERNAME_AVAILABLE )
            this.loginCase = USERNAME_AVAILABLE;

        else if (loginActionAnswer.answer == ALREADY_IN_GAME)
            this.loginCase = ALREADY_IN_GAME;
        else
            this.loginCase=USERNAME_ALREADY_TAKEN;


    }

    /**
     * This method handles the answer of join action
     * @param joinActionAnswer
     */
    @Override
    public void handle(JoinActionAnswer joinActionAnswer) {

        this.joinCase=joinActionAnswer.answer;

    }

    /**
     * This method handles the pass action answer from the server and passe it to the view update handler
     * @param passActionAnswer
     */
    @Override
    public void handle(PassActionAnswer passActionAnswer) {
        if (!passActionAnswer.answer.equals(true))
          viewUpdateHandlerInterface.invalidAction();

    }

    /**
     * This method handles the answer of a dice insert. If the answer contains an exceptions it passes
     * it to the view update handler
     * @param diceInsertedAnswer
     */
    @Override
    public void handle(DiceInsertedAnswer diceInsertedAnswer)  {


        if (diceInsertedAnswer.answer.equals(false) && diceInsertedAnswer.exception!=null)
            try {
                throw diceInsertedAnswer.exception;
            } catch (Exception e) {
               this.viewUpdateHandlerInterface.invalidDiceInsert(e);
            }

    }

    @Override
    public void handle(PatternAnswer patternAnswer) {

    }
    /**
     * This method handles the answer of a tool card use. If the answer contains an exceptions it passes
     * it to the view update handler
     * @param useToolCardAnswer
     */
    @Override
    public void handle(UseToolCardAnswer useToolCardAnswer) {

        if (useToolCardAnswer.answer.equals(false) && useToolCardAnswer.exception!=null)
            try {
                throw useToolCardAnswer.exception;
            } catch (Exception e) {
                this.viewUpdateHandlerInterface.invalidUseToolCard(e);
            }
    }

    /**
     * This method handles the answer for a second step of a card use. It passes the list of dice
     * to view update handler.
     * @param cardEffectAnswer
     */
    @Override
    public void handle(CardEffectAnswer cardEffectAnswer) {
        viewUpdateHandlerInterface.handleCardEffect(cardEffectAnswer.diceList);
    }
}
