package porprezhas.Network;

import porprezhas.control.ServerRMIInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
    public static final String INSERT_DICE_COMMAND = "insertDice";
    public static final String CHOOSE_PATTERN = "choosePattern";
    public static final String PASS = "pass";
    public static final String USE_TOOL_CARD = "useToolCard";



    public RMIClient() throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry();
        server = (ServerRMIInterface) registry.lookup("serverController");
        this.in = new Scanner(System.in);
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
            username = in.nextLine();

            bLoggedIn = server.login(username);

        }


        System.out.println("Login effettuato correttamente");
        System.out.println();
        viewUpdateHandlerInterface = new CLIViewUpdateHandler(username);
        clientObserver = new ClientObserver(viewUpdateHandlerInterface, username);
        server.joinGame(username);


        //TO_DO ADD OBSERVER

        String command;
        do {
            command =  in.nextLine();


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

                switch (method) {
                    case JOIN_COMMAND:
                        server.joinGame(username);
                        break;
                    case INSERT_DICE_COMMAND:
                        int space2 = space+1 + parsingString.indexOf(" ");
                        parsingString = parsingString.substring(space2 - space, parsingString.length());
                        int space3 = space2+1 + parsingString.indexOf(" ");
                        String numberDice = command.substring(space+1 , space2 );
                        String xBoardValue= command.substring(space2+1, space3);
                        String yBoardValue= command.substring(space3+1, command.length());
                        if(server.insertedDice(Integer.parseInt(numberDice),Integer.parseInt(xBoardValue) , Integer.parseInt(yBoardValue), username )) {
                            System.out.println("Mossa ok");
                            //TO-DO fix
                            this.server.passUser(username);
                        }
                        else
                            System.out.println("Mossa non valida");
                        break;
                    case CHOOSE_PATTERN:

                        break;
                    case PASS:

                        if(!server.passUser(username))
                            System.out.println(" Non è il tuo turno.");
                        break;
                    case USE_TOOL_CARD:
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
