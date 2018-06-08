package porprezhas.RMI.Client;

import porprezhas.RMI.MainViewInterface;
import porprezhas.control.GameControllerInterface;
import porprezhas.control.ServerControllerInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public abstract class MainView  implements MainViewInterface {


/*    ServerControllerInterface serverController;
    GameControllerInterface gameController;
    ViewClient viewClient;
    private final Scanner in;
    private String username;
    private int numberPlayer;


    public static final String LOGIN_COMMAND = "login";
    public static final String JOIN_COMMAND = "join";
    public static final String LOGOUT_COMMAND = "logout";
    public static final String INSERT_DICE_COMMAND = "insertDice";
    public static final String CHOOSE_PATTERN = "choosePattern";
    public static final String PASS = "pass";
    public static final String USE_TOOL_CARD = "useToolCard";



    public MainView(int numberPlayer) throws RemoteException {
        super();
        this.in = new Scanner(System.in);


    }

    public ViewClient getViewClient() {
        return viewClient;
    }

    public void setServerController(ServerControllerInterface serverController) {
        this.serverController= serverController;
    }

    private void printHelp() {
        System.out.println(">>> Available commands:");
      //  System.out.println("\t" + LOGIN_COMMAND + ":\t\t login");
        System.out.println("\t" + LOGOUT_COMMAND + ":\t\t logout");
        System.out.println("\t" + JOIN_COMMAND + ":\t\t join a new Game");
        System.out.println("\t" + INSERT_DICE_COMMAND + ":\t\t insert a new Dice");
        System.out.println("\t" + CHOOSE_PATTERN + ":\t\t choose a pattern");
        System.out.println("\t" + PASS + ":\t\t end your turn");
        System.out.println("\t" + USE_TOOL_CARD + ":\t\t use a tool card");
    }

    public void run() throws RemoteException {
        System.out.println("\t\tSAGRADA\t\t");
        Boolean flag=false;

        while(!flag) {
                System.out.println(">>> Login:");
                username = in.nextLine();

                flag = serverController.login(username);

        }
        Registry registry = LocateRegistry.getRegistry();
        String clientView = username.concat("MainView");
        registry.rebind(clientView, this);
        System.out.println("Login effettuato correttamente");
        System.out.println();
        serverController.joinGame(username);

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
                            serverController.joinGame(username);
                            break;
                        case INSERT_DICE_COMMAND:
                            int space2 = space+1 + parsingString.indexOf(" ");
                            parsingString = parsingString.substring(space2 - space, parsingString.length());
                            int space3 = space2+1 + parsingString.indexOf(" ");
                            String numberDice = command.substring(space+1 , space2 );
                            String xBoardValue= command.substring(space2+1, space3);
                            String yBoardValue= command.substring(space3+1, command.length());
                            if(gameController.insertedDice(Integer.parseInt(numberDice),Integer.parseInt(xBoardValue) , Integer.parseInt(yBoardValue), username )) {
                                System.out.println("Mossa ok");
                                //TO-DO fix
                                this.gameController.pass();
                            }
                            else
                                System.out.println("Mossa non valida");
                            break;
                        case CHOOSE_PATTERN:

                            break;
                        case PASS:

                            if(!this.gameController.passUser(username))
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

            serverController.logout(username);
        System.out.println("\t\tBYE!");
    }

    @Override
    public Boolean addGameController(int indexGameController) throws RemoteException {
        String rmiGameControllerName= "GameController".concat(String.valueOf(indexGameController));
        Registry registry = LocateRegistry.getRegistry();
        try {
            this.gameController= (GameControllerInterface) registry.lookup(rmiGameControllerName);
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        this.viewClient= new ViewClient(numberPlayer, username);
        ClientObserver testObserver= null;
        try {
            testObserver = new ClientObserver( getViewClient(), indexGameController);
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        String observerRmiName = "observer".concat(username);
        registry.rebind(observerRmiName, testObserver);
        return true;
    }*/
}
