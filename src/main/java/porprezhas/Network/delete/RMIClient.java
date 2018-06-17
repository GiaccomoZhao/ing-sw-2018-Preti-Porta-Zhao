package porprezhas.Network.delete;

import com.github.javafaker.Faker;
import porprezhas.Network.CLIViewUpdateHandler;
import porprezhas.Network.ViewUpdateHandlerInterface;
import porprezhas.Network.rmi.client.ClientObserver;
import porprezhas.Network.rmi.common.ServerRMIInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

public class RMIClient implements RMIClientInterface, Runnable {

    private ViewUpdateHandlerInterface viewUpdateHandlerInterface;
    private String username;
    private ServerRMIInterface server;
    private Registry registry;
    private final Scanner in;
    private ClientObserver clientObserver;


    public static final String LOGIN_COMMAND = "login";
    public static final String JOIN_COMMAND = "join";
    public static final String LOGOUT_COMMAND = "logout";
    public static final String INSERT_DICE_COMMAND = "dice";
    public static final String CHOOSE_PATTERN = "choosePattern";
    public static final String PASS = "pass";
    public static final String USE_TOOL_CARD = "card";



    public RMIClient() throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry();
        server = (ServerRMIInterface) registry.lookup("serverController");
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

    public void run() {
        try {
            runSagrada();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
    public void runSagrada() throws RemoteException, NotBoundException {
        System.out.println("\t\tSAGRADA\t\t");
        Boolean bLoggedIn=false;


        while(!bLoggedIn) {
            System.out.println(">>> Login:");
//            username = in.nextLine();

            Faker faker = new Faker();
            String firstName = faker.name().firstName();
            String playerName = firstName; //"P" + i;
            username = playerName;

            bLoggedIn = server.login(username);

        }


        System.out.println("Login effettuato correttamente");
        System.out.println();
        viewUpdateHandlerInterface = new CLIViewUpdateHandler(username);
        clientObserver = new ClientObserver(viewUpdateHandlerInterface, username);
        //ClientActionSingleton.setClientActionInstance(new RMIClientAction(server, "Matteo"));
        server.joinGame(username);


        //TO_DO ADD OBSERVER

        String command;
        do {
            command =  in.nextLine();

            String[] splittedStrings = command.split(" ");

            for (String s : splittedStrings)
                System.out.println(s);

            command= splittedStrings[0];

            if (!command.equals(LOGOUT_COMMAND) ) {


                switch (command) {
                    case JOIN_COMMAND:
                        server.joinGame(username);
                        break;
                    case INSERT_DICE_COMMAND:
                        if (splittedStrings.length<4){
                            System.out.println("command param Error");
                            break;
                        }
                        try {

                            int index = Integer.parseInt(splittedStrings[1]) -1;
                            long diceID = ((CLIViewUpdateHandler) viewUpdateHandlerInterface).getID(index);

                            if(server.insertedDice(diceID,Integer.parseInt(splittedStrings[2])-1 , Integer.parseInt(splittedStrings[3])-1, username )) {

                                System.out.println("Dice inserted!");
                                //TO-DO fix
                                this.server.passUser(username);
                            } else {
                                System.out.println("Not valid move");
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            System.err.println(e.getMessage());     // print Invalid Move Message
//                            if(bDebug)
//                                e.printStackTrace();
                        }
                        break;
                    case CHOOSE_PATTERN:

                        break;
                    case PASS:

                        if(!server.passUser(username))
                            System.out.println(" Non è il tuo turno.");
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
}
