package porprezhas.Network;

import porprezhas.Network.Command.*;
import porprezhas.model.SerializableGameInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class CLISocketClient implements AnswerHandler {
    private ViewUpdateHandlerInterface viewUpdateHandlerInterface;
    private String username;
    private int port;

    private final Scanner in;
    private ClientObserver clientObserver;
    Boolean flag=false;
    Socket socket ;
   ObjectInputStream socketIn;
    ObjectOutputStream socketOut;


    public static final String LOGIN_COMMAND = "login";
    public static final String JOIN_COMMAND = "join";
    public static final String LOGOUT_COMMAND = "logout";
    public static final String INSERT_DICE_COMMAND = "insertDice";
    public static final String CHOOSE_PATTERN = "choosePattern";
    public static final String PASS = "pass";
    public static final String USE_TOOL_CARD = "useToolCard";



    public CLISocketClient(InetAddress ip , int port) throws IOException {

        this.port=port;
        this.in = new Scanner(System.in);
        socket = new Socket(ip, port);
        System.out.println("Connection established");
      socketOut = new ObjectOutputStream(socket.getOutputStream());
        socketIn = new ObjectInputStream(socket.getInputStream());
    }

    private void printHelp() {
        System.out.println(">>> Available commands:");
        System.out.println("\t" + LOGOUT_COMMAND + ":\t\t logout");
        System.out.println("\t" + JOIN_COMMAND + ":\t\t join a new Game");
        System.out.println("\t" + INSERT_DICE_COMMAND + ":\t\t insert a new Dice");
        System.out.println("\t" + CHOOSE_PATTERN + ":\t\t choose a pattern");
        System.out.println("\t" + PASS + ":\t\t end your turn");
        System.out.println("\t" + USE_TOOL_CARD + ":\t\t use a tool card");
    }

    public void run() {
        this.login();
        this.join();
        Boolean run=true;
        String command=null;
        while (run){
            try {
               if(command==null) ((Answer) socketIn.readObject()).handle(this);

            //                command =  in.nextLine();


            } catch (IOException e) {
                System.err.println("Exception on network: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }



    }

        @Override
    public void handle(UpdateAnswer updateAnswer) {

        this.viewUpdateHandlerInterface.update(updateAnswer.serializableGameInterface);
    }

    @Override
    public void handle(LoginActionAnswer loginActionAnswer) {

        if(loginActionAnswer.answer.equals(true))
            flag=true;
        else
            System.out.println("Username not available");

    }

    @Override
    public void handle(JoinActionAnswer joinActionAnswer) {
        if(joinActionAnswer.answer.equals(true)) {
            System.out.println("You join a game");
            flag=true;
        }
        else
            System.out.println("Error in join phase");
    }

    @Override
    public void handle(PassActionAnswer passActionAnswer) {
        if (!passActionAnswer.answer.equals(true))
            System.out.println("It's not your turn!");
    }

    public void login(){

        System.out.println("\t\tSAGRADA\t\t");


        while (!flag) {
            System.out.println(">>> Login:");
            username = in.nextLine();

            try {
                socketOut.writeObject(new LoginAction(username));
                socketOut.flush();
                ((Answer) socketIn.readObject()).handle(this);

            } catch (IOException e) {
                System.err.println("Exception on network: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
        this.viewUpdateHandlerInterface=new CLIViewUpdateHandler(username);
        System.out.println("Login effettuato correttamente");
        System.out.println();
    }

    public void join(){
        flag=false;
        while(!flag){
            try {

                socketOut.writeObject(new JoinAction(username));
                socketOut.flush();
                ((Answer) socketIn.readObject()).handle(this);

                System.out.println("tento il join");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public void action(String command) throws IOException, ClassNotFoundException {
        do {


            if (!command.equals(LOGOUT_COMMAND) ) {
                String parsingString= null;
                String method=null;
                int space=0;
                if(command.contains(" ")){
                    space = command.indexOf(" ");
                    parsingString = command.substring(space + 1, command.length());
                    method = command.substring(0, space);
                }
                else
                    method=command;
                switch (method){

                    case INSERT_DICE_COMMAND:
                        int space2 = space+1 + parsingString.indexOf(" ");
                        parsingString = parsingString.substring(space2 - space, parsingString.length());
                        int space3 = space2+1 + parsingString.indexOf(" ");
                        String numberDice = command.substring(space+1 , space2 );
                        String xBoardValue= command.substring(space2+1, space3);
                        String yBoardValue= command.substring(space3+1, command.length());
                        socketOut.writeObject(new InsertDiceAction(username, Integer.parseInt(numberDice) -1,Integer.parseInt(xBoardValue)-1,Integer.parseInt(yBoardValue)-1 ));
                        socketOut.flush();

                        break;

                    case PASS:

                        socketOut.writeObject(new PassAction(username));
                        socketOut.flush();
                        ((Answer) socketIn.readObject()).handle(this);
                        break;
                    case USE_TOOL_CARD:
                        break;
                    default:
                        printHelp();
                        break;
                }

            }
        } while (!command.equals(LOGOUT_COMMAND));

    }
}
