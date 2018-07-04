package porprezhas.client;

import porprezhas.Network.*;
import porprezhas.Network.rmi.client.ClientObserver;
import porprezhas.Network.rmi.common.ServerRMIInterface;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

/**
* This is the main class for CLI client.
* The user can choose here which type of connection use and the type of game( single player or multiplayer).
* */

public class CliClient {

    private ViewUpdateHandlerInterface viewUpdateHandlerInterface;
    private String username;

    private final Scanner in;
    private String typeConnection;
    private int port;
    private InetAddress ip;

    private int myPort=5000;
    private Boolean alreadyIn=false;
    private String command;


    public static final String JOIN_COMMAND = "join";
    public static final String RESUME_COMMAND = "resume";
    public static final String SINGLE_PLAYER = "single";
    public static final String LOGOUT_COMMAND = "logout";
    public static final String INSERT_DICE_COMMAND = "dice";
    public static final String PASS = "pass";
    public static final String USE_TOOL_CARD = "card";
    public static final String SOCKET = "s";
    public static final String RMI = "r";
    public static final String RETURN_TO_HOME = "return";

    public CliClient(InetAddress ip, int port) {
        this.in = new Scanner(System.in);
        this.ip=ip;
        this.port=port;

    }
    public CliClient(InetAddress ip, int port, int myPort) {
        this.in = new Scanner(System.in);
        this.ip=ip;
        this.port=port;
        this.myPort=myPort;

    }

    /** This method prints all the possible moves of the player
     *
     */
    private void printHelp() {
        System.out.println(">>> Available game commands:");

        System.out.println("\t" + INSERT_DICE_COMMAND + "[index] [row] [column]: \t insert a new Dice");
        System.out.println("\t" + PASS + ":\t\t end your turn");
        System.out.println("\t" + USE_TOOL_CARD + ":\t\t use a tool card");
    }


    /**This is the central method of cli client.
     * It calls all the phases needed to prepare the game and handles
     * the input of the user.
     *
     * @throws RemoteException
     * @throws NotBoundException
     */
    public void runSagrada() throws RemoteException, NotBoundException {
        System.out.println("\t\tSAGRADA\t\t");
        System.out.println("Type 's' for socket or 'r' for Rmi");
        typeConnection= in.nextLine();;
        while(!typeConnection.equals(SOCKET) && !typeConnection.equals(RMI)){
            System.out.println("Type 's' for socket or 'r' for Rmi");
            typeConnection=in.nextLine();
        }
        this.loginPashe();
        this.joinphase();




        do {
            if (!alreadyIn) {
                in.reset();
                command = in.nextLine();
            }
            alreadyIn=false;
            String[] splittedStrings = command.split(" ");


            command= splittedStrings[0];

            if (!command.equals(LOGOUT_COMMAND) ) {


                switch (command) {

                    case INSERT_DICE_COMMAND:
                        if (splittedStrings.length<4){
                            System.out.println("command param Error");
                            break;
                        }

                            int index = Integer.parseInt(splittedStrings[1]) -1;
                            long diceID = ((CLIViewUpdateHandler) viewUpdateHandlerInterface).getID(index);
                            ClientActionSingleton.getClientAction().insertDice( diceID,
                                    Integer.parseInt(splittedStrings[2]) -1 , Integer.parseInt(splittedStrings[3])-1);

                        break;

                    case PASS:

                        ClientActionSingleton.getClientAction().pass();

                        break;
                    case USE_TOOL_CARD:
                        ArrayList<Integer> paramList=new ArrayList<Integer>();
                        for (int i=2; i < splittedStrings.length; i ++)
                            paramList.add(Integer.parseInt(splittedStrings[i])-1);

                            ClientActionSingleton.getClientAction().useToolCard(username,Integer.parseInt(splittedStrings[1])-1 , paramList);

                        break;
                    default:
                        if (!command.equals(RETURN_TO_HOME))
                        printHelp();
                        break;
                }

            }
        } while (!command.equals(RETURN_TO_HOME));

        System.out.println("Type 'join' to start a new game");
        System.out.println("Type 'single' to start a new single player game");
        this.joinphase();
    }

    /**
     * This method handles login phase of cli user.
     * It prepares all the connection information and calls clientActionSingleton.
     * It prints also the result of this action with 3 possibilities:
     * Username already taken
     * Username available
     * Username with lost connection
     */
    public void loginPashe(){
       Boolean bLog=false;
       while (!bLog) {
           System.out.println(">>> Login:");
           username = in.nextLine();

           // if user typed his UserName


           // Start the Connection to the server
           if (this.typeConnection.equals(RMI)) {
               try {

                   ClientActionSingleton.setClientActionInstance(new RMIClientAction(ip.getHostAddress(), port-2));
               } catch (RemoteException e) {
                   System.err.println(e.getMessage());
               } catch (NotBoundException e) {
                   System.err.println(e.getMessage());
               }
           } else if (this.typeConnection.equals(SOCKET)) {

                   ClientActionSingleton.setClientActionInstance(new SocketClientAction(ip, port));

           }

           // Error cached
           if (null == ClientActionSingleton.getClientAction() ||
                   !ClientActionSingleton.getClientAction().isConnected()) {
               System.out.println("Error Code = 404: Server NOT Found");

               // Connected to server
           } else {
               // Try to Login with given user name
               int resultLogin=ClientActionSingleton.getClientAction().login(username);
               if (resultLogin == 0) {
                   // Logged In
                   bLog=true;
                   System.out.println("Logged in!\n");
                   System.out.println("Type 'join' to start a new game");
                   System.out.println("Type 'single' to start a new single player game");

               }
               else if (resultLogin == 1){
                   bLog=true;
                   System.out.println("Logged in!");
                   System.out.println("There is an active game with this username");
                   System.out.println("Type 'resume' to resume the active game or 'join' to start a new game");
                   System.out.println("Type 'single' to start a new single player game");

               }
               else {// Login Failed!!!
                   System.out.println("Invalid username");
                   //if there is an active Socket connection we close it
                   if (this.typeConnection.equals(SOCKET) && ClientActionSingleton.getClientAction() != null)
                       ((SocketClientAction) ClientActionSingleton.getClientAction()).closeConnection();
               }
           }
       }
    }

    /**This method handles join phase of cli user. It handles both join and resume cases.
     *
     *
     */
    public void joinphase(){

        String command = in.nextLine();
        while (!command.equals(JOIN_COMMAND) && !command.equals(RESUME_COMMAND) && !command.equals(SINGLE_PLAYER)){
            System.out.println("Please type the correct command");
            command=in.nextLine();
        }

        if(command.equals(JOIN_COMMAND)){
            viewUpdateHandlerInterface = new CLIViewUpdateHandler(username);
        if(this.typeConnection.equals(RMI)) {
            try {
                ClientObserver clientObserver = new ClientObserver(viewUpdateHandlerInterface, username, myPort);
                ((RMIClientAction) ClientActionSingleton.getClientAction()).setMyport(myPort);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
           if (ClientActionSingleton.getClientAction().join(viewUpdateHandlerInterface)) {
               System.out.println("Joined in the game successfully!\n");
               System.out.println("Wait until the game is ready to start");

           }
            this.choosePatternPhase();
        }

        if(command.equals(RESUME_COMMAND)){
            viewUpdateHandlerInterface = new CLIViewUpdateHandler(username);

            if(this.typeConnection.equals(RMI)) {
                try {
                    ClientObserver clientObserver = new ClientObserver(viewUpdateHandlerInterface, username, myPort);
                    ((RMIClientAction) ClientActionSingleton.getClientAction()).setMyport(myPort);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            if (ClientActionSingleton.getClientAction().resumeGame(viewUpdateHandlerInterface)) {
                System.out.println("Resume the game successfully!\n");
            }
        }

    }

    /**With this method user can choose is pattern.
     * If he wait too long and the pattern is choosen by the server
     * and the game is started the command is the first move of the player.
     * Boolean Already is used to skip a second request of input in thi case.
     */
    public void choosePatternPhase(){
        in.reset();
        int i;
        command=in.nextLine();
        try {
            i= Integer.parseInt(command);
       }
       catch (NumberFormatException e){
           i=1;
           alreadyIn=true;
       }
        while (!(i<5 && i>0))
            i=in.nextInt();
        ClientActionSingleton.getClientAction().choosePattern(i-1);

    }

    public static void main(String[] args) throws UnknownHostException {
        final int port = 58090+1;
        CliClient cliClient = new CliClient(InetAddress.getLocalHost(), port);
        try {
            cliClient.runSagrada();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}
