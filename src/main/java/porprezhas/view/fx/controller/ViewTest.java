package porprezhas.view.fx.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import porprezhas.model.Game;
import porprezhas.model.Player;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.Pattern;
import porprezhas.view.fx.component.ConfirmBox;


public class ViewTest extends Application {
    public static class GUIConstants {
        public static final int ICON_QUANTITY = 145;   // index from 0 to 144
    }

    private Stage primaryStage;
    private AnchorPane rootLayout;


    private List<Player> players;
    private List<GameViewController.PlayerInfo> playersInfo;

    private GameViewController gameViewController;

    public ViewTest() {
        players = new ArrayList<>();
        players.add( new Player("me"));
        players.add( new Player("p1"));
        players.add( new Player("p2"));
        players.add( new Player("p3"));

        this.playersInfo = new ArrayList<>();
        Random random = new Random();
        for (Player player : players) {
            playersInfo.add(new GameViewController.PlayerInfo(
                    player.getName(),
                    random.nextInt(GUIConstants.ICON_QUANTITY),
                    Pattern.TypePattern.values()[1]));
        }
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


        Random random = new Random();
        for (int col = 0; col < Game.GameConstants.ROUND_NUM; col++) {
            if (random.nextInt(10) < 2) {
                for (int row = 0; row < Game.GameConstants.MAX_DICE_PER_ROUND; row++) {
                    gameViewController.addDiceToRoundTrack(col,
                            new Dice(random.nextInt(6) + 1,
                                    Dice.ColorDice.values()[random.nextInt(Dice.ColorDice.values().length - 1)])
                    );
                }
            } else {
                gameViewController.addDiceToRoundTrack(col,
                        new Dice(random.nextInt(6) + 1,
                                Dice.ColorDice.values()[random.nextInt(Dice.ColorDice.values().length - 1)]) );
                for (int row = 0; row < Game.GameConstants.MAX_DICE_PER_ROUND; row++) {
                    if (random.nextInt(10) < 3) {
                        gameViewController.addDiceToRoundTrack(col,
                                new Dice(random.nextInt(6) + 1,
                                        Dice.ColorDice.values()[random.nextInt(Dice.ColorDice.values().length - 1)]) );

                    }
                }
            }
        }
        for (int i = 0; i < gameViewController.num_player; i++) {
            // insert a lot of dices to test
            for (int col = 0; col < GameViewController.BOARD_COLUMN; col++) {
                for (int row = 0; row < GameViewController.BOARD_ROW; row++) {
                    if (random.nextInt(10) < 6) {
                        gameViewController.addDice(i,
                                new Dice(random.nextInt(6) + 1,
                                            Dice.ColorDice.values()[random.nextInt(Dice.ColorDice.values().length - 1)]),
                                col, row );
                    }
                }
            }
        }
//        showElementOverview();
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

            // Create a controller instance, passing the information about players
            gameViewController = new GameViewController(playersInfo);
            // Set it in the FXMLLoader
            loader.setController(gameViewController);   // I haven't set the controller in fxml because i want the controller get setup at construction

            // Load root layout from fxml file.
            rootLayout = loader.load();     // NOTE: If you get ONLY one ERROR in this line, it may because you haven't mark the folder 'resource' as resources root
                                            //       look for folder resource on the project root path, there is a folder resource, right click and choose in the end of list: 'Mark directory as'
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
        launch(args);
    }
}


// TODO: every DICE fall around the center of draft poll
//       every dice move away from the colliding dices, in the rival direction
//       this should be done in 3 seconds, so make this move a bit away from center to speed up