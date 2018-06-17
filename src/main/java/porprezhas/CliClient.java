package porprezhas;

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

public class CliClient {

    private ViewUpdateHandlerInterface viewUpdateHandlerInterface;
    private String username;
    private ServerRMIInterface server;
    private Registry registry;
    private final Scanner in;
    private ClientObserver clientObserver;
    private String typeConnection;
    private int port=1457;
    private InetAddress ip;

    public static final String LOGIN_COMMAND = "login";
    public static final String JOIN_COMMAND = "join";
    public static final String LOGOUT_COMMAND = "logout";
    public static final String INSERT_DICE_COMMAND = "dice";
    public static final String CHOOSE_PATTERN = "choosePattern";
    public static final String PASS = "pass";
    public static final String USE_TOOL_CARD = "card";
    public static final String SOCKET = "s";
    public static final String RMI = "r";

    public CliClient() {
        this.in = new Scanner(System.in);
    }

    private void printHelp() {
        System.out.println(">>> Available commands:");
        System.out.println("\t" + LOGOUT_COMMAND + ":\t\t logout");
        System.out.println("\t" + JOIN_COMMAND + ":\t\t join a new Game");
        System.out.println("\t" + INSERT_DICE_COMMAND + "[index] [row] [column]: \t insert a new Dice");
        System.out.println("\t" + CHOOSE_PATTERN + ":\t\t choose a pattern");
        System.out.println("\t" + PASS + ":\t\t end your turn");
        System.out.println("\t" + USE_TOOL_CARD + ":\t\t use a tool card");
    }



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


        String command;
        do {
            command =  in.nextLine();

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
                            ClientActionSingleton.getClientAction().moveDice(0, diceID, 0,
                                    Integer.parseInt(splittedStrings[2]) -1 , Integer.parseInt(splittedStrings[3])-1);

                        break;
                    case CHOOSE_PATTERN:

                        break;
                    case PASS:

                        ClientActionSingleton.getClientAction().pass();

                        break;
                    case USE_TOOL_CARD:
                        ArrayList<Integer> paramList=new ArrayList<Integer>();
                        for (int i=2; i < splittedStrings.length; i ++)
                            paramList.add(Integer.parseInt(splittedStrings[i])-1);

                        try {



                            if(server.usedToolCard(username,Integer.parseInt(splittedStrings[1])-1 , paramList)) {

                                System.out.println("ToolCardUsed!");


                            } else {
                                System.out.println("Param error");
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            System.err.println(e.getMessage());     // print Invalid Move Message
                        }
                        break;
                    default:
                        printHelp();
                        break;
                }

            }
        } while (!command.equals(LOGOUT_COMMAND));

        server.logout(username);
        System.out.println("\t\tBYE!");
    }

    public void loginPashe(){
       Boolean bLog=false;
       while (!bLog) {
           System.out.println(">>> Login:");
           username = in.nextLine();

           // if user typed his UserName


           // Start the Connection to the server
           if (this.typeConnection.equals(RMI)) {
               try {
                   ClientActionSingleton.setClientActionInstance(new RMIClientAction());
               } catch (RemoteException e) {
                   System.err.println(e.getMessage());
               } catch (NotBoundException e) {
                   System.err.println(e.getMessage());
               }
           } else if (this.typeConnection.equals(SOCKET)) {
               try {
                   ip = InetAddress.getLocalHost();
                   ClientActionSingleton.setClientActionInstance(new SocketClientAction(ip, port));
               } catch (UnknownHostException e) {
                   System.err.println(e.getMessage());
               }
           }

           // Error cached
           if (null == ClientActionSingleton.getClientAction() ||
                   !ClientActionSingleton.getClientAction().isConnected()) {
               System.out.println("404: server NOT Found");

               // Connected to server
           } else {
               // Try to Login with given user name
               if (ClientActionSingleton.getClientAction().login(username)) {
                   // Logged In
                   bLog=true;

                   // Login Failed!!!
               } else {
                   System.out.println("Invalid username");
                   //if there is an active Socket connection we close it
                   if (this.typeConnection.equals(SOCKET) && ClientActionSingleton.getClientAction() != null)
                       ((SocketClientAction) ClientActionSingleton.getClientAction()).closeConnection();
               }
           }
       }
    }

    public void joinphase(){

        System.out.println("Type 'join' to start a new game");
        String command = in.nextLine();

       if(command.equals(JOIN_COMMAND)){
        viewUpdateHandlerInterface = new CLIViewUpdateHandler(username);


        if(this.typeConnection.equals(RMI)) {
            try {
                ClientObserver clientObserver = new ClientObserver(viewUpdateHandlerInterface, username);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
        ClientActionSingleton.getClientAction().join(viewUpdateHandlerInterface);
    }

    }

    public static void main(String[] args) {
        CliClient cliClient = new CliClient();
        try {
            cliClient.runSagrada();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}
