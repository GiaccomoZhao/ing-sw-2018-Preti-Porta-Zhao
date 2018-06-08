package porprezhas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import porprezhas.Network.*;
import porprezhas.control.ServerRMIInterface;
import porprezhas.model.Game;
import porprezhas.model.Player;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.Pattern;
import porprezhas.view.fx.component.BackgroundMusicPlayer;
import porprezhas.view.fx.component.ConfirmBox;
import porprezhas.view.fx.controller.GameViewController;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

import static porprezhas.view.fx.GuiSettings.*;
import static porprezhas.view.fx.GuiSettings.FPS_PRINT_AT_MIN;

public class GuiRmiClient extends Application implements RMIClientInterface {
    static int mainPlayerPosition;  // to identify which player am i?

    private Stage primaryStage;
    private AnchorPane rootLayout;


    private List<Player> players;
    private List<GameViewController.PlayerInfo> playersInfo;

    private GameViewController gameViewController;


    public static void startRMI() throws RemoteException, NotBoundException {
        RmiClientGUI rmiClient = new RmiClientGUI();

        Registry registry= LocateRegistry.getRegistry();


        new Thread(rmiClient).start();
       // ClientActionSingleton.setClientActionInstance(new RMIClientAction());
    }


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("tester");

        // initialize the layout from fxml and
        // setup the game GUI in base the player info!!!
        initRootLayout();

        // application GUI logic
        initMainLogic();

        // output FPS
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                System.out.println("FPS " + com.sun.javafx.perf.PerformanceTracker.getSceneTracker(primaryStage.getScene()).getInstantFPS());
            }
        }, 0, (long) (60*1000.0 / FPS_PRINT_AT_MIN));

        // play background music
        BackgroundMusicPlayer.playMusic();
    }


    private void initMainLogic (){
        primaryStage.setOnCloseRequest(event -> {
            event.consume();    // consume close_request, because we are going to handle it
            quitGame();
        });
/*      this is notified before maximizing the window, I have to work with old size, so this is useless....
        primaryStage.maximizedProperty().addListener((ov, t, t1) -> {
            gameViewController.updateSize();
//            if(bDebug)
//                System.out.println("maximized:" + t1.booleanValue());
        });
*/
    }

    private void quitGame() {
        Boolean bQuit = new ConfirmBox().display("Title", "Are you sure to quit during a Game?");
        if(bQuit) {
            primaryStage.close();
        }
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // create a FXMLLoader and open fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/GameView.fxml"));
            if(loader == null)
                System.err.println(this + ": Error with loader.setLocation(" + getClass().getResource("/GameView.fxml") + ")");



            // TODO: test-use, This should be initialized before this scene starts
            Random random = new Random();
            // add all players
            players = new ArrayList<>();
            players.add( new Player("P1"));
            players.get(players.size()-1).setPosition(players.size()-1);
            players.get(players.size()-1).setIcon(random.nextInt(ICON_QUANTITY) +1);
            players.add( new Player("P2"));
            players.get(players.size()-1).setPosition(players.size()-1);
            players.get(players.size()-1).setIcon(random.nextInt(ICON_QUANTITY) +1);
            players.add( new Player("me"));
            players.get(players.size()-1).setPosition(players.size()-1);
            players.get(players.size()-1).setIcon(random.nextInt(ICON_QUANTITY) +1);
            players.add( new Player("P4"));
            players.get(players.size()-1).setPosition(players.size()-1);
            players.get(players.size()-1).setIcon(random.nextInt(ICON_QUANTITY) +1);

            this.playersInfo = new ArrayList<>();
            int i = 0;
            for (Player player : players) {
                playersInfo.add(new GameViewController.PlayerInfo(
                        i++,
                        player.getName(),
                        player.getIconId(),
                        Pattern.TypePattern.values()[1]));
            }



            // Create a controller instance, passing the information about players
            gameViewController = new GameViewController(playersInfo, mainPlayerPosition);



            // Set it in the FXMLLoader
            loader.setController(gameViewController);   // I haven't set the controller in fxml because i want the controller get setup at construction

            // Load root layout from fxml file.
            rootLayout = loader.load();     // NOTE: If you get ONLY one ERROR in this line, it may because you haven't mark the folder 'resource' as resources root
            //       look for folder resource on the project root path, there is a folder resource, right click and choose in the end of list: 'Mark directory as'
            // Solitaire has smaller window size
            if(playersInfo.size() == 1) {
                primaryStage.setWidth(SOLITAIRE_WIDTH);
            }

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input;
        System.out.println("输入玩家位置，type player position (from 0 to MAX_PLAYER_QUANTITY-1):");
        input = scanner.next();
        mainPlayerPosition = Integer.parseInt(input);

        try {
            startRMI();
        } catch (Exception e) {
            e.printStackTrace();
        }
        launch(args);
    }
}
